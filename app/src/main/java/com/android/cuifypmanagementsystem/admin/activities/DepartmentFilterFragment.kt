package com.android.cuifypmanagementsystem.admin.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.admin.adapters.recyclerview.DepartFilterAdapter
import com.android.cuifypmanagementsystem.admin.adapters.recyclerview.OnDepartSelected
import com.android.cuifypmanagementsystem.databinding.FragmentDepartmentFilterBinding
import com.android.cuifypmanagementsystem.admin.viewmodel.DepartmentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DepartmentFilterFragment : BottomSheetDialogFragment(), OnDepartSelected {

    private var _binding: FragmentDepartmentFilterBinding? = null
    private val binding get() = _binding!!
    private val departments: List<String> = listOf(
        "Computer Science",
        "Electrical Engineering",
        "Mechanical Engineering",
        "Civil Engineering",
        "Chemical Engineering",
        "Biomedical Engineering",
        "Industrial Engineering",
        "Aerospace Engineering",
        "Materials Science",
        "Environmental Engineering"
    )


    private val departmentViewModel: DepartmentViewModel by lazy{
        ViewModelProvider(requireActivity())[DepartmentViewModel::class.java]
    }

    private val filterAdapter: DepartFilterAdapter by lazy {
        DepartFilterAdapter(departments, requireContext(),this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepartmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvDepartFilter.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = filterAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onSelect(selectedDepartments: List<String>) {
        binding.btnApplyFilter.setOnClickListener {
            departmentViewModel.selected(selectedDepartments)
            dismiss()
        }
    }

}
