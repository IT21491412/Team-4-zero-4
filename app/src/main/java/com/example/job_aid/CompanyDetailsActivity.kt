package com.example.job_aid


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class CompanyDetailsActivity(val ComId: String) : AppCompatActivity() {

    private lateinit var tvComId: TextView
    private lateinit var tvComName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("ComId").toString(),
                intent.getStringExtra("ComName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("ComId").toString()
            )
        }

    }

    private fun initView() {
        tvComId = findViewById(R.id.tvEmpId)
        tvComName = findViewById(R.id.tvEmpName)
        tvAddress = findViewById(R.id.tvEmpAge)
        tvPhone = findViewById(R.id.tvEmpSalary)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvComId.text = intent.getStringExtra("ComId")
        tvComName.text = intent.getStringExtra("ComName")
        tvAddress.text = intent.getStringExtra("Address")
        tvPhone.text = intent.getStringExtra("Phone")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Company").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etComName = mDialogView.findViewById<EditText>(R.id.etComName)
        val etAddress = mDialogView.findViewById<EditText>(R.id.etAddress)
        val etPhone = mDialogView.findViewById<EditText>(R.id.etPhone)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etComName.setText(intent.getStringExtra("ComName").toString())
        etAddress.setText(intent.getStringExtra("Address").toString())
        etPhone.setText(intent.getStringExtra("Phone").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                ComId,
                etComName.text.toString(),
                etAddress.text.toString(),
                etPhone.text.toString()
            )

            Toast.makeText(applicationContext, "Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvComName.text = etComName.text.toString()
            tvAddress.text = etAddress.text.toString()
            tvPhone.text = etPhone.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        CompanyName: String,
        Address: String,
       Phone: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Company").child(id)
        val empInfo = CompanyModel(id, CompanyName, Address, Phone)
        dbRef.setValue(empInfo)
    }

}