package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.BatchAdapter
import com.android.cuifypmanagementsystem.adapters.OnAction
import com.android.cuifypmanagementsystem.databinding.ActivityBatchBinding
import com.android.cuifypmanagementsystem.room.datamodels.Batch
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel

class BatchActivity : AppCompatActivity() , OnAction {
    private lateinit var binding : ActivityBatchBinding
    private val batchAdapter: BatchAdapter by lazy {
        BatchAdapter(emptyList(), this)
    }
    private val viewModel : BatchViewModel by lazy {
        ViewModelProvider(this)[BatchViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val list : List<Batch> = listOf(
            Batch(0, "FA21", 6, 78),
            Batch(0, "SP21", 7, 78),
            Batch(0, "FA20", 8, 100)
        )

        batchAdapter.updateBatches(list)
        batchAdapter.setOnCategoryClickListenerInterface(this)

        setAdapterOnRV()

        binding.fabAddBatch.setOnClickListener {
            startActivity(Intent(this, AddEditBatchActivity::class.java))
        }

    }

    private fun setAdapterOnRV() {
        binding.rvBatches.adapter = batchAdapter
        binding.rvBatches.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBatches()
        viewModel.batches.observe(this) {
            batchAdapter.updateBatches(it)
            toggleFab(it.size < 3)
        }
    }

    override fun onDeleted(batch : Batch) {
        showDialog(batch)
    }

    private fun showDialog(batch: Batch)
    {
         AlertDialog.Builder(this)
        .setTitle("Delete Batch?")

        .setPositiveButton("OK") { dialog, which ->
            viewModel.delete(batch)
        }

        .setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
            .create()
            .show()
    }

    private fun toggleFab(show: Boolean) {
        binding.fabAddBatch.visibility = if (show) View.VISIBLE else View.GONE
    }


}