package com.example.job_aid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job_aid.databinding.ActivityAccountSettingsBinding
import com.example.job_aid.databinding.ActivityUserDetailsBinding
import com.example.job_aid.databinding.ActivityUserProfileBinding
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