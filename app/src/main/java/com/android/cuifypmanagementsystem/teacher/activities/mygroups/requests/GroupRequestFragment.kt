package com.android.cuifypmanagementsystem.teacher.activities.mygroups.requests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.databinding.FragmentGroupRequestBinding
import com.android.cuifypmanagementsystem.teacher.activities.mygroups.MyGroupsViewModel
import com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.GroupsAndRequestsAdapter
import com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.callbacks.OnGroupRequestClick
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result

class GroupRequestFragment : Fragment() , OnGroupRequestClick{


    private var _binding: FragmentGroupRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : MyGroupsViewModel
    private val adapter = GroupsAndRequestsAdapter(data = emptyList(), type = GroupDataType.REQUESTS, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MyGroupsViewModel::class.java]
        viewModel.fetchGroups(GroupDataType.REQUESTS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGroupRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requests.observe(viewLifecycleOwner) {result ->
            when(result) {
                is Result.Success -> {
                    hideProgressDialog()
                    if (result.data.isNotEmpty()) {
                        adapter.updateData(result.data)
                        setupRV()
                        binding.tvNoRequestsFoundMessage.visibility = View.GONE
                    } else {
                        adapter.updateData(emptyList())
                        binding.tvNoRequestsFoundMessage.visibility = View.VISIBLE
                    }
                }

                is Result.Failure -> {
                    hideProgressDialog()
                    Log.e("GroupDataTesting", "Data Requests Fetch Failed: ${result.exception.message}")
                }
                is Result.Loading -> {showProgressDialog(context = requireContext(), message = "Loading Requests...")}
            }

        }
    }

    private fun setupRV() {
        binding.rvTeacherGroupRequestList.adapter = adapter
        binding.rvTeacherGroupRequestList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onApproveClick(groupId: String, batch: String?) {
        viewModel.addGroupToSupervision(groupId, batch!!)

        Log.d("GroupDataTesting", "Clicked")


        viewModel.groupAdditionToSupervisionStatus.observe(this) {flag ->
            if (flag) {
                viewModel.fetchGroups(GroupDataType.REQUESTS)
                viewModel.fetchGroups(GroupDataType.GROUPS)
                Log.d("GroupDataTesting", "Addition Success")

                showToast("Group added successfully")
            } else {
                Log.d("GroupDataTesting", "Addition Failure")
                showToast("Group could not be added")
            }
        }
    }

    override fun onRejectClick(groupId: String) {
        viewModel.rejectGroupforSupervision(groupId)
    }


    private fun showToast(msg : String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
