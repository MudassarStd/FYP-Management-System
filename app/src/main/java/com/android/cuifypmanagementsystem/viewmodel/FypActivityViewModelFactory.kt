package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.repository.FypActivityRepository
import com.android.cuifypmanagementsystem.repository.TeacherRepository

class FypActivityViewModelFactory(private val fypActivityRepository: FypActivityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FypActivityViewModel(fypActivityRepository) as T
    }
}