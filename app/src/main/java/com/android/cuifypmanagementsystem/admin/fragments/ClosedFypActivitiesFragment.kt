package com.android.cuifypmanagementsystem.admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.adapters.FypActivityAdapter
import com.android.cuifypmanagementsystem.databinding.FragmentClosedFypActivitiesBinding
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel

class ClosedFypActivitiesFragment : Fragment() {

    private lateinit var fypActivityViewModel: FypActivityViewModel
    private val activityAdapter: FypActivityAdapter by lazy {
        FypActivityAdapter(requireContext(), emptyList())
    }

    private var _binding: FragmentClosedFypActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClosedFypActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvClosedFypActivities.adapter = activityAdapter
        binding.rvClosedFypActivities.layoutManager = LinearLayoutManager(requireContext())

        fypActivityViewModel = ViewModelProvider(requireActivity())[FypActivityViewModel::class.java]

        fypActivityViewModel.fypActivitiesFetch.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    val closedActivities = result.data.filter {
                        !it.status
                    }
                    if(closedActivities.isNotEmpty())
                    {
                        activityAdapter.updateActivitiesData(closedActivities)
                        toggleList(true)
                    }
                    else {
                        toggleList(false)
                    }
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                    // Handle failure
                }
                is Result.Loading -> {
                    showProgressDialog("laoding data", requireContext())
                }
            }
        }
    }

    private fun toggleList(hasItem : Boolean) {
        binding.tvNoClosedActivity.visibility = if(!hasItem) View.VISIBLE else View.GONE
        binding.rvClosedFypActivities.visibility = if(hasItem) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
