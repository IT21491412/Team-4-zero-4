package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class TipDetails : AppCompatActivity() {


    private lateinit var tvTipId: TextView
    private lateinit var tvTipTitle: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("tipId").toString(),
                intent.getStringExtra("tipTitle").toString()
            )
        }

        btnDelete.setOnClickListener{
            deleteTip(
                intent.getStringExtra("tipId").toString()
            )
        }

    }

    private fun deleteTip(
        id:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Tips").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this , "Tip data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, adminViewTips::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener {error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvTipId = findViewById(R.id.tvEmpId)
        tvTipTitle = findViewById(R.id.tvEmpName)
        tvTipDescription = findViewById(R.id.tvEmpAge)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvTipId.text = intent.getStringExtra("tipId")
        tvTipTitle.text = intent.getStringExtra("tipTitle")
        tvTipDescription.text = intent.getStringExtra("tipDescription")

    }


    private fun openUpdateDialog(
        tipId: String,
        tipTitle: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_tip, null)

        mDialog.setView(mDialogView)

        val etTipTitle = mDialogView.findViewById<EditText>(R.id.etTipTitle)
        val etTipDescription = mDialogView.findViewById<EditText>(R.id.etTipDescription)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etTipTitle.setText(intent.getStringExtra("tipTitle").toString())
        etTipDescription.setText(intent.getStringExtra("tipDescription").toString())

        mDialog.setTitle("Updating $tipTitle Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateTipData(
                tipId,
                etTipTitle.text.toString(),
                etTipDescription.text.toString(),
            )

            Toast.makeText(applicationContext, "Tip Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvTipTitle.text = etTipTitle.text.toString()
            tvTipDescription.text = etTipDescription.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateTipData(
        id: String,
        title: String,
        description: String,
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Tips").child(id)
        val tipInfo = TipsModel(id, title, description)
        dbRef.setValue(tipInfo)
    }


}