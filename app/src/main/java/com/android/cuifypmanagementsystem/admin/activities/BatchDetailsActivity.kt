package com.android.cuifypmanagementsystem.admin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityBatchDetailsBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.utils.BatchActivityExtras
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel

class BatchDetailsActivity : AppCompatActivity() {
    private val binding : ActivityBatchDetailsBinding by lazy {
        ActivityBatchDetailsBinding.inflate(layoutInflater)
    }
    private val batchViewModel : BatchViewModel by lazy {
        ViewModelProvider(this)[BatchViewModel::class.java]
    }
    private lateinit var batch : Batch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // setting up batch observer
        observeBatchData()
        getBatchFromIntent()


        // handling Buttons

        binding.btnDeleteBatch.setOnClickListener {
            showDialog(batch)
        }

        binding.btnEditBatch.setOnClickListener {
            val i = Intent(this, AddEditBatchActivity::class.java).apply {
                putExtra(BatchActivityExtras.EXTRA_ACTION_EDIT, true)
                putExtra("batch", batch)
            }
            startActivity(i)
        }

        binding.btnStartFYPActivity.setOnClickListener{
            Toast.makeText(this, "Waiting for Teachers List", Toast.LENGTH_SHORT).show()
        }

        binding.cvBatch.setOnClickListener{
            toggleVisibility(binding.ll1, binding.ll2, binding.ll3)
        }
    }



    private fun observeBatchData() {
       batchViewModel.batch.observe(this){
           binding.tvBatchName.text = it.name
           binding.tvBatchSemester.text = it.semester.toString()+"th Semester"
           binding.cvBatchTitle.text = it.name
           binding.tvStudentsRegistered.text = it.registeredStudents.toString()+" students registered"
           batch = it
       }
    }

    private fun getBatchFromIntent() {
        val batchId = intent.getIntExtra("batchId", -1)
        batchViewModel.getBatchById(batchId)
    }


    private fun toggleVisibility(vararg views: View) {
        views.forEach { view ->
            view.visibility = if (view.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun showDialog(batch: Batch)
    {
        AlertDialog.Builder(this)
            .setTitle("Delete Batch?")

            .setPositiveButton("OK") { dialog, which ->
                batchViewModel.delete(batch)
                Toast.makeText(this, "Batch Deleted Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }

            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}