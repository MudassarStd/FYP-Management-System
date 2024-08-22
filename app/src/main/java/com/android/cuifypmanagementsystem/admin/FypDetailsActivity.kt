package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityFypDetailsBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_CHANGE_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_CHANGE_FYP_SECRETORY
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModelFactory
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModelFactory

class FypDetailsActivity : AppCompatActivity() {
    private val binding : ActivityFypDetailsBinding by lazy {
        ActivityFypDetailsBinding.inflate(layoutInflater)
    }

    private lateinit var teacherViewModel : TeacherViewModel
    private lateinit var batchViewModel : BatchViewModel

    private  var fypActivityRecord : FypActivityRecord? = null
    private  var fypHead : Teacher? = null
    private var fypSecretory : Teacher? = null
    private  var batch : Batch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = Color.parseColor("#576AE0")

        initializeViewModels()


        fypActivityRecord = intent.getSerializableExtra("fypActivityRecord") as? FypActivityRecord
        fypActivityRecord?.let { record ->
            // Use 'record' as a local, immutable reference
            val fypHeadId = record.fypHeadId.toString()
            val fypSecretoryId = record.fypSecId.toString()
            val batchId = record.batchId.toString()

            // Fetching data
            batchViewModel.getBatchById(batchId)
            teacherViewModel.getHeadSecretoryById(fypHeadId, fypSecretoryId)

            // Observing data results
            observeResults()

            // Setting UI elements
            binding.tvFypDetailsStartDate.text = record.registrationTimeStamp.toString()
            binding.tvFypDetailsActivityStatus.text = record.status.toString()
        } ?: Toast.makeText(this, "Null Exception: Error fetching activity details", Toast.LENGTH_LONG).show()



        binding.toolbarFypActivityDetails.setNavigationOnClickListener {
            onBackPressed()
        }
    }

//    private fun fetchDetails() {
//
//    }

    private fun observeResults() {
        batchViewModel.batchById.observe(this) { batch ->
            batch?.let {
                this.batch = batch
                checkAndUpdateUI()
            }
        }

        teacherViewModel.fypHeadSecretaryById.observe(this){result ->
            result?.let{
                fypHead = result.first
                fypSecretory = result.second
                checkAndUpdateUI()
            }
        }

    }

    private fun checkAndUpdateUI() {
        if (batch != null && fypHead != null && fypSecretory != null) {
            updateUI()
        }
    }

    private fun updateUI() {


        binding.tvFypDetailsHeadName.text = fypHead!!.name
        binding.tvFypDetailsSecretoryName.text = fypSecretory!!.name

        binding.tvFypDetailsBatchName.text = batch!!.name
        binding.tvFypDetailsSemester.text = batch!!.semester.toString()+"th"
        binding.tvFypDetailsActivityStudents.text = batch!!.registeredStudents.toString()
        binding.tvFypDetailsActivityGroups.text = batch!!.registeredGroups.toString()



        // -------- Action Change Head --------
        binding.btnFypDetailsChangeHead.setOnClickListener {
            val intent = Intent(this, ManageTeacherActivity::class.java ).apply {
                action = ACTION_CHANGE_FYP_HEAD
                putExtra("activityId", fypActivityRecord!!.firestoreId )
                putExtra("currentRoleHolderTeacherId", fypActivityRecord!!.fypHeadId)
            }
            startActivity(intent)
        }

        // -------- Action Change Secretory --------
        binding.btnFypDetailsChangeSecretory.setOnClickListener {
            val intent = Intent(this, ManageTeacherActivity::class.java ).apply {
                action = ACTION_CHANGE_FYP_SECRETORY
                putExtra("activityId", fypActivityRecord!!.firestoreId )
                putExtra("currentRoleHolderTeacherId", fypActivityRecord!!.fypSecId)
            }
            startActivity(intent)
        }
    }

    private fun initializeViewModels() {

        val teacherRepository = (application as BaseApplication).teacherRepository
        teacherViewModel = ViewModelProvider(this, TeacherViewModelFactory(teacherRepository))[TeacherViewModel::class.java]

        val batchRepository = (application as BaseApplication).batchRepository
        batchViewModel = ViewModelProvider(this, BatchViewModelFactory(batchRepository))[BatchViewModel::class.java]

    }
}