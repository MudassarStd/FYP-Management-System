package com.android.cuifypmanagementsystem.teacher.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.FragmentDashboardBinding
import com.android.cuifypmanagementsystem.teacher.activities.DisplayFypIdeasActivity
import com.android.cuifypmanagementsystem.teacher.activities.ShareFypIdeaActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boxShareFypIdea.setOnClickListener {
            startActivity(Intent(requireContext(), ShareFypIdeaActivity::class.java))
        }

        binding.boxViewFypIdeas.setOnClickListener {
            startActivity(Intent(requireContext(), DisplayFypIdeasActivity::class.java))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}