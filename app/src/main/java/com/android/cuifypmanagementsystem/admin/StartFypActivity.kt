package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityStartFypActivityyBinding
import com.android.cuifypmanagementsystem.datamodels.BatchInfo
import com.android.cuifypmanagementsystem.datamodels.FypActivityRole
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.datamodels.TeacherNameAndId
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_ACTIVITY_BATCH
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_SECRETORY
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModelFactory
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModelFactory
import com.android.cuifypmanagementsystem.viewmodel.H_S_SelectionViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModelFactory

class StartFypActivity : AppCompatActivity() {
    private val binding: ActivityStartFypActivityyBinding by lazy {
        ActivityStartFypActivityyBinding.inflate(layoutInflater)
    }
    private lateinit var selectionViewModel : H_S_SelectionViewModel
    private lateinit var fypActivityViewModel : FypActivityViewModel
    private lateinit var batchViewModel: BatchViewModel
    private lateinit var teacherViewModel : TeacherViewModel


    private var fypHeadId : String? = null
    private var fypSecretoryId : String? = null
    private var batchId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val teacherRepository = (application as BaseApplication).teacherRepository
        teacherViewModel = ViewModelProvider(this, TeacherViewModelFactory(teacherRepository))[TeacherViewModel::class.java]


        // Initialize ViewModel with factory
        selectionViewModel = (application as BaseApplication).getH_S_SelectionViewModel()

        val fypActivityRepository = (application as BaseApplication).fypActivityRepository
        fypActivityViewModel = ViewModelProvider(this, FypActivityViewModelFactory(fypActivityRepository))[FypActivityViewModel::class.java]

        val batchRepository = (application as BaseApplication).batchRepository
        batchViewModel = ViewModelProvider(this, BatchViewModelFactory(batchRepository))[BatchViewModel::class.java]



        setFieldSelectionObservers()
        displayResult()


        // starting activity

        binding.btnStartActivity.setOnClickListener {
            val fypActivityRecord = getFypActivityRecord()
            if (fypHeadId != fypSecretoryId)
            {
                fypActivityRecord?.let {
                    fypActivityViewModel.startFypActivity(fypActivityRecord)
                } ?: Toast.makeText(this,"Invalid Data", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Same person cannot be selected as head & secretory", Toast.LENGTH_SHORT).show()
            }

            observeResults()
        }
    }



    private fun setFieldSelectionObservers(){
        selectionViewModel.selectedHead.observe(this){
            it?.let{
                binding.etFypHead.setText(it.name)
                fypHeadId = it.firestoreId
            }

        }

        selectionViewModel.selectedSecretory.observe(this){
            it?.let{
                binding.etFypSecretory.setText(it.name)
                fypSecretoryId = it.firestoreId
            }
        }

        selectionViewModel.selectedBatch.observe(this){
            it?.let {
                binding.etFypActivityBatch.setText(it.name)
                batchId = it.firestoreId
            }
        }
    }


    // observing results of start activity cloud operations

    private fun observeResults() {
        fypActivityViewModel.fypActivityStartStatus.observe(this){
            when(it){
                is Result.Success -> {
                    hideProgressDialog()

                    val activityId = it.data
                    showProgressDialog("Updating teachers' roles, please wait...", this)

                    val fypHeadRole = FypActivityRole(activityId, "Head")
                    val fypSecretoryRole = FypActivityRole(activityId, "Secretory")

                    teacherViewModel.updateTeacherRoles(fypHeadId!!, fypHeadRole, fypSecretoryId!!, fypSecretoryRole)
                    batchViewModel.updateBatchActivityStatus(batchId!!, true)

                    teacherViewModel.teacherRoleUpdateStatusForActivity.observe(this){
                        when(it) {
                            is Result.Success -> {
                                resetFields()
                                hideProgressDialog()
                                Toast.makeText(this, "Activity started successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, AllFypActivity::class.java))
                                finish()
                            }
                            is Result.Failure ->{
                                hideProgressDialog()
                                showRetryDialog(fypHeadId!!, fypHeadRole, fypSecretoryId!!, fypSecretoryRole)
//                                val errorMessage = it.exception.message ?: "An unknown error occurred"
//                                Toast.makeText(this, "Activity Could not start: $errorMessage", Toast.LENGTH_SHORT).show()
                            }

                            Result.Loading -> {
                                Log.d("fjksd","updating teacher roles ")
                            }
                        }
                    }


                }
                is Result.Failure -> {
                    hideProgressDialog()
                    val errorMessage = it.exception.message ?: "An unknown error occurred"
                    Toast.makeText(this, "Activity Could not start: $errorMessage", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressDialog("Starting Activity, please wait..", this)
                }

            }
        }
    }

    private fun resetFields() {
        selectionViewModel.setSelectedHead(null)
        selectionViewModel.setSelectedSecretory(null)
        selectionViewModel.setSelectedBatch(null)
    }

    private fun getFypActivityRecord() : FypActivityRecord?{
        val fypHeadName = binding.etFypHead.text.toString()
        val fypSecretoryName = binding.etFypSecretory.text.toString()
        val batchName = binding.etFypActivityBatch.text.toString()
        return if(fypHeadName.isNotEmpty() && fypSecretoryName.isNotEmpty() && batchName.isNotEmpty())
        {
            FypActivityRecord(null,fypHeadId!!, fypSecretoryId!!, batchId!!, true, System.currentTimeMillis())
        }
        else{
            null
        }
    }

    fun displayResult(){
        val fypHead = binding.etFypHead
        val fypSec = binding.etFypSecretory
        val activityBatch = binding.etFypActivityBatch

        fypHead.setOnClickListener {
            val intent = Intent(this, ManageTeacherActivity::class.java).also {
                it.action = ACTION_SELECT_FYP_HEAD
            }
            startActivity(intent)
        }

        fypSec.setOnClickListener {
            val intent = Intent(this, ManageTeacherActivity::class.java).also {
                it.action = ACTION_SELECT_FYP_SECRETORY
            }
            startActivity(intent)
        }

        activityBatch.setOnClickListener {
            val intent = Intent(this, BatchActivity::class.java).also {
                it.action = ACTION_SELECT_FYP_ACTIVITY_BATCH
            }
            startActivity(intent)
        }

    }


    private fun showRetryDialog(fypHeadId: String, fypHeadRole: FypActivityRole, fypSecretoryId: String, fypSecretoryRole: FypActivityRole) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Update Failed")
            .setMessage("Failed to update teacher roles. Do you want to retry?")
            .setPositiveButton("Retry") { _, _ ->
                showProgressDialog("Updating Teachers' Roles, please wait...", this)
                teacherViewModel.updateTeacherRoles(fypHeadId, fypHeadRole, fypSecretoryId, fypSecretoryRole)
            }
            .setNegativeButton("Cancel") { _, _ ->
                fypActivityViewModel.deleteFypActivity(fypHeadRole.activityId)
            }
            .create()

        alertDialog.setOnDismissListener {
            fypActivityViewModel.deleteFypActivity(fypHeadRole.activityId)
        }

        alertDialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        resetFields()
    }

}