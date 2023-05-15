package com.example.job_aid

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class applyNowForm : AppCompatActivity() {

    private lateinit var etEmpName: EditText
    private lateinit var etEmpContact: EditText
    private lateinit var btnSave: Button
    private lateinit var btnUpload: Button
    private var selectedFileUri: Uri? = null


    companion object {
        private const val FILE_REQUEST_CODE = 101
    }


    private lateinit var dbRef:DatabaseReference

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
             selectedFileUri = data.data
            // Handle the selected file URI
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_now_form)

        etEmpName = findViewById(R.id.etEmpName)
        etEmpContact = findViewById(R.id.etEmpContact)
        btnSave = findViewById(R.id.btnSave)
        btnUpload = findViewById(R.id.btnUpload)


        dbRef = FirebaseDatabase.getInstance().getReference("Applicants")

        btnUpload.setOnClickListener{
            val intent = Intent()
            intent.type = "*/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"),FILE_REQUEST_CODE )
        }

        btnSave.setOnClickListener{
            saveApplicantData()
        }


    }

    private fun saveApplicantData(){
        //getting values
        val applicantName = etEmpName.text.toString()
        val applicantContact = etEmpContact.text.toString()

        if(applicantName.isEmpty()) {
            etEmpName.error = "please enter name"
            return
            }
        if(applicantContact.isEmpty()) {
            etEmpContact.error = "please enter Contact Number"
            return
        }

        val applicantId = dbRef.push().key!!

        val applicant = ApplicantModel(applicantId, applicantName , applicantContact)

        dbRef.child(applicantId).setValue(applicant)
            .addOnCompleteListener {task ->

                if (task.isSuccessful) {
                    selectedFileUri?.let { fileUri ->
                        // Upload the file to Firebase Storage
                        val storageRef = FirebaseStorage.getInstance().reference
                        val fileRef =
                            storageRef.child("files/${applicantId}/${fileUri.lastPathSegment}")
                        val uploadTask = fileRef.putFile(fileUri)

                        uploadTask.addOnSuccessListener {
                            // File uploaded successfully
                            // Handle any further actions or notifications
                        }.addOnFailureListener { exception ->
                            // Error occurred during file upload
                            // Handle the error and display a toast message
                            Toast.makeText(
                                this,
                                "File upload failed: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    Toast.makeText(this, "Application submitted", Toast.LENGTH_LONG).show()

                    etEmpName.text.clear()
                    etEmpContact.text.clear()

                    finish()

                }
//                    .addOnFailureListener { err ->
//                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
//                }
            }
    }
}