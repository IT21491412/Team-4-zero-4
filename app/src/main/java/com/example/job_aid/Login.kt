package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job_aid.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.alreadyAcc.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val currentUser = firebaseAuth.currentUser
                        if (currentUser != null) {
                            val userId = currentUser.uid
                            getUserType(userId)
                        }else{
                            Toast.makeText(this, "Failed To get user data", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Fill All Fields !!", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun getUserType(userId: String) {
        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        databaseRef.get().addOnSuccessListener { dataSnapshot ->
            val userType = dataSnapshot.child("userType").value.toString()



            if (userType == "job_seeker") {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()

            } else if (userType == "company") {
                val intent = Intent(this, VacancyFetching::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Invalid user type", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this,
                "Failed to get user information: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}