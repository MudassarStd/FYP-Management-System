package com.android.cuifypmanagementsystem.admin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.adapters.FypActivityAdapter
import com.android.cuifypmanagementsystem.adapters.OnActivityAction
import com.android.cuifypmanagementsystem.admin.activities.StartFypActivity
import com.android.cuifypmanagementsystem.databinding.FragmentCurrentFypActivitiesBinding
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.GlobalSharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrentFypActivitiesFragment : Fragment() ,  OnActivityAction {

    private val fypActivityViewModel: FypActivityViewModel by viewModels()
//    private lateinit var globalSharedViewModel: GlobalSharedViewModel
    private val activityAdapter: FypActivityAdapter by lazy {
        FypActivityAdapter(requireContext(), emptyList())
    }

    private var activityUpdateTrigger : Boolean = false

    private var _binding: FragmentCurrentFypActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAdapter.setOnActivityActionListener(this)
//        globalSharedViewModel = (requireActivity().application as BaseApplication).getGlobalSharedViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentFypActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCurrentFypActivities.adapter = activityAdapter
        binding.rvCurrentFypActivities.layoutManager = LinearLayoutManager(requireContext())

        binding.fabAddActivity.setOnClickListener {
            startActivity(Intent(requireContext(), StartFypActivity::class.java))
        }

        fypActivityViewModel.fetchFypActivityData(true)

        fypActivityViewModel.fypActivitiesFetch.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    if (result.data.isNotEmpty()) {
                        toggleFab(result.data.size < 3)
                        activityAdapter.updateActivitiesData(result.data)
                        toggleList(true)
                    } else {
                        toggleFab(true)
                        toggleList(false)
                    }
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                    // Handle failure
                }
                is Result.Loading -> {
                    showProgressDialog("loading data...", requireContext())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (activityUpdateTrigger) {
            fypActivityViewModel.fetchFypActivityData(true)
            activityUpdateTrigger = false
        }
    }

    private fun toggleFab(showFab: Boolean) {
        binding.fabAddActivity.visibility = if (showFab) View.VISIBLE else View.GONE
    }

    private fun toggleList(hasItem : Boolean) {
        binding.tvNoActiveActivity.visibility = if(!hasItem) View.VISIBLE else View.GONE
        binding.rvCurrentFypActivities.visibility = if(hasItem) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFypActivitySelected() {
        activityUpdateTrigger = true
//        globalSharedViewModel.informActivityUpdate(true)
    }
}
