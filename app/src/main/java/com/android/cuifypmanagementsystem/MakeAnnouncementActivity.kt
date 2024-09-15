package com.android.cuifypmanagementsystem

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.admin.viewmodel.AnnouncementViewModel
import com.android.cuifypmanagementsystem.databinding.ActivityMakeAnnouncementBinding
import com.android.cuifypmanagementsystem.datamodels.Announcement
import com.android.cuifypmanagementsystem.utils.Constants.ANNOUNCEMENT_RECIPIENT_STUDENT
import com.android.cuifypmanagementsystem.utils.Constants.ANNOUNCEMENT_RECIPIENT_TEACHER
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeAnnouncementActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMakeAnnouncementBinding.inflate(layoutInflater)}
    private val announcementViewModel : AnnouncementViewModel by viewModels()

    private var audience : String? = null
    private var priority : Boolean? = false

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
        binding.toolbarMakeAnnouncement.setNavigationOnClickListener { onBackPressed() }

        getIntentAction()
        binding.btnPublishAnnouncement.setOnClickListener {
            val announcement = getAnnouncement()
            announcement?.let{
                announcementViewModel.publishAnnouncement(it)
                observePublishResults()
            } ?: showToast("Data required!!")
        }
    }

    private fun observePublishResults() {
        announcementViewModel.publishResult.observe(this) {result->
            when (result) {
                is Result.Loading -> {
                    showProgressDialog("Publishing...", this)
                }
                is Result.Success -> {
                    hideProgressDialog()
                    showToast("Published Successfully")
                    resetFields()
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    showToast("Error: ${result.exception.message}")
                }
            }
        }
    }

    private fun getIntentAction() {
        audience = when(intent.action) {
            ANNOUNCEMENT_RECIPIENT_TEACHER -> "teacher"
            ANNOUNCEMENT_RECIPIENT_STUDENT -> "student"
            else -> null
        }
    }

    private fun getAnnouncement() : Announcement? {
        val title = binding.etAnnouncementTitle.text.toString()
        val description = binding.etAnnouncementDescription.text.toString()
        getPriority()

        return if (title.isNotEmpty() && description.isNotEmpty() && audience != null && priority != null) {
            Announcement(null, title,description, System.currentTimeMillis(), "admin", audience!!, priority!!)
        } else {
            null
        }
    }

    private fun getPriority() {
        binding.radioGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            priority = when (checkedId) {
                R.id.rbLow -> false
                R.id.rbHigh -> true
                else -> null // Default or handle other cases
            }
        }
    }

    private fun resetFields() {
        binding.etAnnouncementTitle.setText("")
        binding.etAnnouncementDescription.setText("")
        binding.etAnnouncementTitle.clearFocus()
        binding.etAnnouncementDescription.clearFocus()
    }

    private fun showToast(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}