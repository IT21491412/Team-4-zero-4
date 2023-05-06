package com.example.job_aid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class adminViewTips : AppCompatActivity() {

    private lateinit var tipRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var tipList: ArrayList<TipsModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var addTipbtn : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_tips)

        tipRecyclerView = findViewById(R.id.rvTip)
        tipRecyclerView.layoutManager = LinearLayoutManager(this)
        tipRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        addTipbtn = findViewById(R.id.addTipbtn)

        addTipbtn.setOnClickListener {
            val intent = Intent(this, InsertTip::class.java)
            startActivity(intent)
        }

        tipList = arrayListOf<TipsModel>()

        getTipsData()


    }

    private fun getTipsData() {

        tipRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Tips")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tipList.clear()
                if (snapshot.exists()){
                    for (tipSnap in snapshot.children){
                        val tipData = tipSnap.getValue(TipsModel::class.java)
                        tipList.add(tipData!!)
                    }
                    val mAdapter = TipAdapter(tipList)
                    tipRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : TipAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@adminViewTips, TipDetails::class.java)

                            //put extras
                            intent.putExtra("tipId", tipList[position].tipId)
                            intent.putExtra("tipTitle", tipList[position].title)
                            intent.putExtra("tipDescription", tipList[position].description)
                            startActivity(intent)
                        }

                    })

                    tipRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}