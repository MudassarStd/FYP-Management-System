package com.android.cuifypmanagementsystem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.adapters.DepartFilterAdapter
import com.android.cuifypmanagementsystem.adapters.OnDepartSelected
import com.android.cuifypmanagementsystem.adapters.TeacherAdapter
import com.android.cuifypmanagementsystem.databinding.ActivityManageTeacherBinding
import com.android.cuifypmanagementsystem.viewmodels.DepartmentViewModel

class ManageTeacher : AppCompatActivity()  {
    private val binding : ActivityManageTeacherBinding by lazy {
        ActivityManageTeacherBinding.inflate(layoutInflater)
    }

    private val departFilter : DepartmentFilterFragment by lazy {
        DepartmentFilterFragment()
    }

    private val teacherAdapter : TeacherAdapter by lazy {
        TeacherAdapter()
    }

    private val departmentViewModel: DepartmentViewModel by lazy{
        ViewModelProvider(this)[DepartmentViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // filter teachers
        binding.fabFilter.setOnClickListener {
            departFilter.show(supportFragmentManager, departFilter.tag)
        }

        departmentViewModel.selectedDepartments.observe(this){
            Toast.makeText(this, "$it  Selected", Toast.LENGTH_SHORT).show()
        }
    }
}