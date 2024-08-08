package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.OnTeacherEvents
import com.android.cuifypmanagementsystem.adapters.TeacherAdapter
import com.android.cuifypmanagementsystem.databinding.ActivityManageTeacherBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModelFactory
import com.android.cuifypmanagementsystem.viewmodels.DepartmentViewModel

class ManageTeacherActivity : AppCompatActivity() , OnTeacherEvents  {
    private val binding : ActivityManageTeacherBinding by lazy {
        ActivityManageTeacherBinding.inflate(layoutInflater)
    }

    private val departFilter : DepartmentFilterFragment by lazy {
        DepartmentFilterFragment()
    }

    private val teacherAdapter : TeacherAdapter by lazy {
        TeacherAdapter(emptyList(), this)
    }

    private val departmentViewModel: DepartmentViewModel by lazy{
        ViewModelProvider(this)[DepartmentViewModel::class.java]
    }

    private lateinit var teacherViewModel : TeacherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        teacherAdapter.setOnTeacherEventInterface(this)

        val teacherRepository = (application as BaseApplication).teacherRepository
        teacherViewModel = ViewModelProvider(this, TeacherViewModelFactory(teacherRepository))[TeacherViewModel::class.java]

        Log.d("TeacherCRUDTesting", "ManageTeacher:")
//
//        teacherViewModel.teachers.observe(this){
//           teacherAdapter.updateTeachers(it)
//            Log.d("TeacherCRUDTesting", "ManageTeacher: $it")
//        }




        teacherViewModel.teachersFromCloud.observe(this){result ->
            when(result){
                is Result.Success -> {
                    hideProgressDialog()
                    teacherAdapter.updateTeachers(result.data)
                }
                is Result.Loading -> {
                    showProgressDialog("Loading Data, please wait..", this)
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    val errorMessage = result.exception.message ?: "An unknown error occurred"
                    Toast.makeText(this, "Loading data failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

        }



        // search bar

        val searchView = binding.svManageTeachers
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                teacherAdapter.filter(newText ?: "")
                return true
            }
        })


        // filter teachers
        binding.ivBtnFilterTeachers.setOnClickListener {
            departFilter.show(supportFragmentManager, departFilter.tag)
        }

        departmentViewModel.selectedDepartments.observe(this){
            Toast.makeText(this, "$it  Selected", Toast.LENGTH_SHORT).show()
        }

        binding.fabAddTeacher.setOnClickListener {
            startActivity(Intent(this, AddTeacher::class.java))
        }

        setUpRV()
    }

    private fun setUpRV() {
        binding.rvManageTeachers.adapter = teacherAdapter
        binding.rvManageTeachers.layoutManager = LinearLayoutManager(this)
    }

    override fun onDeleteSignal(teacher: Teacher) {
        showConfirmationDialog(teacher)
    }

    private fun showConfirmationDialog(teacher: Teacher) {
        AlertDialog.Builder(this)
            .setTitle("Delete Teacher?")

            .setPositiveButton("Confirm") { dialog, which ->
//                teacherViewModel.deleteTeacher(teacher)
                Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }

            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}