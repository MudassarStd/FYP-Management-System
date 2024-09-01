package com.android.cuifypmanagementsystem.teacher.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityDisplayFypIdeasBinding
import com.android.cuifypmanagementsystem.databinding.ActivityShareFypIdeaBinding
import com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.DisplayFypIdeasAdapter
import com.android.cuifypmanagementsystem.teacher.viewmodel.GeneralViewModel
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayFypIdeasActivity : AppCompatActivity() {


    private val binding by lazy { ActivityDisplayFypIdeasBinding.inflate(layoutInflater) }
    private val generalViewModel: GeneralViewModel by viewModels()
    private lateinit var adapter : DisplayFypIdeasAdapter

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

        adapter = DisplayFypIdeasAdapter(emptyList())

        binding.rvDisplayFypIdeas.adapter = adapter
        binding.rvDisplayFypIdeas.layoutManager = LinearLayoutManager(this)


    }

    override fun onResume() {
        super.onResume()
        generalViewModel.fetchFypIdeas()
        observeResults()
    }

    private fun observeResults() {
        generalViewModel.fypIdeas.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    adapter.updateIdeas(result.data)
                    Log.d("TestingDisplayIdeas","data passed to adapter : ${result.data}")
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    Toast.makeText(this,"failed", Toast.LENGTH_SHORT).show()
                    // Handle error
                }
                is Result.Loading -> {
                    showProgressDialog("Fetching Ideas...", this)
                }
            }
    }

}
}
