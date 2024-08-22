package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.repository.BatchRepository
import com.android.cuifypmanagementsystem.repository.TeacherRepository

class BatchViewModelFactory(private val batchRepository: BatchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BatchViewModel(batchRepository) as T
    }
}