package com.android.cuifypmanagementsystem.teacher.activities.mygroups.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.databinding.FragmentCurrentGroupsBinding
import com.android.cuifypmanagementsystem.teacher.activities.mygroups.MyGroupsViewModel
import com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.GroupsAndRequestsAdapter
import com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.callbacks.OnGroupRequestClick
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result

class CurrentGroupsFragment : Fragment() , OnGroupRequestClick{

    private var _binding: FragmentCurrentGroupsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MyGroupsViewModel

    private val adapter = GroupsAndRequestsAdapter(data = emptyList(), type = GroupDataType.GROUPS, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MyGroupsViewModel::class.java]
        viewModel.fetchGroups(GroupDataType.GROUPS)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentCurrentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.groups.observe(viewLifecycleOwner) {result ->
            when(result) {
                is Result.Success -> {
                    hideProgressDialog()
                    if (result.data.isNotEmpty()) {
                        adapter.updateData(result.data)
                        setupRV()
                        binding.tvNoGroupsFoundMessage.visibility = View.GONE
                    } else {
                        binding.tvNoGroupsFoundMessage.visibility = View.VISIBLE
                    }

                }
                is Result.Failure -> {
                    hideProgressDialog()
                    Log.e("GroupDataTesting", "Data Groups Fetch Failed: ${result.exception.message}")
                }
                is Result.Loading -> {
                    showProgressDialog(context = requireContext(), message = "Loading Groups...")
                }
            }

        }
    }

    private fun setupRV() {
        binding.rvTeacherGroupList.adapter = adapter
        binding.rvTeacherGroupList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to avoid memory leaks
        _binding = null
    }

    override fun onApproveClick(groupId: String, batch: String?) {
        TODO("Not yet implemented")
    }

    override fun onRejectClick(groupId: String) {
        TODO("Not yet implemented")
    }
}
