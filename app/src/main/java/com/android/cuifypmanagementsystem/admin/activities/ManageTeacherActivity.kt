package com.android.cuifypmanagementsystem.admin.activities

import CustomDialogHelper.showActionSuccessDialog
import CustomDialogHelper.showReversibleActionFailedDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.OnTeacherEvents
import com.android.cuifypmanagementsystem.adapters.TeacherAdapter
import com.android.cuifypmanagementsystem.databinding.ActivityManageTeacherBinding
import com.android.cuifypmanagementsystem.datamodels.FypActivityRole
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_CHANGE_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_CHANGE_FYP_SECRETORY
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_HEAD
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_SECRETORY
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.FypActivityViewModel
import com.android.cuifypmanagementsystem.viewmodel.GlobalSharedViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.viewmodels.DepartmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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

    private val teacherViewModel : TeacherViewModel by viewModels()
    private val fypActivityViewModel: FypActivityViewModel by viewModels()
    private lateinit var globalSharedViewModel: GlobalSharedViewModel

    // changes
    private  var headSelectionIntent : Boolean = false
    private  var secretorySelectionIntent : Boolean = false
    private  var changeHeadIntent : Boolean = false
    private  var changeSecretoryIntent : Boolean = false
    // changes

    private var actionChangeRole_ActivityId : String? = null
    private var actionChangeRole_CurrentTeacherId : String? = null
    private var actionChangeRole_NewTeacherId : String? = null
    private var actionChangeRole_Role : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#576AE0")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbarManageTeachers.setNavigationOnClickListener {
            onBackPressed()
        }

        // selection VM for persisting selected values (head & secretory)
        globalSharedViewModel = (application as BaseApplication).getGlobalSharedViewModel()
        // changes

        teacherAdapter.setOnTeacherEventInterface(this)


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

            // Conditionals for changing fyp head or secretory role

            ACTION_CHANGE_FYP_HEAD -> {
                changeHeadIntent = true
                changeScreenTitle("Select New Head")
                actionChangeRole_ActivityId = intent.getStringExtra("activityId")
                actionChangeRole_CurrentTeacherId = intent.getStringExtra("currentRoleHolderTeacherId")
                true
            }
               ACTION_CHANGE_FYP_SECRETORY -> {
                changeSecretoryIntent = true
                changeScreenTitle("Select New Secretory")
                actionChangeRole_ActivityId = intent.getStringExtra("activityId")
                actionChangeRole_CurrentTeacherId = intent.getStringExtra("currentRoleHolderTeacherId")
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

        actionChangeRole_NewTeacherId = teacher.firestoreId

        if (headSelectionIntent){
            globalSharedViewModel.setSelectedHead(teacher)
            finish()
        }
        else if(secretorySelectionIntent){
            globalSharedViewModel.setSelectedSecretory(teacher)
            finish()
        }
        else if(changeHeadIntent){
            showDialog("Head", teacher)
        }
        else if(changeSecretoryIntent){
            showDialog("Secretory", teacher)
        }
        else{
            val intent = (Intent(this, TeacherDetailsActivity::class.java)).apply {
                putExtra("name", teacher.name)
                putExtra("department", teacher.department)
                putExtra("isSupervisor", teacher.supervisor)
                putExtra("registrationTimestamp", teacher.registrationTimeStamp)
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
        binding.toolbarManageTeachers.title = title
    }

    private fun showDialog(role : String, newTeacher : Teacher) {

        actionChangeRole_Role = role

        val message: Spanned = Html.fromHtml(
            "Are you sure to select <b>${newTeacher.name}</b> as new <b>Fyp $role</b>? <br><br>" +
                    "<b>Note</b>: If you proceed, you will not be able to undo this action."
        )


        MaterialAlertDialogBuilder(this)
            .setTitle("Update Role") // Set the title of the dialog
            .setMessage(message) // Set the message
            .setPositiveButton("Proceed") { dialog, _ ->
                // Handle positive button click

                updateFypRoles(newTeacher.firestoreId!!, role)

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // Handle negative button click
                dialog.dismiss()
            }

            .show()
    }

    private fun updateFypRoles(newTeacherId : String, role: String) {

        val fypActivityRole = FypActivityRole(actionChangeRole_ActivityId!!, role)

        fypActivityViewModel.updateFypRole(actionChangeRole_ActivityId!!, newTeacherId, role)
        teacherViewModel.updateFypRole(actionChangeRole_CurrentTeacherId!!, newTeacherId, fypActivityRole)

        showProgressDialog("Updating roles..", this)

        observeFypRoleUpdateResults()
    }



    private fun observeFypRoleUpdateResults() {
        val bothResultsObserver = MediatorLiveData<Pair<Result<Void?>?, Result<Void?>?>>().apply {

            var fypActivityUpdateResult: Result<Void?>? = null
            var teacherUpdateResult: Result<Void?>? = null

            addSource(fypActivityViewModel.updateFypRoleResult) { result ->
                fypActivityUpdateResult = result
                value = Pair(fypActivityUpdateResult, teacherUpdateResult)
            }

            addSource(teacherViewModel.updateFypRoleResultForTeachers) { result ->
                teacherUpdateResult = result
                value = Pair(fypActivityUpdateResult, teacherUpdateResult)
            }
        }

        bothResultsObserver.observe(this) { (fypActivityResult, teacherResult) ->
            if (fypActivityResult != null && teacherResult != null) {
                when {
                    fypActivityResult is Result.Success && teacherResult is Result.Success -> {
                        hideProgressDialog()
                        showActionSuccessDialog(this, "Roles updated successfully")

                        // reset values
                        actionChangeRole_Role = null
                        actionChangeRole_NewTeacherId = null
                    }
                    fypActivityResult is Result.Failure && teacherResult is Result.Failure -> {
                        hideProgressDialog()

//                        val fypActivityErrorMessage = (fypActivityResult as? Result.Failure)?.exception?.message
//                        val teacherErrorMessage = (teacherResult as? Result.Failure)?.exception?.message

//                        val errorMessage = buildString {
//                            append("Failed to update roles. ")
//                            fypActivityErrorMessage?.let { append("FYP Activity Error: $it. ") }
//                            teacherErrorMessage?.let { append("Teacher Error: $it.") }
//                        }

                        showReversibleActionFailedDialog(this, "Update operations failed. Try again",
                            onRetry = {
                                Toast.makeText(this, "Retrying", Toast.LENGTH_SHORT).show()
                                updateFypRoles(actionChangeRole_NewTeacherId!!, actionChangeRole_Role!!)
                        }, onCancel = {
                            finish()
                        })

//                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        hideProgressDialog()
                        val partialSuccessMessage = "Updates partially executed, rolling back"
                        Toast.makeText(this, partialSuccessMessage, Toast.LENGTH_SHORT).show()

                        rollbackChanges(fypActivityResult, teacherResult)
                    }
                }
            }
        }
    }




    private fun rollbackChanges(fypActivityResult: Result<Void?>?, teacherResult: Result<Void?>?) {


//        // rollBack successful changes
        if (fypActivityResult is Result.Success && teacherResult is Result.Failure) {

            fypActivityViewModel.rollbackFypRoleUpdate(actionChangeRole_ActivityId!!, actionChangeRole_CurrentTeacherId!!, actionChangeRole_Role!!)
        }

        if (teacherResult is Result.Success && fypActivityResult is Result.Failure) {
            teacherViewModel.rollbackTeacherRoleUpdate(actionChangeRole_CurrentTeacherId!!, actionChangeRole_NewTeacherId!!, FypActivityRole(actionChangeRole_ActivityId!!, actionChangeRole_Role!!))
        }

    }
}