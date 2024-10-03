package com.android.cuifypmanagementsystem.student.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.databinding.ActivityRegisterGroupBinding
import com.android.cuifypmanagementsystem.student.adapter.recyclerview.GroupMembersAdapter
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.student.datamodel.Group
import com.android.cuifypmanagementsystem.student.datamodel.GroupMember
import com.android.cuifypmanagementsystem.student.datamodel.Project
import com.android.cuifypmanagementsystem.student.ui.fragment.AvailableStudentListFragment
import com.android.cuifypmanagementsystem.student.viewmodel.RegisterGroupViewModel
import com.android.cuifypmanagementsystem.student.viewmodel.StudentGroupViewModel
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterGroupActivity : AppCompatActivity() , GroupMembersAdapter.OnGroupMemberSelectionInterface{
    private val binding by lazy { ActivityRegisterGroupBinding.inflate(layoutInflater) }
    private val studentGroupViewModel: StudentGroupViewModel by viewModels()
    private val registerGroupViewModel: RegisterGroupViewModel by viewModels()
    private val adapter = GroupMembersAdapter(emptyList(), this)

    private var studentBatch : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupUI()
        getCurrentStudent()
    }

    private fun setupUI() {
        window.statusBarColor = Color.parseColor("#576AE0")
        binding.toolbarGroupRegistration.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnRegisterStudentGroup.setOnClickListener {
            if (validateGroupData()) {
                studentGroupViewModel.registerGroup(collectGroupData())
                observeRegistrationResults()
            }
        }

        binding.btnAddMembers.setOnClickListener {
            AvailableStudentListFragment().show(supportFragmentManager, "AvailableStudentListFragment")
            observeMembersSelection()
        }

        // Setup recyclerview
        binding.rvGroupMembers.adapter = adapter
        binding.rvGroupMembers.layoutManager = LinearLayoutManager(this)
    }

    private fun validateGroupData(): Boolean {
        val projectTitle = binding.etStudentProjectTitle.text.toString()
        val projectDescription = binding.etStudentProjectDescription.text.toString()
        val member1 = binding.tvYourRegistration.text.toString()
//      val member2 = binding.tvStudentGroupMember2Reg.text.toString()

        return when {
            projectTitle.isBlank() -> {
                showToast("Please enter project title.")
                false
            }
            projectDescription.isBlank() -> {
                showToast("Please enter project description.")
                false
            }
            member1.isBlank() -> {
                showToast("No student found for registration.")
                false
            }
//            member2.isBlank() -> {
//                showToast("A group must have at least 2 members.")
//                false
//            }
            else -> true
        }
    }

    private fun observeMembersSelection() {
        registerGroupViewModel.groupMembers.observe(this) { members ->
//            updateMemberUI(members)
            if (members.size <= 2) {
                adapter.updateMembers(members)
            }
            binding.tvGroupCount.text = members.size.toString()
            Log.d("TestingMemberSelection", "Selected Members: $members")
        }
    }

    private fun updateMemberUI(members: List<GroupMember>) {


//        when (members.size) {
//            0 -> resetMemberUI()
//            1 -> {
//                binding.tvStudentGroupMember2Reg.text = members[0].registrationNumber
//                binding.llMember2Section.visibility = View.VISIBLE
//                binding.llMember3Section.visibility = View.GONE
//            }
//            2 -> {
//                binding.tvStudentGroupMember2Reg.text = members[0].registrationNumber
//                binding.tvStudentGroupMember3Reg.text = members[1].registrationNumber
//                binding.llMember2Section.visibility = View.VISIBLE
//                binding.llMember3Section.visibility = View.VISIBLE
//                binding.btnAddMembers.isEnabled = false
//            }
//        }
//
//        binding.tvMinusMember2.setOnClickListener {
//            removeMember(members[0])
//        }
//
//        if (members.size > 1) {
//            binding.tvMinusMember3.setOnClickListener {
//                removeMember(members[1])
//            }
//        }
    }

    private fun resetMemberUI() {
        binding.btnAddMembers.isEnabled = true
    }

    private fun removeMember(member: GroupMember) {
        registerGroupViewModel.removeMember(member)
        binding.btnAddMembers.isEnabled = true
        resetMemberUI()
    }

    private fun getCurrentStudent() {
        val studentId = intent.getStringExtra("studentId")
        studentId?.let {
            studentGroupViewModel.getStudentById(it)
            studentGroupViewModel.student.observe(this) { student ->
                student?.let {
                    binding.tvYourRegistration.text = it.registrationNumber
                    binding.tvYourName.text = it.name
                    studentBatch = student.batch

                    // data for member filtration
                    registerGroupViewModel.setMemberFilterParameters(it.email, it.depart)
                }
            }
        }
    }

    private fun collectGroupData(): Group {
        val pTitle = binding.etStudentProjectTitle.text.toString()
        val pDescription = binding.etStudentProjectDescription.text.toString()
        val member1 = binding.tvYourRegistration.text.toString()

        val project = Project(
            title = pTitle,
            description = pDescription,
            techStack = null
        )

        return Group(
            firestoreId = null,
            groupMembers = listOf(member1, "fjklsfjksldjfsld").filter { it.isNotBlank() },
            membersCount = listOf(member1, "fjksdfjskdljfkls").count { it.isNotBlank() },
            batch = studentBatch,
            project = project,
            supervisor = null,
            registrationDate = System.currentTimeMillis()
        )
    }

    private fun observeRegistrationResults() {
        studentGroupViewModel.registerGroupResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    showToast("Group registered successfully!")
                    startActivity(Intent(this, MainStudentActivity::class.java))
                    finish()
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    showToast("Failed to register group: ${result.exception.message}")
                }
                is Result.Loading -> showProgressDialog("Registering Group...", this)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onMemberRemoved(member: GroupMember) {
        registerGroupViewModel.removeMember(member)
    }
}