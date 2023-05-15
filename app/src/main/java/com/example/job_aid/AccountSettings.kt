package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.job_aid.databinding.ActivityAccountSettingsBinding
import com.example.job_aid.databinding.ActivityUserDetailsBinding
import com.example.job_aid.databinding.ActivityUserProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AccountSettings : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase

    private lateinit var binding:ActivityAccountSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
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
                    Toast.makeText(this@AccountSettings, "Failed to fetch data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // edit email

        binding.btnSave.setOnClickListener{
            val user = firebaseAuth.currentUser
            val email = binding.editEmail.text.toString()

            if (email.isNotEmpty()) {
                // Display a confirmation dialog
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Are you sure you want to update your email?")
                    .setCancelable(false)
                    .setPositiveButton("Update") { dialog, id ->
                        // Update the user's email
                        user?.updateEmail(email)?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, id ->
                        dialog.cancel()
                    }

                val alert = dialogBuilder.create()
                alert.setTitle("Update Email")
                alert.show()
            }
        }


        //Change Username

        binding.btnUsernamesave.setOnClickListener{

            val newUsername = binding.editUsername.text.toString()

            // Display a confirmation dialog
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Are you sure you want to update your username?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    // Update the user's username
                    val currentUser = firebaseAuth.currentUser
                    currentUser?.let { user ->
                        // Update the username in the Firebase database
                        database.reference.child("users").child(user.uid).child("name").setValue(newUsername)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Username updated successfully", Toast.LENGTH_SHORT).show()
                                    binding.etusername.text = newUsername // Update the displayed username
                                } else {
                                    Toast.makeText(this, "Failed to update username", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

            val alert = dialogBuilder.create()
            alert.setTitle("Update Username")
            alert.show()
        }



//Change Password

        binding.btnResetPassword.setOnClickListener{

        }




// Delete Account

        binding.btnDeleteAccount.setOnClickListener{

            val email = binding.deleteAccount.text.toString().trim()

            //Validate the email
            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the entered email matches the current user's email
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null && currentUser.email == email) {

                // Display a confirmation dialog
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Are you sure you want to delete your account?")
                    .setCancelable(false)
                    .setPositiveButton("Delete") { dialog, id ->
                        // Delete the user account
                        currentUser.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    .setNegativeButton("Cancel") { dialog, id -> dialog.cancel()
                    }

                val alert = dialogBuilder.create()
                alert.setTitle("Delete Account")
                alert.show()
            } else {
                Toast.makeText(this, "Entered email does not match the current user", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex()
        return email.matches(emailRegex)
    }


}