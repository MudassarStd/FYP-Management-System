package com.android.cuifypmanagementsystem.teacher.ui.groups.current

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.cuifypmanagementsystem.databinding.FragmentCurrentGroupsBinding
import com.android.cuifypmanagementsystem.teacher.activities.TeacherGroupDetailsActivity

class CurrentGroupsFragment : Fragment() {

    private var _binding: FragmentCurrentGroupsBinding? = null
    private val binding get() = _binding!!

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
        // Use the binding object to access views


        binding.btnAcceptGroup.setOnClickListener {
            startActivity(Intent(requireContext(), TeacherGroupDetailsActivity::class.java ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to avoid memory leaks
        _binding = null
    }
}
