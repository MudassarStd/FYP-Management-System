package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_SECRETORY
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.H_S_SelectionViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModelFactory
import com.android.cuifypmanagementsystem.viewmodels.DepartmentViewModel
import java.io.Serializable

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
    private lateinit var selectionViewModel: H_S_SelectionViewModel

    // changes
    private  var headSelectionIntent : Boolean = false
    private  var secretorySelectionIntent : Boolean = false
    // changes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // selection VM for persisting selected values (head & secretory)
        selectionViewModel = (application as BaseApplication).getH_S_SelectionViewModel()
        // changes

        teacherAdapter.setOnTeacherEventInterface(this)

        val teacherRepository = (application as BaseApplication).teacherRepository
        teacherViewModel = ViewModelProvider(this, TeacherViewModelFactory(teacherRepository))[TeacherViewModel::class.java]

        checkIntentAction()

        // observing teachers data from cloud

        teacherViewModel.teachersFromCloud.observe(this){result ->
            when(result){
                is Result.Success -> {
                    hideProgressDialog()
                    teacherAdapter.updateTeachers(result.data)
                    Log.d("hfsjdhfsjd","data from cloud: ${result.data}")
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

        // setting up search bar

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
            teacherAdapter.filterByDepartments(it)
        }

        binding.fabAddTeacher.setOnClickListener {
            startActivity(Intent(this, AddTeacher::class.java))
        }

        setUpRV()
    }

    private fun checkIntentAction(){
        val intentAction = intent.action
        val isSelectionIntent = when (intentAction) {
            ACTION_SELECT_FYP_HEAD -> {
                headSelectionIntent = true
                changeScreenTitle("Select FYP Head")
                true
            }
            ACTION_SELECT_FYP_SECRETORY -> {
                secretorySelectionIntent = true
                changeScreenTitle("Select FYP Secretory")
                true
            }
            else -> false
        }


        // getting data from cloud based on Intent
        if (isSelectionIntent) {
            binding.fabAddTeacher.visibility = View.GONE
            teacherViewModel.getNotFypHeadSecretaries()
        } else {
            binding.fabAddTeacher.visibility = View.VISIBLE
            teacherViewModel.getAllTeachersFromCloud()
        }

    }

    private fun setUpRV() {
        binding.rvManageTeachers.adapter = teacherAdapter
        binding.rvManageTeachers.layoutManager = LinearLayoutManager(this)
    }

    override fun onDeleteSignal(teacher: Teacher) {
        showConfirmationDialog(teacher)
    }

    override fun onTeacherClick(teacher: Teacher) {
        if (headSelectionIntent){
            selectionViewModel.setSelectedHead(teacher)
            finish()
        }
        else if(secretorySelectionIntent){
            selectionViewModel.setSelectedSecretory(teacher)
            finish()
        }
        else{
            val intent = (Intent(this, TeacherDetailsActivity::class.java)).apply {
                putExtra("name", teacher.name)
                putExtra("department", teacher.department)
                putExtra("isSupervisor", teacher.supervisor)
                putExtra("isFypHeadOrSecretory", teacher.fypHeadOrSecretory)
                putExtra("activityRole", teacher.fypActivityRole?.activityRole)
                putExtra("activityId", teacher.fypActivityRole?.activityId)
            }
            Log.d("hfsjdhfsjd","value test before starting actv: ${teacher.fypHeadOrSecretory}")
            startActivity(intent)
        }
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

    private fun changeScreenTitle(title : String){
        binding.tvManageTeachersTitle.text = title
    }

}