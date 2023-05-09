package com.example.job_aid

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job_aid.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()



        binding.signOutButton.setOnClickListener{
            firebaseAuth.signOut()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnProfile.setOnClickListener{
            val intent = Intent(this,UserProfile::class.java)
            startActivity(intent)
        }

        binding.btnHome2.setOnClickListener{
            val intent = Intent(this,Home2::class.java)
            startActivity(intent)
        }

        binding.btnApplication.setOnClickListener{
            val intent = Intent(this,Application::class.java)
            startActivity(intent)
        }


    }


}