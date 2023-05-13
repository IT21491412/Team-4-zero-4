package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class  bookmarks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)


        val linearLayout = findViewById<LinearLayout>(R.id.tips_btn)
        linearLayout.setOnClickListener {
            // Navigate to the desired destination
            val intent = Intent(this, userViewTips::class.java)
            startActivity(intent)
        }

    }
}