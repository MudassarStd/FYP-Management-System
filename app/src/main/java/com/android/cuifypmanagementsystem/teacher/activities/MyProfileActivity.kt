package com.android.cuifypmanagementsystem.teacher.activities

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityMyProfileBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.teacher.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.GlobalSharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MyProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyProfileBinding.inflate(layoutInflater) }
    private lateinit var globalSharedViewModel: GlobalSharedViewModel
    private val teacherViewModel: TeacherViewModel by viewModels()

    private var teacher: Teacher? = null
    private var cameraImageUri: Uri? = null
    private var selectedImageUri: Uri? = null
    private var tempFile: File? = null

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraImageUri?.let { uri ->
                selectedImageUri = uri
                binding.cvProfilePhoto.setImageURI(selectedImageUri)
                // Optionally, delete the temp file here if needed
                // tempFile?.delete()
            }
        } else {
            showToast("Failed to capture photo")
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            // Delete the previous temp file if it exists
            tempFile?.delete()
            // Reset the tempFile reference
            tempFile = null

            selectedImageUri = uri
            binding.cvProfilePhoto.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        globalSharedViewModel = (application as BaseApplication).getGlobalSharedViewModel()
        teacher = globalSharedViewModel.getTeacher()
        updateUI()
        setupListeners()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#576AE0")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.ivEditProfilePhoto.setOnClickListener {
            showImageSourceDialog()
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
            observeUpdateResults()
        }
    }

    private fun updateProfile() {
        val name = binding.etUserName.text.toString()
        val department = binding.etUserDepartment.text.toString()

        if (name.isNotBlank() && department.isNotBlank()) {
            teacher?.let {
                if (name != it.name || department != it.department) {
                    val updatedTeacher = it.copy(name = name, department = department)
                    teacherViewModel.updateTeacher(updatedTeacher)
                }
            }
        } else {
            showToast("Valid data needed for updating profile")
        }
    }

    private fun observeUpdateResults() {
        teacherViewModel.updateProfileResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showProgressDialog("Updating profile..", this)
                is Result.Success -> {
                    hideProgressDialog()
                    globalSharedViewModel.setTeacher(teacher)
                    showToast("Profile updated successfully")
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    showToast("Could not update profile")
                }
            }
        }
    }

    private fun updateUI() {
        teacher?.let {
            binding.etUserName.setText(it.name)
            binding.etUserEmail.setText(it.email)
            binding.etUserDepartment.setText(it.department)
        }
    }

    private fun showImageSourceDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Take Image From")
            .setItems(arrayOf("Camera", "Gallery")) { _, which ->
                when (which) {
                    0 -> {
                        // Delete the previous temp file if it exists
                        tempFile?.delete()
                        // Create a new temporary file and store its reference
                        tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
                        cameraImageUri = FileProvider.getUriForFile(
                            this,
                            "${packageName}.provider",
                            tempFile!!
                        )
                        cameraImageUri?.let { uri ->
                            takePicture.launch(uri)
                        }
                    }
                    1 -> pickImage.launch("image/*")
                }
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
