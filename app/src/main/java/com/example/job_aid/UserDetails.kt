package com.example.job_aid

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job_aid.databinding.ActivityUserDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserDetails : AppCompatActivity() {

    private lateinit var binding :ActivityUserDetailsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var storageReference: StorageReference
    private lateinit var imageUri:Uri
    private lateinit var database: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                    startActivity(Intent(this, UserProfile::class.java))
                    true
                }
                else -> false
            }
        }

//        bottom Navigation bar ends

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
                    Toast.makeText(
                        this@UserDetails,
                        "Failed to fetch data: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        binding.btnDetails.setOnClickListener {

            val firstName = binding.firstName.text.toString().trim()
            val lastName = binding.lastName.text.toString().trim()
            val address = binding.address.text.toString().trim()
            val city = binding.city.text.toString().trim()
            val phoneNumber = binding.phonenumber.text.toString().trim()
            val dob = binding.dob.text.toString().trim()
            val languages = binding.languages.text.toString().trim()

            // Validate the entered data
            if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() ||
                city.isEmpty() || phoneNumber.isEmpty() || dob.isEmpty() || languages.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val currentUser = firebaseAuth.currentUser
                currentUser?.let { user ->
                    val userRef = database.reference.child("UserProfData").child(user.uid)
                    val userProfData = UserProfData(
                        user.uid,
                        user.displayName,
                        user.email,
                        firstName,
                        lastName,
                        address,
                        city,
                        phoneNumber,
                        dob,
                        languages
                    )
                    userRef.setValue(userProfData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@UserDetails,
                                "User details updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this@UserDetails,
                                "Failed to update user details: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

        }
    }

    private fun displayUserProfileData(userProfData: UserProfData) {
//        binding.etusername.text = userProfData.name
//        binding.etemail.text = userProfData.email
        binding.firstName.setText(userProfData.firstName)
        binding.lastName.setText(userProfData.lastName)
        binding.address.setText(userProfData.address)
        binding.city.setText(userProfData.city)
        binding.phonenumber.setText(userProfData.phoneNumber)
        binding.dob.setText(userProfData.dob)
        binding.languages.setText(userProfData.languages)
    }

}