package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.BatchAdapter
import com.android.cuifypmanagementsystem.adapters.OnAction
import com.android.cuifypmanagementsystem.databinding.ActivityBatchBinding
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_ACTIVITY_BATCH
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModel
import com.android.cuifypmanagementsystem.viewmodel.BatchViewModelFactory
import com.android.cuifypmanagementsystem.viewmodel.H_S_SelectionViewModel

class BatchActivity : AppCompatActivity() , OnAction {
    private lateinit var binding : ActivityBatchBinding
    private lateinit var batchViewModel: BatchViewModel
    private lateinit var selectionViewModel: H_S_SelectionViewModel
    private var isBatchSelectionIntent: Boolean = false


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

        checkIntentAction()
        
        selectionViewModel = (application as BaseApplication).getH_S_SelectionViewModel()

        val batchRepository = (application as BaseApplication).batchRepository
        batchViewModel = ViewModelProvider(this, BatchViewModelFactory(batchRepository))[BatchViewModel::class.java]

        batchAdapter.setOnCategoryClickListenerInterface(this)

        fetchBatches()
        setAdapterOnRV()

        binding.fabAddBatch.setOnClickListener {
            startActivity(Intent(this, AddEditBatchActivity::class.java))
        }

    }

    private fun fetchBatches() {

        Log.d("TestingBatchLogic", "Inside fetch in activity")

        batchViewModel.batchesFromCloud.observe(this){
            when(it){
                is Result.Success -> {
                    val data = it.data
//                    hideProgressDialog()

                    batchAdapter.updateBatches(data)
                    toggleFab(data.size < 3)
                }
                is Result.Failure -> {
//                    hideProgressDialog()
                    val errorMessage = it.exception.message ?: "An unknown error occurred"
                    Toast.makeText(this, "Loading data failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressDialog("Loading Batches, please wait..", this)
                }
            }
        }
    }

    private fun checkIntentAction(){
        if(intent.action == ACTION_SELECT_FYP_ACTIVITY_BATCH){
            toggleFab(false)
            isBatchSelectionIntent = true
        }
    }

    private fun setAdapterOnRV() {
        binding.rvBatches.adapter = batchAdapter
        binding.rvBatches.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        batchViewModel.fetchAllBatches()
    }

    override fun onDeleted(batch : Batch) {
        showDialog(batch)
    }

    override fun onBatchSelectionForActivity(batch: Batch) {
        if (isBatchSelectionIntent)
        {
            if (batch.fypActivityStatus){
                Toast.makeText(this, "Activity already started for this batch", Toast.LENGTH_SHORT).show()
            }
            else{
                selectionViewModel.setSelectedBatch(batch)
                finish()
            }
        }
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