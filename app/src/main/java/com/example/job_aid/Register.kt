package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.job_aid.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()




        binding.alreadyAcc.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener{

            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()
            val userType = if(binding.radioJobSeeker.isChecked) "job_seeker" else "company"


            if (username.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if(pass == confirmPass){

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            val databaseRef = database.reference.child("users").child(firebaseAuth.currentUser!!.uid)
                            val users :Users = Users( username ,email, firebaseAuth.currentUser!!.uid,userType)

                            databaseRef.setValue(users).addOnCompleteListener{
                                if(it.isSuccessful){
                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)

                                }else{
                                    Toast.makeText(this,"Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                }

                            }


//                            val intent = Intent(this, Login::class.java)
//                            startActivity(intent)

                        }else{
                            Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

                        }
                    }

                }else{
                    Toast.makeText(this, "Password is not Matching", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Fill ALl Fields", Toast.LENGTH_SHORT).show()
            }
        }


    }
}