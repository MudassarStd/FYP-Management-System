package com.android.cuifypmanagementsystem.student.ui.activity

import CustomDialogHelper.showActionConfirmationDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.student.adapter.recyclerview.AvailableSupervisorAdapter
import com.android.cuifypmanagementsystem.databinding.ActivitySelectSupervisorBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.student.adapter.recyclerview.OnSupervisorClickListener
import com.android.cuifypmanagementsystem.student.viewmodel.SupervisorViewModel
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSupervisorActivity : AppCompatActivity(), OnSupervisorClickListener {

    private val binding by lazy {ActivitySelectSupervisorBinding.inflate(layoutInflater)}
    private lateinit var adapter : AvailableSupervisorAdapter
    private val supervisorViewModel : SupervisorViewModel by viewModels()
    private var groupId : String? = null
    private var groupBatch : String? = null
    private var requestsMap : MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#576AE0")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        
        groupId = intent.getStringExtra("groupId")
        groupBatch = intent.getStringExtra("groupBatch")

        Toast.makeText(this, "Group Id is $groupId", Toast.LENGTH_SHORT).show()

        adapter = AvailableSupervisorAdapter(emptyList(), this, groupId)

        setupRV()
        supervisorViewModel.fetchSupervisors()
        observeSupervisorFetchResults()
    }

    private fun observeSupervisorFetchResults() {
        supervisorViewModel.supervisors.observe(this) {result ->
            when(result) {
                is Result.Success -> {
                    adapter.updateTeachers(result.data)
                }
                is Result.Failure -> {

                }
                is Result.Loading -> {

                }
            }
        }
    }

    private fun setupRV() {
        binding.rvAvailableSupervisors.adapter = adapter
        binding.rvAvailableSupervisors.layoutManager = LinearLayoutManager(this)
    }

    override fun onRequestSupervisorClick(teacher: Teacher)  {
        var requestState = false
        supervisorViewModel.requestSupervisor(groupId, groupBatch, teacher.firestoreId)
//        showActionConfirmationDialog(this, "Once request is sent, it cannot be undone.\nYour group information will displayed to requested supervisors.", onProceed = {
//            supervisorViewModel.requestSupervisor(groupId, groupBatch, teacher.firestoreId)
//
//        })
    }

    fun addToRequestMap(groupId: String?, firestoreId: String?) {
        if (groupId == null || firestoreId == null) return
        if (requestsMap.containsKey(groupId)) {
            requestsMap[groupId]?.add(firestoreId)
        } else {
            requestsMap[groupId] = mutableListOf(firestoreId)
        }

        Log.d("SupervisorRequestsMap", "Request Map: $requestsMap")
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}