package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.FypActivityAdapter
import com.android.cuifypmanagementsystem.databinding.ActivityAllFypBinding
import com.android.cuifypmanagementsystem.repository.FypActivityRepository
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class AllFypActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerview: RecyclerView
    private lateinit var binding: ActivityAllFypBinding
    private lateinit var fypActivityViewModel: FypActivityViewModel
    private val activityAdapter: FypActivityAdapter by lazy {
        FypActivityAdapter(this, emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllFypBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fypActivityRepository = (application as BaseApplication).fypActivityRepository
        fypActivityViewModel = ViewModelProvider(this, FypActivityViewModelFactory(fypActivityRepository))[FypActivityViewModel::class.java]


        fetchActivities()

        setUpRecyclerView()

        binding.fabAddActivity.setOnClickListener{
            startActivity(Intent(this, StartFypActivity::class.java))
        }
    }

    private fun fetchActivities() {

        fypActivityViewModel.fypActivitiesFetch.observe(this){
            when(it){
                is Result.Success -> {
                    hideProgressDialog()
                    activityAdapter.updateActivitiesData(it.data)
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    val errorMessage = it.exception.message ?: "An unknown error occurred"
                    Toast.makeText(this, "Loading data failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressDialog("Loading Activities, please wait..", this)
                }

            }
        }
    }

    private fun setUpRecyclerView() {
        recyclerview = binding.rvFypActivity
        recyclerview.adapter = activityAdapter
        recyclerview.layoutManager = LinearLayoutManager(this)
    }


}
