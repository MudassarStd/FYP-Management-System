package com.android.cuifypmanagementsystem.admin

import android.app.ProgressDialog
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
import com.android.cuifypmanagementsystem.databinding.ActivityStartFypActivityyBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.room.datamodels.FypActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_SECRETORY
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModelFactory
import com.android.cuifypmanagementsystem.viewmodel.H_S_SelectionViewModel

class StartFypActivity : AppCompatActivity() {
    private val binding: ActivityStartFypActivityyBinding by lazy {
        ActivityStartFypActivityyBinding.inflate(layoutInflater)
    }
    private lateinit var selectionViewModel : H_S_SelectionViewModel
    private lateinit var fypActivityViewModel : FypActivityViewModel

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

        // Initialize ViewModel with factory
        selectionViewModel = (application as BaseApplication).getH_S_SelectionViewModel()

        val fypActivityRepository = (application as BaseApplication).fypActivityRepository
        fypActivityViewModel = ViewModelProvider(this, FypActivityViewModelFactory(fypActivityRepository))[FypActivityViewModel::class.java]




        selectionViewModel.selectedHead.observe(this){
            binding.etFypHead.setText(it)
        }

        selectionViewModel.selectedSecretory.observe(this){
            binding.etFypSecretory.setText(it)
        }

        displayResult()

        binding.btnStartActivity.setOnClickListener {
            val fypActivityRecord = getFypActivityRecord()
            fypActivityRecord?.let {
                fypActivityViewModel.startFypActivity(fypActivityRecord)
            } ?: Toast.makeText(this,"Invalid Data", Toast.LENGTH_SHORT).show()

            observeResults()

        }
    }

    private fun observeResults() {
        fypActivityViewModel.fypActivityStartStatus.observe(this){
            when(it){
                is Result.Success -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Successfully Started Activity", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AllFypActivity::class.java))
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    val errorMessage = it.exception.message ?: "An unknown error occurred"
                    Toast.makeText(this, "Loading data failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressDialog("Starting Activity, please wait..", this)
                }

            }
        }
    }

    private fun getFypActivityRecord() : FypActivity?{
        val fyphead = binding.etFypHead.text.toString()
        val fypsec = binding.etFypSecretory.text.toString()
        val startyear = binding.etFypActivityBatch.text.toString()
        return if(fyphead.isNotEmpty() && fypsec.isNotEmpty() && startyear.isNotEmpty())
        {
            FypActivity(fyphead, fypsec, startyear, 0.toString(), "CurrentDate")
        }
        else{
            null
        }
    }

    fun displayResult(){
        val fypHead = binding.etFypHead
        val fypSec = binding.etFypSecretory

        fypHead.setOnClickListener {
            val intent = Intent(this, DisplayTeacherActivity::class.java).also {
                it.action = ACTION_SELECT_FYP_HEAD
            }
            startActivity(intent)
        }

        fypSec.setOnClickListener {
            val intent = Intent(this, DisplayTeacherActivity::class.java).also {
                it.action = ACTION_SELECT_FYP_SECRETORY
            }
            startActivity(intent)
        }
    }


}