package com.android.cuifypmanagementsystem.teacher.activities

import CustomDialogHelper.showActionSuccessDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityShareFypIdeaBinding
import com.android.cuifypmanagementsystem.datamodels.FypIdea
import com.android.cuifypmanagementsystem.teacher.viewmodel.GeneralViewModel
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ShareFypIdeaActivity : AppCompatActivity() {

    private val binding by lazy { ActivityShareFypIdeaBinding.inflate(layoutInflater) }
    private val generalViewModel: GeneralViewModel by viewModels()
    private var linkCount = 1
    private var showAuthorIdentity = false

    private var teacherName : String? = null
    private var category : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#576AE0")


        setupWindowInsets()
        setupToolbar()
        collectTeacherName()
        setupListeners()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        binding.toolbarShareFypIdea.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun collectTeacherName() {
        teacherName = intent.getStringExtra("teacherName")
    }

    private fun setupListeners() {
        binding.btnAddLink.setOnClickListener { addLinkField() }
        binding.btnPublishIdea.setOnClickListener { publishIdea() }
        binding.etFypIdeaCategory.setOnClickListener { showTechCategory() }
    }

    private fun publishIdea() {
        collectFypIdea()?.let {
            generalViewModel.addFypIdea(it)
            observeResult()
        }
    }

    private fun observeResult() {
        generalViewModel.fypIdeaAdditionStatus.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    showActionSuccessDialog(this, "Idea successfully published")
                    resetFields()
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    showToast("Idea could not be published")
                }
                is Result.Loading -> {
                    showProgressDialog("Publishing Idea...", this)
                }
            }
        }
    }

    private fun resetFields() {
        binding.etFypIdeaTitle.text!!.clear()
        binding.etFypIdeaDescription.text.clear()
        binding.linksContainer.removeAllViews()
        linkCount = 1
        binding.cbHideIdentity.isChecked = false
    }

    private fun collectFypIdea(): FypIdea? {
        val title = binding.etFypIdeaTitle.text.toString().trim()
        val description = binding.etFypIdeaDescription.text.toString().trim()
        val links = getLinks()
        val category = binding.etFypIdeaCategory.text.toString()
        showAuthorIdentity = !binding.cbHideIdentity.isChecked

        return if (title.isEmpty() || description.isEmpty()) {
            showToast("Title or Description cannot be empty")
            null
        } else {
            FypIdea(
                firestoreId = null,
                title = title,
                description = description,
                links = links,
                ideaTaken = false,
                author = if (showAuthorIdentity) teacherName else "Faculty",
                category = category,
                dateTime = System.currentTimeMillis()
            )
        }
    }

    private fun getLinks(): List<String> {
        return (0 until binding.linksContainer.childCount)
            .map { binding.linksContainer.getChildAt(it) as EditText }
            .mapNotNull { it.text.toString().trim().takeIf(String::isNotEmpty) }
    }

    private fun addLinkField() {
        val linkEditText = EditText(this).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(R.dimen.link_et_height) // 48dp height
            ).apply {
                setMargins(0, 20, 0, 0)
            }
            hint = "Link $linkCount"
            setPadding(20, 0, 20, 0)
            setTextColor(ContextCompat.getColor(this@ShareFypIdeaActivity, R.color.black))
            setBackgroundResource(R.drawable.rounded_corners_white_background)
            elevation = 2f
            inputType = InputType.TYPE_TEXT_VARIATION_URI
        }

        binding.linksContainer.addView(linkEditText)
        linkCount++
    }

    private fun showTechCategory() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Category")
            .setItems(arrayOf("Artificial Intelligence", "Software Development", "Research Article")) { _, which ->
                binding.etFypIdeaCategory.setText( when (which) {
                    0 -> "Artificial Intelligence"
                    1 -> "Software Development"
                    2 -> "Research Article"
                    else -> "Other"
                })
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
