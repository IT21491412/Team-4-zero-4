package com.example.job_aid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Application : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)
    }
}