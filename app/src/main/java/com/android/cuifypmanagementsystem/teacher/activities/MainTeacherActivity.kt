package com.android.cuifypmanagementsystem.teacher.activities

import CustomDialogHelper.showAlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.ChangePasswordActivity
import com.android.cuifypmanagementsystem.LoginActivity
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityMainTeacherBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.teacher.AnnouncementActivity
import com.android.cuifypmanagementsystem.teacher.activities.mygroups.MyGroupsActivity
import com.android.cuifypmanagementsystem.teacher.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.GlobalSharedViewModel
import com.android.cuifypmanagementsystem.auth.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainTeacherBinding
    private val teacherViewModel : TeacherViewModel by viewModels()
    private val userAuthViewModel : UserAuthViewModel by viewModels()
    private lateinit var globalSharedViewModel: GlobalSharedViewModel

    private var isCreatedFirstTime : Boolean = true

    private var teacher : Teacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        globalSharedViewModel = (application as BaseApplication).getGlobalSharedViewModel()
        getTeacherIdFromIntent()
        handleNavigation()
        handleAlerts()
    }



    override fun onResume() {
        super.onResume()
        if (!isCreatedFirstTime){
            teacher = globalSharedViewModel.getTeacher()
            Log.d("TesingTeacherUpdateError", "Onresume: ${globalSharedViewModel.getTeacher()}")
            updateUI()
        } else {
            isCreatedFirstTime = false
        }

    }

    private fun handleNavigation() {
        binding.boxShareFypIdea.setOnClickListener {
            val intent = Intent(this, ShareFypIdeaActivity::class.java).apply {
                putExtra("teacherName", teacher!!.name)
            }
            startActivity(intent)
        }

        binding.boxViewFypIdeas.setOnClickListener {
            startActivity(Intent(this, DisplayFypIdeasActivity::class.java))
        }

        binding.tvMyGroups.setOnClickListener {
            startActivity(Intent(this, MyGroupsActivity::class.java))
        }

        binding.tvTakeStudentGroups.setOnClickListener {
            startActivity(Intent(this, StudentGroupsActivity::class.java))
        }

        binding.tvAnnouncements.setOnClickListener {
            startActivity(Intent(this, AnnouncementActivity::class.java))
        }

        binding.tvMyProfile.setOnClickListener {
            startActivity(Intent(this, MyProfileActivity::class.java))
        }

        binding.tvChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.tvLogout.setOnClickListener {
            userAuthViewModel.userLogout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleAlerts() {
        binding.tvFypRolesHeader.setOnClickListener {
            showAlertDialog(this,
                "You have selected as ${teacher!!.fypActivityRole!!.activityRole} for FYP activity." +
                        "\nFollowing has options to translate to your authorities.\n\nGood luck!")
        }
    }

    private fun getTeacherIdFromIntent() {
        val teacherId = intent.getStringExtra("userId")
        teacherViewModel.getTeacherById(teacherId!!)

        teacherViewModel.teacher.observe(this) {result->
            when(result) {
                is Result.Success -> {
                    // Update UI with the teacher data
                    teacher = result.data
                    teacher!!.firestoreId = teacherId
                    globalSharedViewModel.setTeacher(teacher)
                    updateUI()
                }
                is Result.Failure -> {
                    binding.tvScreenDataFetchError.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.VISIBLE
                    binding.pbTeacherDashboard.visibility = View.GONE
//                    Toast.makeText(this, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()

                    binding.btnRetry.setOnClickListener { retry() }
                }
                is Result.Loading -> {
                    // Show a loading indicator if needed
                }
            }
        }
    }

    private fun retry() {
        getTeacherIdFromIntent()
        binding.pbTeacherDashboard.visibility = View.VISIBLE
        binding.tvScreenDataFetchError.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
    }

    private fun updateUI() {
        teacher?.let {
            if (it.fypHeadOrSecretory == 1) {
                binding.fypRolesSection.visibility = View.VISIBLE
            }
            binding.tvTeacherName.text = it.name
            binding.pbTeacherDashboard.visibility =  View.GONE
            binding.svTeacherDashboard.visibility = View.VISIBLE
        }
    }
}
