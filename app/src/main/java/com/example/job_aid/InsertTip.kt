package com.example.job_aid

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertTip : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAdd: Button

    private lateinit var dbRef: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_tip)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnAdd = findViewById(R.id.btnAdd)

        dbRef = FirebaseDatabase.getInstance().getReference("Tips")


        btnAdd.setOnClickListener {
            saveTipData()
        }

    }

    private fun saveTipData(){
        //get values
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()

        if(title.isEmpty()){
            etTitle.error = "Please Enter Title"
        }
        if(description.isEmpty()){
            etDescription.error = "Please Enter Description"
        }

        val tipId = dbRef.push().key!!

        val tip = TipsModel(tipId , title , description)

        dbRef.child(tipId).setValue(tip)
            .addOnCompleteListener{
                Toast.makeText(this , "Tip added successfully",Toast.LENGTH_LONG).show()

                etTitle.text.clear()
                etDescription.text.clear()

            }.addOnFailureListener{err ->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}