package com.android.cuifypmanagementsystem.student.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.android.cuifypmanagementsystem.utils.Result
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.databinding.FragmentAvailableStudentListBinding
import com.android.cuifypmanagementsystem.student.adapter.recyclerview.AvailableStudentsAdapter
import com.android.cuifypmanagementsystem.student.datamodel.GroupMember
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.student.viewmodel.RegisterGroupViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableStudentListFragment : BottomSheetDialogFragment(), AvailableStudentsAdapter.OnMemberSelectionInterface {

    private var _binding: FragmentAvailableStudentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerGroupViewModel: RegisterGroupViewModel
    private val adapter = AvailableStudentsAdapter(emptyList(), this)
    private var currentEmail : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerGroupViewModel = ViewModelProvider(requireActivity())[RegisterGroupViewModel::class.java]
        observeListFilterParameters()
        observeList()
    }

    private fun observeListFilterParameters() {
        registerGroupViewModel.filterParameters.observe(this) { filterParameters ->
            registerGroupViewModel.fetchAllAvailableStudents(filterParameters.department)
            currentEmail = filterParameters.currentEmail
        }
    }
    private fun observeList() {
        registerGroupViewModel.availableStudents.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val filtered = result.data.filter { it.email != currentEmail}
                    adapter.updateStudents(filtered)
                    binding.pbAvailableStudents.visibility = View.GONE
                    binding.rvAvailableStudents.visibility = View.VISIBLE
                }

                is Result.Failure -> {
                    binding.pbAvailableStudents.visibility = View.GONE
                }

                Result.Loading -> TODO()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvailableStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAvailableStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAvailableStudents.adapter = adapter

        binding.ivDoneSelection.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMemberClicked(student: Student, isSelected: Boolean) {
        val member = GroupMember(null.toString(), student.name, student.registrationNumber)
        if (isSelected) {
            registerGroupViewModel.addMember(member)
        } else {
            registerGroupViewModel.removeMember(member)
        }
    }
}
