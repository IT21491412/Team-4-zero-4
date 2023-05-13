package com.example.job_aid

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job_aid.databinding.ActivityHomeBinding
import com.example.job_aid.databinding.ActivityUserProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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

                binding.etusername.text = it.uid
                binding.etemail.text = it.email
            }
        }

        //      Bottom  Navigation bar Starts

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val menuItem = menu.findItem(R.id.navigation_profile)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, Home2::class.java))
                    true
                }
                R.id.navigation_bookmarks -> {
                    startActivity(Intent(this, bookmarks::class.java))
                    true
                }
                R.id.navigation_tips -> {
                    startActivity(Intent(this, Application::class.java))
                    true
                }
                R.id.navigation_profile -> {

                    // startActivity(Intent(this, UserProfile::class.java))
                    true
                }
                else -> false
            }
        }

//        bottom Navigation bar ends


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