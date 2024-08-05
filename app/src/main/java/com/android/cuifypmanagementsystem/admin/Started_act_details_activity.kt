package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.ActivityAdapter
import com.android.cuifypmanagementsystem.databinding.ActivityStartedActDetailsBinding
import com.android.cuifypmanagementsystem.room.datamodels.startActivity
import com.google.firebase.firestore.FirebaseFirestore

class Started_act_details_activity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerview: RecyclerView
    private lateinit var binding: ActivityStartedActDetailsBinding
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartedActDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseFirestore.getInstance()
        recyclerview = binding.rvActivity
        recyclerview.layoutManager = LinearLayoutManager(this)

        fetchActivityData()

        val fabAddActivity = binding.fabAddActivity
        fabAddActivity.setOnClickListener {
            val intent = Intent(this, Start_FYP_Activityy::class.java)
            startActivity(intent)
        }
    }

    private fun fetchActivityData() {
        db.collection("Activities")
            .get()
            .addOnSuccessListener { result ->
                val activityList = mutableListOf<startActivity>()
                for (document in result) {
                    val startActivity = document.toObject(startActivity::class.java)
                    activityList.add(startActivity)
                }
                Log.d("data", activityList.toString())

                // Update the adapter with fetched data
                activityAdapter = ActivityAdapter(this, activityList)
                recyclerview.adapter = activityAdapter
            }
            .addOnFailureListener { exception ->
                Log.d("jamal", "Cannot fetch data: ${exception.message}")
            }
    }
}
