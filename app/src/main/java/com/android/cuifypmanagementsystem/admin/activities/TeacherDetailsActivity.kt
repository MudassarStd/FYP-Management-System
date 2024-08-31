package com.android.cuifypmanagementsystem.admin.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityTeacherDetailsBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.utils.DateTime.longToDate
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TeacherDetailsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTeacherDetailsBinding.inflate(layoutInflater) }
    private val fypActivityViewModel: FypActivityViewModel by viewModels()
    private val batchViewModel: BatchViewModel by viewModels()
    private var fypActivityInfo: FypActivityRecord? = null
    private var batchInfo: Batch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupWindowInsets()
        window.statusBarColor = Color.parseColor("#576AE0")

        binding.toolbalTeacherDetails.setNavigationOnClickListener {
            onBackPressed()
        }

        handleIntentData()
        observeFypActivityInfoResult()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleIntentData() {
        val name = intent.getStringExtra("name")
        val department = intent.getStringExtra("department")
        val isSupervisor = intent.getIntExtra("isSupervisor", 0)
        val registrationTimestamp = intent.getLongExtra("registrationTimestamp", 0)
        val isFypHeadOrSecretory = intent.getIntExtra("isFypHeadOrSecretory", 0)
        val activityRole = intent.getStringExtra("activityRole")
        val activityId = intent.getStringExtra("activityId")

        activityId?.let {
            fypActivityViewModel.getActivityInfo(it)
        }

        updateUI(name, department, isSupervisor, isFypHeadOrSecretory, activityRole, registrationTimestamp)
    }

    private fun observeFypActivityInfoResult() {
        fypActivityViewModel.fypActivityInfo.observe(this) { fypActivity ->
            fypActivity?.let {
                fypActivityInfo = it
                batchViewModel.getBatchById(fypActivityInfo!!.batchId!!)
                observeBatchInfoResult()

            } ?: showToast("Failed to get activity details")
        }
    }

    private fun observeBatchInfoResult() {
        batchViewModel.batchById.observe(this){ batch ->
            batch?.let{
                batchInfo = batch
            } ?: showToast("Failed to get batch details")
            updateFypActivityInfoUI()
        }
    }

    private fun updateUI(
        name: String?,
        department: String?,
        isSupervisor: Int,
        isFypHeadOrSecretary: Int,
        activityRole: String?,
        registrationTimestamp: Long
    ) {
        // converting timestamp into date

        binding.tvTeacherName.text = name
        binding.tvDepartment.text = department
        binding.tvRegistrationDate.text = longToDate(registrationTimestamp)

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
        binding.tvFypActivityName.text = batchInfo!!.name
        binding.tvFypActivityDescription.text = fypActivityInfo!!.batchId
    }

    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
