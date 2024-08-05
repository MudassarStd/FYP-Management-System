package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAddEditBatchBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.utils.BatchActivityExtras
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel

class AddEditBatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBatchBinding
    private val viewModel: BatchViewModel by lazy {
        ViewModelProvider(this)[BatchViewModel::class.java]
    }
    private var isEditing: Boolean = false
    private lateinit var editingBatch: Batch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets()

        viewModel.getAllBatches()

        isEditing = intent.hasExtra(BatchActivityExtras.EXTRA_ACTION_EDIT)

        if (isEditing) {
            editingBatch = intent.getSerializableExtra("batch") as Batch
            populateFields(editingBatch)
        }

        binding.btnUpdate.setOnClickListener { handleBatchUpdate() }
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
        batch?.let {
            if (isEditing) {
                updateBatch(it)
            } else {
                addBatch(it)
            }
        }
    }

    private fun updateBatch(batch: Batch) {
        if (viewModel.validateBatchForEditing(batch.name, batch.semester)) {
            viewModel.update(batch)
            showToast("Batch updated Successfully")
            finish()
            startActivity(Intent(this, BatchActivity::class.java))
        } else {
            showToast("Batch already exists")
        }
    }

    private fun addBatch(batch: Batch) {
        if (viewModel.validateBatch(batch.name, batch.semester)) {
            viewModel.insert(batch)
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
            if (isEditing) {
                Batch(editingBatch.id, batchName, semester, editingBatch.registeredStudents)
            } else {
                Batch(0, batchName, semester, 0)
            }
        } else {
            showToast("Invalid Information")
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
