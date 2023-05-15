package com.example.job_aid

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class Salary_Calculator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salary_calculator)

        val salary = findViewById<EditText>(R.id.salary)
        val noDays = findViewById<EditText>(R.id.noDays)
        val hourDay = findViewById<EditText>(R.id.hourDay)
        val calBtn = findViewById<Button>(R.id.calBtn)
        val calValue = findViewById<TextView>(R.id.calValue)
        val settingsBackBtn = findViewById<ImageView>(R.id.calBackBtn)



        // Set click listener for the Calculate button
        calBtn.setOnClickListener {
            // Get input values from EditText fields
            val salary = salary.text.toString().toFloat()
            val noDays = noDays.text.toString().toFloat()
            val hourDay = hourDay.text.toString().toFloat()

            // Calculate the value
            val calResult = salary * noDays * hourDay

            // Format the result to 2 decimal points
            val formattedResult = String.format("%.2f", calResult)

            // Set the result to the TextView
            calValue.text = "Rs. $formattedResult"
        }
    }
    }
