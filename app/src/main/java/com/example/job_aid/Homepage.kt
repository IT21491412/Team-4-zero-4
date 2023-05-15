package com.example.job_aid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.example.job_aid.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList


class Homepage : AppCompatActivity() {

    private lateinit var vacncyRecyclerView: RecyclerView

    //    private lateinit var tvLoadingData: TextView
    private lateinit var vacList: ArrayList<VacancyModel>
    private lateinit var dbRef: DatabaseReference
//    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: VacAdapter
    private lateinit var newArrayList : ArrayList<VacancyModel>
    private lateinit var tempArrayList : ArrayList<VacancyModel>



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        super.onCreate(savedInstanceState)




//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        setContentView(R.layout.activity_homepage)


        //      Bottom  Navigation bar Starts

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val menuItem = menu.findItem(R.id.navigation_home)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
//                    startActivity(Intent(this, Homepage::class.java))
                    true
                }
                R.id.navigation_bookmarks -> {
                    startActivity(Intent(this, bookmarks::class.java))
                    true
                }
                R.id.navigation_tips -> {
                    startActivity(Intent(this, userViewTips::class.java))
                    true
                }
                R.id.navigation_profile -> {

//                     startActivity(Intent(this, UserProfile::class.java))
                    true
                }
                else -> false
            }
        }

//        bottom Navigation bar ends

        vacncyRecyclerView = findViewById(com.example.job_aid.R.id.recyclerView)
        vacncyRecyclerView.layoutManager = LinearLayoutManager(this)
        vacncyRecyclerView.setHasFixedSize(true)

        vacList = arrayListOf<VacancyModel>()

        getVacanciesData()

//        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//            override fun onQueryTextChange(newText: String): Boolean {
//                searchList(newText)
//                return true
//            }
//        })

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
                            val intent = Intent(this@Homepage, userViewVacancy::class.java)

                            //put extras
                            intent.putExtra("vid", vacList[position].vid)
                            intent.putExtra("jbRl", vacList[position].jbRl)
                            intent.putExtra("jobDes", vacList[position].jobDes)
                            intent.putExtra("comOver", vacList[position].comOver)
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

//    fun searchList(text: String) {
//        val searchList = java.util.ArrayList<VacancyModel>()
//        for (dataClass in vacList) {
//            if (dataClass.jbRl?.lowercase()
//                    ?.contains(text.lowercase(Locale.getDefault())) == true
//            ) {
//                searchList.add(dataClass)
//            }
//        }
//        adapter.searchDataList(searchList)
//    }

}