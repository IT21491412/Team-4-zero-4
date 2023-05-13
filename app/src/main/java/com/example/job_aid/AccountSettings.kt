package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job_aid.databinding.ActivityAccountSettingsBinding
import com.example.job_aid.databinding.ActivityUserDetailsBinding
import com.example.job_aid.databinding.ActivityUserProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


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

        if(firebaseAuth.currentUser != null){
            firebaseAuth.currentUser?.let {

               // binding.etusername.text = it.uid
                binding.etemail.text = it.email
            }
        }

        binding.btnSave.setOnClickListener{
            val user = firebaseAuth.currentUser
            val email = binding.editEmail.text.toString()

            if(email.isNotEmpty()){
                user?.updateEmail(email)?.addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Email updated successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
                    }

                }

            }


        }



    }

//    private fun checkEmailField():Boolean{
//        val email = binding.editEmail.text.toString()
//        if(binding.editEmail.text.toString() == ""){
//            binding.textInputLayoutEmail.error = "This is required field"
//        }
//    }
}