package com.example.rent_intrument_application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val instruments = listOf(
        Instrument("Guitar", 50, R.drawable.guitar_image),
        Instrument("Piano", 100, R.drawable.piano_image),
        Instrument("Violin", 75, R.drawable.drums_image)
    )

    private var currentInstrumentIndex = 0
    private var totalBorrowedCost = 0

    // A list to keep track of the borrowed items
    private val borrowedItems = mutableSetOf<String>()

    private lateinit var instrumentImage: ImageView
    private lateinit var instrumentName: TextView
    private lateinit var instrumentPrice: TextView
    private lateinit var borrowStatusText: TextView
    private lateinit var totalCostText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_first)

        instrumentImage = findViewById(R.id.instrument_image)
        instrumentName = findViewById(R.id.instrument_name)
        instrumentPrice = findViewById(R.id.instrument_price)
        borrowStatusText = findViewById(R.id.borrow_status)
        totalCostText = findViewById(R.id.total_borrowed_cost) // Reference to the total cost block

        updateInstrumentDisplay()

        findViewById<Button>(R.id.prev_button).setOnClickListener {
            if (currentInstrumentIndex > 0) {
                currentInstrumentIndex--
                updateInstrumentDisplay()
            }
        }

        findViewById<Button>(R.id.next_button).setOnClickListener {
            if (currentInstrumentIndex < instruments.size - 1) {
                currentInstrumentIndex++
                updateInstrumentDisplay()
            }
        }

        findViewById<Button>(R.id.borrow_button).setOnClickListener {
            val currentInstrument = instruments[currentInstrumentIndex]

            if (borrowedItems.contains(currentInstrument.name)) {
                // Show error if the item is already borrowed
                Toast.makeText(this, "${currentInstrument.name} is already borrowed!", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed to the second page
                val intent = Intent(this, SubActivity::class.java).apply {
                    putExtra("instrument_name", currentInstrument.name)
                    putExtra("instrument_price", currentInstrument.price)
                    putExtra("instrument_image", currentInstrument.imageRes)
                }
                startActivityForResult(intent, 100)
            }
        }
    }

    // Function to update the instrument display
    private fun updateInstrumentDisplay() {
        val currentInstrument = instruments[currentInstrumentIndex]
        instrumentImage.setImageResource(currentInstrument.imageRes)
        instrumentName.text = currentInstrument.name
        instrumentPrice.text = "Price per month: $${currentInstrument.price}"

        // Check if the item is already borrowed
        if (borrowedItems.contains(currentInstrument.name)) {
            borrowStatusText.text = "Status: Borrowed"
        } else {
            borrowStatusText.text = "Status: Available"
        }

        // Update total borrowed cost
        totalCostText.text = " $$totalBorrowedCost"
    }

    // Handle the result when returning from SubActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val borrowedInstrument = data?.getStringExtra("borrowed_instrument")
            val borrowedCost = data?.getIntExtra("borrowed_cost", 0) ?: 0

            borrowedInstrument?.let {
                borrowedItems.add(it)
                totalBorrowedCost += borrowedCost // Add the cost of the borrowed item to the total cost
                updateInstrumentDisplay()
            }
        }
    }
}

data class Instrument(val name: String, val price: Int, val imageRes: Int)
