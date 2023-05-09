package com.example.job_aid

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job_aid.databinding.ActivityHomeBinding
import com.example.job_aid.databinding.ActivityUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if(firebaseAuth.currentUser != null){
            firebaseAuth.currentUser?.let {

                //binding.etusername.text = it.uid
                binding.etemail.text = it.email
            }
        }


        // navigations to pages

        binding.accountSettings.setOnClickListener{
            val intent = Intent(this, AccountSettings::class.java)
            startActivity(intent)
        }

        binding.userDetails.setOnClickListener{
            val intent = Intent(this, UserDetails::class.java)
            startActivity(intent)
        }
    }
}