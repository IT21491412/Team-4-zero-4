package com.example.job_aid

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job_aid.databinding.ActivityAddedVacanciesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R

class VacancyFetching: AppCompatActivity() {

    private lateinit var vacncyRecyclerView: RecyclerView
    private lateinit var binding : ActivityAddedVacanciesBinding

    //    private lateinit var tvLoadingData: TextView
    private lateinit var vacList: ArrayList<VacancyModel>
    private lateinit var dbRef: DatabaseReference

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        binding = ActivityAddedVacanciesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        vacncyRecyclerView = findViewById(com.example.job_aid.R.id.rvVacancy)
        vacncyRecyclerView.layoutManager = LinearLayoutManager(this)
        vacncyRecyclerView.setHasFixedSize(true)
//        tvLoadingData = findViewById(R.id.tvLoadingData)

        vacList = arrayListOf<VacancyModel>()


        binding.logoutbutton.setOnClickListener{

            //dsfirebaseAuth.signOut()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            //finish()
        }

        getVacanciesData()


        //      Bottom  Navigation bar Starts

        val bottomNavigationView = findViewById<BottomNavigationView>(com.example.job_aid.R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val menuItem = menu.findItem(com.example.job_aid.R.id.company_navigation_home)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                com.example.job_aid.R.id.company_navigation_home -> {
                    startActivity(Intent(this, VacancyFetching::class.java))
                    true
                }
                com.example.job_aid.R.id.navigation_vacancy -> {
                     startActivity(Intent(this, VacancyAdd ::class.java))
                    true
                }

                com.example.job_aid.R.id.navigation_profile -> {

                    //  startActivity(Intent(this, ::class.java))
                    true
                }
                else -> false
            }
        }

//        bottom Navigation bar ends

    }

    private fun getVacanciesData() {

        vacncyRecyclerView.visibility = View.GONE
//        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Vacancies")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                vacList.clear()
                if (snapshot.exists()){
                    for (vacSnap in snapshot.children){
                        val vacData = vacSnap.getValue(VacancyModel::class.java)
                        vacList.add(vacData!!)
                    }
                    val mAdapter = VacAdapter(vacList)
                    vacncyRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : VacAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@VacancyFetching, VacancyDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("vid", vacList[position].vid)
                            intent.putExtra("jbRl", vacList[position].jbRl)
                            intent.putExtra("jobDes", vacList[position].jobDes)
                            intent.putExtra("comOver", vacList[position].comOver)
                            intent.putExtra("add", vacList[position].add)
                            startActivity(intent)

                        }


                    })

                    vacncyRecyclerView.visibility = View.VISIBLE
//                    tvLoadingData.visibility = View.GONE
                }
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        }

    }
