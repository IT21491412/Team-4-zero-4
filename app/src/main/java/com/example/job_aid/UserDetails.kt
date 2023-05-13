package com.example.job_aid

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job_aid.databinding.ActivityUserDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserDetails : AppCompatActivity() {

    private lateinit var binding :ActivityUserDetailsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var storageReference: StorageReference
    private lateinit var imageUri:Uri
    private lateinit var database : DatabaseReference


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
        val uid = firebaseAuth.currentUser?.uid.toString()
        val name = firebaseAuth.currentUser?.displayName
        val email = firebaseAuth.currentUser?.email

        if(firebaseAuth.currentUser != null){
            firebaseAuth.currentUser?.let {

                binding.etusername.text = it.uid
                binding.etemail.text = it.email
            }
        }

        database = FirebaseDatabase.getInstance().getReference("UserProfData")
        if(uid.isNotEmpty()){
            getUserData()
        }



        binding.btnSave.setOnClickListener{

            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val city = binding.city.text.toString()
            val phoneNumber = binding.phonenumber.text.toString()
            val education = binding.education.text.toString()
            val skills = binding.skills.text.toString()
            val languages = binding.languages.text.toString()

//          val profData = mapOf<String,String>(
//                "firstName" to firstName,
//                "lastName" to lastName,
//                "city" to city,
//                "phoneNumber" to phoneNumber,
//                "education" to education,
//                "skills" to skills,
//                "languages" to languages
//           )

            if (uid != null){

                database = FirebaseDatabase.getInstance().getReference("UserProfData")
                val profData = UserProfData(
                    uid = uid,
                    name = name,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    city = city,
                    phoneNumber = phoneNumber,
                    education = education,
                    skills = skills,
                    languages = languages)


                database.child(uid).setValue(profData).addOnCompleteListener {

                    if (it.isSuccessful) {
                        //uploadProfilePic()

                        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, UserProfile::class.java)
                        startActivity(intent)


                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }




    }

    private fun getUserData() {
    }

    private fun uploadProfilePic(){

        imageUri  = Uri.parse("android.resource://$packageName/${R.drawable.profile}")
        storageReference = FirebaseStorage.getInstance().getReference("users/"+firebaseAuth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this,"Profile Successfully updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{

            Toast.makeText(this,"Failed to upload the image",Toast.LENGTH_SHORT).show()
        }
    }

}