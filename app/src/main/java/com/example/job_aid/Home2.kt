package com.example.job_aid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Home2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)
    }
}