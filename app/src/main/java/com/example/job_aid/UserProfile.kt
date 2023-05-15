package com.example.job_aid

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job_aid.databinding.ActivityHomeBinding
import com.example.job_aid.databinding.ActivityUserProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

        //fetch user email and username

        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userRef = database.reference.child("users").child(user.uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.child("name").value as String
                    val email = dataSnapshot.child("email").value as String

                    // Update the text area with the fetched data
                    binding.etusername.text = "$username"
                    binding.etemail.text = "$email"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors that occur during data retrieval
                    Toast.makeText(this@UserProfile, "Failed to fetch data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
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

        binding.signoutsection.setOnClickListener{

            firebaseAuth.signOut()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()

        }
    }
}