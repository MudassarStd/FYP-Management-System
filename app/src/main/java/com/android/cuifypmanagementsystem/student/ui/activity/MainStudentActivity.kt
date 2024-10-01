package com.android.cuifypmanagementsystem.student.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityMainStudentBinding
import com.android.cuifypmanagementsystem.student.datamodel.Group
import com.android.cuifypmanagementsystem.student.viewmodel.StudentGroupViewModel
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainStudentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainStudentBinding.inflate(layoutInflater) }
    private val studentGroupViewModel: StudentGroupViewModel by viewModels()
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

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        studentGroupViewModel.fetchMyGroup(uid)

        studentGroupViewModel.fetchGroupResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data != null) {
                        binding.pbMainStudent.visibility = View.GONE
                        updateUI(result.data)
                        Log.d("GroupFetchTesting", "Group data: ${result.data}")
                    } else {
                        binding.tvNoRegistrationMessage.visibility = View.VISIBLE
                        binding.btnRegisterGroup.isEnabled = true
                        binding.pbMainStudent.visibility = View.GONE
                    }
                }

                is Result.Failure -> {
                    Log.d("GroupFetchTesting", "Failed to Fetch Group: ${result.exception.message}")
                    binding.pbMainStudent.visibility = View.GONE
                    binding.tvNoRegistrationMessage.visibility = View.VISIBLE
                    binding.tvNoRegistrationMessage.setText("NO REGISTRATION FOUND")
                }

                Result.Loading -> TODO()
            }
        }

        binding.btnRegisterGroup.setOnClickListener {
            val intent = Intent(this, RegisterGroupActivity::class.java).apply {
                putExtra("studentId", uid)
            }
            startActivity(intent)
        }
    }

    private fun updateUI(group : Group) {

        binding.sectionGroupDetails.visibility = View.VISIBLE

        binding.tvProjectTitle.text = group.project!!.title
        binding.tvProjectDescription.text = group.project.description
        if (group.supervisor == null) {
            binding.btnSelectSupervisor.visibility = View.VISIBLE
            binding.tvSupervisor.visibility = View.GONE

            binding.btnSelectSupervisor.setOnClickListener {
                val intent = Intent(this, SelectSupervisorActivity::class.java).apply {
                    putExtra("groupId", group.firestoreId)
                    putExtra("groupBatch", group.batch)
                }
                startActivity(intent)
            }

        } else {
            binding.tvSupervisor.text = group.supervisor
            binding.btnSelectSupervisor.visibility = View.GONE
        }
        group.groupMembers?.let {
            binding.tvStudentName1.text = it[0]
            binding.tvStudentName2.text = it[1]
            if (it.size == 3) {
                binding.tvStudentName3.text = it[2]
            }
        }
    }
}