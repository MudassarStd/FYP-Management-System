package com.android.cuifypmanagementsystem.admin.activities

import CustomDialogHelper.showActionConfirmationDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAddEditBatchBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_BATCH
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.RegistrationUtils.getSemesterNumber
import com.android.cuifypmanagementsystem.utils.RegistrationUtils.validateRegistrationBatch
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AddEditBatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBatchBinding
    private  val batchViewModel: BatchViewModel by viewModels()
    private var isEditing: Boolean = false
    private lateinit var editingBatchData: Batch

    // State messages
    private lateinit var onSuccess: String
    private lateinit var onFailure: String
    private lateinit var onLoading: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        initializeViewModel()
        handleIntent()
        setupListeners()
    }

    private fun setupUI() {
        // Set up status bar color
        window.statusBarColor = Color.parseColor("#576AE0")

        // Configure insets for edge-to-edge display
        setupInsets()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViewModel() {
//        val batchRepository = (application as BaseApplication).batchRepository
//        batchViewModel = ViewModelProvider(this, BatchViewModelFactory(batchRepository)).get(BatchViewModel::class.java)


    }

    private fun handleIntent() {
        val intentAction = intent.action
        if (intentAction == ACTION_EDIT_BATCH) {
            isEditing = true
            binding.btnDeleteToolbarEditBatch.visibility = View.VISIBLE
            editingBatchData = intent.getSerializableExtra("batch") as Batch
            populateFields(editingBatchData)
        }
    }

    private fun setupListeners() {
        binding.toolbarAddEditBatchActivity.setNavigationOnClickListener { onBackPressed() }

        // Listen for changes in the batch name input field
        binding.etBatch.doOnTextChanged { text, _, _, _ ->
            if (!isEditing && text != null && text.length == 4) {
                validateBatchAndSetSemesterNumber(text.toString())
            }
        }

        binding.btnUpdateBatch.setOnClickListener { handleBatchUpdate() }
        binding.btnDeleteToolbarEditBatch.setOnClickListener { deleteBatch() }
    }

    private fun populateFields(batch: Batch) {
        binding.etBatch.setText(batch.name)
        binding.etSemester.setText(batch.semester.toString())

        // Update validation listener for editing mode
        binding.etBatch.doOnTextChanged { text, _, _, _ ->
            if (isEditing && text != null && text.length == 4) {
                validateBatchAndSetSemesterNumber(text.toString())
            }
        }
    }

    private fun validateBatchAndSetSemesterNumber(batchName: String) {
        if (validateRegistrationBatch(batchName)) {
            val semesterNo = getSemesterNumber(batchName)
            binding.etSemester.setText(semesterNo.toString())
        } else {
            showBatchNameError()
        }
    }

    private fun handleBatchUpdate() {
        val batch = getData()
        batch?.let {
            if (validateRegistrationBatch(batch.name)) {
                if (isEditing) {
                    updateBatch(it)
                } else {
                    addBatch(it)
                }
            } else {
               showBatchNameError()
            }
        }
    }

    private fun updateBatch(batch: Batch) {
        if (batchViewModel.validateBatchForEditing(batch.name, batch.semester!!)) {
            batchViewModel.update(batch)
            setStateMessages("Batch updated Successfully", "Batch could not be updated", "Updating batch...")
            observeBatchUpdateResults()
        } else {
            showToast("Batch already exists")
        }
    }

    private fun addBatch(batch: Batch) {
        if (batchViewModel.validateBatch(batch.name, batch.semester!!)) {
            Log.d("TestingAddBatch", "2: addBatch() in ViewModel ")
            batchViewModel.addBatch(batch)
            setStateMessages("Batch added Successfully", "Batch could not be added", "Adding batch...")
            observeBatchUpdateResults()
        } else {
            showToast("Batch already exists")
        }
    }

    private fun deleteBatch() {
        showActionConfirmationDialog(this, "Are you sure you want to delete this batch?", onProceed = {
            batchViewModel.deleteBatch(editingBatchData.firestoreId!!)
            setStateMessages("Batch deleted Successfully", "Batch could not be deleted", "Deleting batch...")
            observeBatchUpdateResults()
        })
    }

    private fun getData(): Batch? {
        val batchName = binding.etBatch.text.toString().uppercase()
        val semesterStr = binding.etSemester.text.toString()

        return if (batchName.isNotEmpty() && semesterStr.isNotEmpty()) {
            val semester = semesterStr.toInt()
            val batchId = if(isEditing) {
                editingBatchData.firestoreId
            } else {
                null
            }
            Batch(batchId, batchName, semester, 0, 0, false)
        } else {
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setStateMessages(success: String, failure: String, loading: String) {
        onSuccess = success
        onFailure = failure
        onLoading = loading
    }

    private fun showBatchNameError() {
        binding.etBatch.error = "Invalid batch name. Format should be like 'fa21' or 'sp22'."
    }

    private fun observeBatchUpdateResults() {
        batchViewModel.batchUpdate.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    showToast(onSuccess)
                    finish()
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    showToast(onFailure)
                }
                is Result.Loading -> {
                    showProgressDialog(onLoading, this)
                }
            }
        }
    }
}
