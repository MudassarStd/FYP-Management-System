package com.android.cuifypmanagementsystem.admin.activities

import CustomDialogHelper.showActionConfirmationDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityFypDetailsBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_CHANGE_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_CHANGE_FYP_SECRETORY
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_OPEN_DETAILS_FOR_CLOSED_ACTIVITY
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FypDetailsActivity : AppCompatActivity() {
    private val binding : ActivityFypDetailsBinding by lazy {
        ActivityFypDetailsBinding.inflate(layoutInflater)
    }

    private val teacherViewModel : TeacherViewModel by viewModels()
    private val batchViewModel : BatchViewModel by viewModels()
    private val fypActivityViewModel: FypActivityViewModel by viewModels()


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

        val intentAction = intent.action
        if (intentAction == ACTION_OPEN_DETAILS_FOR_CLOSED_ACTIVITY) {
            hideAllEditOptions()
        }


        fypActivityRecord = intent.getSerializableExtra("fypActivityRecord") as? FypActivityRecord
        fypActivityRecord?.let { record ->

            Log.d("CheckingFypDetailsBug", "Record in details${fypActivityRecord}")
            // Use 'record' as a local, immutable reference
            val fypHeadId = record.fypHeadId.toString()
            val fypSecretoryId = record.fypSecId.toString()
            val batchId = record.batchId.toString()

            // Fetching data
            batchViewModel.getBatchById(batchId)
            teacherViewModel.getHeadSecretoryById(fypHeadId, fypSecretoryId)

            Log.d("CheckingFypDetailsBug", "after calling viewmodel")

            // Observing data results
            observeResults()

            // Setting UI elements
            binding.tvFypDetailsStartDate.text = record.registrationTimeStamp.toString()
            binding.tvFypDetailsActivityStatus.text = record.status.toString()
        } ?: Toast.makeText(this, "Null Exception: Error fetching activity details", Toast.LENGTH_LONG).show()



        binding.toolbarFypActivityDetails.setNavigationOnClickListener {
            onBackPressed()
        }

        // Handling edit UI updates
        binding.btnEditFypActivityDetails.setOnClickListener {
            showEditOptions()
        }

        binding.btnCancelEditingFypActivityDetails.setOnClickListener {
            hideEditOptions()
        }

        binding.btnCloseFypActivity.setOnClickListener { closeActivity() }
    }


    private fun observeResults() {
        batchViewModel.batchById.observe(this) { batch ->
            batch?.let {
                this.batch = batch
                Log.d("CheckingFypDetailsBug", "Batch Observer: ${this.batch}")

                checkAndUpdateUI()
            } ?: Log.d("CheckingFypDetailsBug", "Batch is Null $batch")

        }

        teacherViewModel.fypHeadSecretaryById.observe(this){result ->
            result?.let{
                fypHead = result.first
                fypSecretory = result.second
                Log.d("CheckingFypDetailsBug", "head sec Observer: ${fypHead} ${fypSecretory}")
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


    private fun showEditOptions() {
        binding.btnEditFypActivityDetails.visibility = View.GONE
        binding.btnCancelEditingFypActivityDetails.visibility = View.VISIBLE
        binding.btnFypDetailsChangeHead.visibility = View.VISIBLE
        binding.btnFypDetailsChangeSecretory.visibility = View.VISIBLE
    }

    private fun hideEditOptions() {
        binding.btnEditFypActivityDetails.visibility = View.VISIBLE
        binding.btnCancelEditingFypActivityDetails.visibility = View.GONE
        binding.btnFypDetailsChangeHead.visibility = View.GONE
        binding.btnFypDetailsChangeSecretory.visibility = View.GONE
    }

    private fun hideAllEditOptions() {
        binding.btnEditFypActivityDetails.visibility = View.GONE
        binding.btnCancelEditingFypActivityDetails.visibility = View.GONE
        binding.btnCloseFypActivity.visibility = View.GONE
        binding.btnDeleteFypActivity.visibility = View.GONE
    }



    private fun closeActivity() {
        showActionConfirmationDialog(this,
            "Are you to close this activity? \nNote: This action is critical and cannot be undone.", onProceed = {
                fypActivityRecord?.firestoreId?.let { fypActivityViewModel.closeActivity(it) }
                fypActivityRecord?.let {
                    teacherViewModel.freeHeadSecretoryOnActivityClosure(it.fypHeadId!!, it.fypSecId!!)
                    batchViewModel.updateBatchOnActivityClosure(it.batchId!!)
                }
                observeActivityClosureResults()
            })
    }

    private fun observeActivityClosureResults() {
        val combinedResults = MediatorLiveData<Triple<Result<Void?>?, Result<Void?>?, Result<Void?>?>>().apply {

            var activityResult: Result<Void?>? = null
            var headSecretoryResult: Result<Void?>? = null
            var batchResult: Result<Void?>? = null


            showProgressDialog("Closing activity...",this@FypDetailsActivity)

            addSource(fypActivityViewModel.activityClosureState) { result ->
                activityResult = result
                value = Triple(activityResult, headSecretoryResult, batchResult)
            }

            addSource(teacherViewModel.freeHeadSecretoryOnActivityClosureState) { result ->
                headSecretoryResult = result
                value = Triple(activityResult, headSecretoryResult, batchResult)
            }

            addSource(batchViewModel.batchUpdate) { result ->
                batchResult = result
                value = Triple(activityResult, headSecretoryResult, batchResult)
            }
        }

        combinedResults.observe(this) { (activityResult, headSecretoryResult, batchResult) ->
            if (activityResult != null && headSecretoryResult != null && batchResult != null) {
                when {
                    activityResult is Result.Success &&
                            headSecretoryResult is Result.Success &&
                            batchResult is Result.Success -> {
                                hideProgressDialog()
                                Toast.makeText(this, "Successfully closed activity", Toast.LENGTH_SHORT).show()
                    }

                    activityResult is Result.Failure &&
                            headSecretoryResult is Result.Failure &&
                            batchResult is Result.Failure -> {
                        hideProgressDialog()
                        Toast.makeText(this, "Fatal error, failed to close activity", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show()
                    }                    }
                    }

                }
    }

}