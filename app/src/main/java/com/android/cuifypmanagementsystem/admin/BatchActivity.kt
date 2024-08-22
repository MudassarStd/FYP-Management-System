package com.android.cuifypmanagementsystem.admin

import CustomDialogHelper.showActionConfirmationDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

class BatchActivity : AppCompatActivity(), OnAction {

    private lateinit var binding: ActivityBatchBinding
    private lateinit var batchViewModel: BatchViewModel
    private lateinit var selectionViewModel: H_S_SelectionViewModel
    private var isBatchSelectionIntent: Boolean = false

    private val batchAdapter: BatchAdapter by lazy {
        BatchAdapter(emptyList(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureStatusBar()

        initializeViewModels()
        setupRecyclerView()
        handleIntentActions()

        binding.fabAddBatch.setOnClickListener {
            startActivity(Intent(this, AddEditBatchActivity::class.java))
        }

        setupToolbarButtons()
    }

    // **Update: Modularized and extracted methods for better readability**
    private fun configureStatusBar() {
        window.statusBarColor = Color.parseColor("#576AE0")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViewModels() {
        val application = application as BaseApplication
        selectionViewModel = application.getH_S_SelectionViewModel()
        val batchRepository = application.batchRepository
        batchViewModel = ViewModelProvider(this, BatchViewModelFactory(batchRepository))[BatchViewModel::class.java]
        batchAdapter.setOnActionListener(this)
    }

    private fun setupRecyclerView() {
        binding.rvBatches.apply {
            adapter = batchAdapter
            layoutManager = LinearLayoutManager(this@BatchActivity)
        }
        fetchBatches()
    }

    private fun fetchBatches() {
        Log.d("TestingBatchLogic", "Fetching batches in activity")
        batchViewModel.batchesFromCloud.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    batchAdapter.updateBatchList(result.data)
                    toggleFabVisibility(result.data.size < 3)
                    hideProgressDialog()
                }
                is Result.Failure -> {
                    val errorMessage = result.exception.message ?: "An unknown error occurred"
                    Toast.makeText(this, "Loading data failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    hideProgressDialog()
                }
                is Result.Loading -> showProgressDialog("Loading Batches, please wait...", this)
            }
        }
    }

    private fun handleIntentActions() {
        if (intent.action == ACTION_SELECT_FYP_ACTIVITY_BATCH) {
            isBatchSelectionIntent = true
            toggleFabVisibility(false)
            hideEditOptions()
            batchAdapter.toggleBatchSelectionMode()
        }
    }

    private fun setupToolbarButtons() {
        binding.btnToolbarEditBatch.setOnClickListener {
            toggleEditMode(true)
        }

        binding.btnToolbarCancelEditBatch.setOnClickListener {
            toggleEditMode(false)
        }
    }

    private fun toggleEditMode(isEditMode: Boolean) {
        batchAdapter.toggleEditMode()
        binding.btnToolbarEditBatch.visibility = if (isEditMode) View.GONE else View.VISIBLE
        binding.btnToolbarCancelEditBatch.visibility = if (isEditMode) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        batchViewModel.fetchAllBatches()
    }

    override fun onDeleted(batch: Batch) {
        // Uncomment and implement if needed
        // showDialog(batch)
    }

    override fun onBatchSelectionForActivity(batch: Batch) {
        if (isBatchSelectionIntent) {
            showActionConfirmationDialog(this,
                "Are you sure to start activity for ${batch.name} batch? \n\n<b>Note:</b> Once activity is started for this batch, you will not be able to edit this batch",
                onProceed = {
                    selectionViewModel.setSelectedBatch(batch)
                    finish()
                })
        }
    }

    private fun toggleFabVisibility(show: Boolean) {
        binding.fabAddBatch.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun hideEditOptions() {
        binding.btnToolbarEditBatch.visibility = View.GONE
        binding.btnToolbarCancelEditBatch.visibility = View.GONE
    }
}
