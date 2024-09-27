package com.example.rent_intrument_application

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SubActivity : AppCompatActivity() {

    private lateinit var instrumentImage: ImageView
    private lateinit var instrumentName: TextView
    private lateinit var monthsSpinner: Spinner
    private lateinit var totalCostText: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var quantitySpinner: Spinner

    private var instrumentPrice: Int = 0
    private var selectedMonths: Int = 0
    private var selectedQuantity: Int = 1
    private var totalCost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_second)

        val instrumentNameValue = intent.getStringExtra("instrument_name")
        instrumentPrice = intent.getIntExtra("instrument_price", 0)
        val instrumentImageRes = intent.getIntExtra("instrument_image", 0)

        instrumentImage = findViewById(R.id.instrument_image_sub)
        instrumentName = findViewById(R.id.instrument_name_sub)
        monthsSpinner = findViewById(R.id.months_spinner)
        totalCostText = findViewById(R.id.total_cost)
        ratingBar = findViewById(R.id.rating_bar)
        quantitySpinner = findViewById(R.id.quantity_spinner)

        instrumentImage.setImageResource(instrumentImageRes)
        instrumentName.text = instrumentNameValue

        // Setup Spinner for selecting months
        ArrayAdapter.createFromResource(
            this,
            R.array.months_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthsSpinner.adapter = adapter
        }

        monthsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedMonths = position
                updateCost()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Setup Spinner for selecting quantity
        ArrayAdapter.createFromResource(
            this,
            R.array.quantity_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            quantitySpinner.adapter = adapter
        }

        quantitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedQuantity = position + 1 // Quantity ranges from 1 to 10
                updateCost()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        findViewById<Button>(R.id.proceed_button).setOnClickListener {
            if (selectedMonths == 0) {
                Toast.makeText(this, "Please select a valid number of months!", Toast.LENGTH_SHORT).apply {
                    setGravity(Gravity.BOTTOM, 0, 100)
                    show()
                }
            } else {
                // Return the borrowed cost back to MainActivity
                val resultIntent = Intent().apply {
                    putExtra("borrowed_instrument", instrumentNameValue)
                    putExtra("borrowed_cost", totalCost)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            finish()
        }
    }

    private fun updateCost() {
        totalCost = instrumentPrice * selectedMonths * selectedQuantity
        totalCostText.text = "Cost: $$totalCost"
    }
}
