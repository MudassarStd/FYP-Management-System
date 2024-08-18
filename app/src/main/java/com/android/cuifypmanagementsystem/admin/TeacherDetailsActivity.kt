package com.android.cuifypmanagementsystem.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityTeacherDetailsBinding
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModelFactory

class TeacherDetailsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTeacherDetailsBinding.inflate(layoutInflater) }
    private lateinit var fypActivityViewModel: FypActivityViewModel
    private var fypActivityInfo: FypActivityRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupWindowInsets()

        initializeViewModel()
        handleIntentData()
        observeFypActivityInfo()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViewModel() {
        val fypActivityRepository = (application as BaseApplication).fypActivityRepository
        fypActivityViewModel = ViewModelProvider(
            this,
            FypActivityViewModelFactory(fypActivityRepository)
        )[FypActivityViewModel::class.java]
    }

    private fun handleIntentData() {
        val name = intent.getStringExtra("name")
        val department = intent.getStringExtra("department")
        val isSupervisor = intent.getIntExtra("isSupervisor", 0)
        val isFypHeadOrSecretory = intent.getIntExtra("isFypHeadOrSecretory", 0)
        val activityRole = intent.getStringExtra("activityRole")
        val activityId = intent.getStringExtra("activityId")

        Log.d("TeacherDetailsActivity", "Activity ID: $activityId")

        activityId?.let {
            fypActivityViewModel.getActivityInfo(it)
        }

        updateUI(name, department, isSupervisor, isFypHeadOrSecretory, activityRole)
    }

    private fun observeFypActivityInfo() {
        fypActivityViewModel.fypActivityInfo.observe(this) { fypActivity ->
            fypActivity?.let {
                fypActivityInfo = it
                updateFypActivityInfoUI()
            }
            Log.d("TeacherDetailsActivity", "FYP Activity Info: $fypActivityInfo")
        }
    }

    private fun updateUI(
        name: String?,
        department: String?,
        isSupervisor: Int,
        isFypHeadOrSecretary: Int,
        activityRole: String?
    ) {
        binding.tvTeacherName.text = name
        binding.tvDepartment.text = department

        val hasRole = when {
            isFypHeadOrSecretary == 1 -> {
                binding.llFypHeadSecretorySection.visibility = View.VISIBLE
                binding.tvTitleSection.text = if (activityRole == "Head") "FYP Head" else "FYP Secretary"
                true
            }
            isSupervisor == 1 -> {
                binding.llSupervisorSection.visibility = View.VISIBLE
                true
            }
            else -> false
        }

        if (!hasRole) {
            binding.tvNoRoleAssigned.visibility = View.VISIBLE
        }
    }


    private fun updateFypActivityInfoUI() {
        binding.tvFypActivityName.text = fypActivityInfo?.batch?.name
        binding.tvFypActivityDescription.text = fypActivityInfo?.batch?.batchId
    }

//    private fun updateSupervisorInfoUI(){
//
//    }
}
