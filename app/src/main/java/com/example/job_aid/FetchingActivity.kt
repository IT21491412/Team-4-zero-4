package com.example.job_aid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class FetchingActivity : AppCompatActivity() {

    private lateinit var comRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var comList: ArrayList<CompanyModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        comRecyclerView = findViewById(R.id.rvCom)
        comRecyclerView.layoutManager = LinearLayoutManager(this)
        comRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        comList = arrayListOf<CompanyModel>()

        getEmployeesData()

    }

    private fun getEmployeesData() {

        comRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Company")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comList.clear()
                if (snapshot.exists()){
                    for (ComSnap in snapshot.children){
                        val ComData = ComSnap.getValue(CompanyModel::class.java)
                        comList.add(ComData!!)
                    }
                    val mAdapter = ComAdapter(comList)
                    comRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ComAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, CompanyDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("ComId", comList[position].ComId)
                            intent.putExtra("ComName", comList[position].ComName)
                            intent.putExtra("Address", comList[position].Address)
                            intent.putExtra("Phone", comList[position].Phone)
                            startActivity(intent)
                        }

                    })

                    comRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}