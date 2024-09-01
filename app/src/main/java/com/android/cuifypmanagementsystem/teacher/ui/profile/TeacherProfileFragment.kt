package com.android.cuifypmanagementsystem.teacher.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.ChangePasswordActivity
import com.android.cuifypmanagementsystem.LoginActivity
import com.android.cuifypmanagementsystem.databinding.FragmentTeacherGroupsBinding
import com.android.cuifypmanagementsystem.databinding.FragmentTeacherProfileBinding
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherProfileFragment : Fragment() {

    private var _binding: FragmentTeacherProfileBinding? = null
    private val userAuthViewModel : UserAuthViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}