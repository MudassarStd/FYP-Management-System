package com.android.cuifypmanagementsystem.admin.activities

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.admin.adapters.recyclerview.FypActivityAdapter
import com.android.cuifypmanagementsystem.admin.adapters.pagers.FypActivityPagerAdapter
import com.android.cuifypmanagementsystem.databinding.ActivityAllFypBinding
import com.android.cuifypmanagementsystem.admin.viewmodel.FypActivityViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllFypActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllFypBinding
    private val fypActivityViewModel: FypActivityViewModel by viewModels()

    private val activityAdapter: FypActivityAdapter by lazy {
        FypActivityAdapter(this, emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllFypBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = Color.parseColor("#576AE0")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pagerAdapter = FypActivityPagerAdapter(this)
        binding.viewPagerAllFypActivity.adapter = pagerAdapter

        // Linking tabLayout with viewPager
        TabLayoutMediator(
            binding.tabLayoutAllFypActivity,
            binding.viewPagerAllFypActivity
        ) { tab, pos ->
            tab.text = when (pos) {
                0 -> "Active"
                1 -> "Closed"
                else -> ""
            }
        }.attach()


    }

//        fetchActivities()
//
//        setUpRecyclerView()

//        binding.fabAddActivity.setOnClickListener{
//            startActivity(Intent(this, StartFypActivity::class.java))
//            finish()
//        }
    }

//    private fun fetchActivities() {
//
//        fypActivityViewModel.fypActivitiesFetch.observe(this){
//            when(it){
//                is Result.Success -> {
//                    val data = it.data
//                    hideProgressDialog()
//
//                    val headSecretoryIds = data.flatMap { listOf(it.fypHeadId, it.fypSecId) }
//
//
//
//                    activityAdapter.updateActivitiesData(data)
//                    toggleFab(data.size < 3)
//                }
//                is Result.Failure -> {
//                    hideProgressDialog()
//                    val errorMessage = it.exception.message ?: "An unknown error occurred"
//                    Toast.makeText(this, "Loading data failed: $errorMessage", Toast.LENGTH_SHORT).show()
//                    Log.d("fsdlfjsaoifhsadofhisd", errorMessage)
//                }
//                is Result.Loading -> {
//                    showProgressDialog("Loading Activities, please wait..", this)
//                }
//
//            }
//        }
//    }

//    fun toggleFab(showFab: Boolean) {
//        binding.fabAddActivity.visibility = if (showFab) View.VISIBLE else View.GONE
//    }

//    private fun setUpRecyclerView() {
//        recyclerview = binding.rvFypActivity
//        recyclerview.adapter = activityAdapter
//        recyclerview.layoutManager = LinearLayoutManager(this)
//    }
