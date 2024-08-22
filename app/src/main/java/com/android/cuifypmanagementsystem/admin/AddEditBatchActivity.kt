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
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAddEditBatchBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.utils.BatchActivityExtras
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_BATCH
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModelFactory

class AddEditBatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBatchBinding
    private lateinit var batchViewModel: BatchViewModel
    private var isEditing: Boolean = false
    private lateinit var editingBatch: Batch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets()

        val batchRepository = (application as BaseApplication).batchRepository
        batchViewModel = ViewModelProvider(this, BatchViewModelFactory(batchRepository))[BatchViewModel::class.java]
//        viewModel.getAllBatches()

        val intentAction = intent.action
        if(intentAction == ACTION_EDIT_BATCH) {
            isEditing = true

            editingBatch = intent.getSerializableExtra("batch") as Batch
            populateFields(editingBatch)
        }

        binding.btnUpdateBatch.setOnClickListener { handleBatchUpdate()
            Log.d("TestingBatchAddLogic", "btn clicked")
        }
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun populateFields(batch: Batch) {
        binding.etBatch.setText(batch.name)
        binding.etSemester.setText(batch.semester.toString())
    }

    private fun handleBatchUpdate() {
        val batch = getData()
        Log.d("TestingBatchAddLogic", "batch: ${batch}")
        batch?.let {
            if (isEditing) {
                updateBatch(it)
            } else {
                addBatch(it)
            }
        }
    }

    private fun updateBatch(batch: Batch) {
        if (batchViewModel.validateBatchForEditing(batch.name, batch.semester!!)) {
            batchViewModel.update(batch)
            showToast("Batch updated start")
//            startActivity(Intent(this, BatchActivity::class.java))
//            finish()
        } else {
            showToast("Batch already exists")
        }
    }

    private fun addBatch(batch: Batch) {
        if (batchViewModel.validateBatch(batch.name, batch.semester!!)) {
            batchViewModel.addBatch(batch)

            showToast("Batch Added Successfully")
            finish()
        } else {
            showToast("Batch already exists")
        }
    }

    private fun getData(): Batch? {
        val batchName = binding.etBatch.text.toString().uppercase()
        val semesterStr = binding.etSemester.text.toString()

        return if (batchName.isNotEmpty() && semesterStr.isNotEmpty()) {
            val semester = semesterStr.toInt()
            Batch(null, batchName, semester, 0, 0, false)
            } else {
                null
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
