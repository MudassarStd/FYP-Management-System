package com.android.cuifypmanagementsystem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.ActivityAddEditBatchBinding
import com.android.cuifypmanagementsystem.room.Batch
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel

class AddEditBatchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddEditBatchBinding
    private val viewModel : BatchViewModel by lazy {
        ViewModelProvider(this)[BatchViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getAllBatches()



        binding.btnUpdate.setOnClickListener {
            val batch = getData()

            batch?.let {
                // if batch does not exist already
                if (viewModel.validateBatch(batch.name, batch.semester))
                {
                    viewModel.insert(batch)
                    Toast.makeText(this, "Batch Added Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(this, "Batch exists already", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun getData(): Batch? {
        val batch = binding.etBatch.text.toString()
        val semester = binding.etSemester.text.toString()

        // fetch number of students registered in current batch from std tables
        return if (batch.isNotEmpty() && semester.isNotEmpty()) {
            Batch(0, batch, semester.toInt(), 0)
        } else {
            Toast.makeText(this, "Invalid Information", Toast.LENGTH_SHORT).show()
            null
        }
    }


}