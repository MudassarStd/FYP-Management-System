package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.repository.TeacherRepository

class TeacherViewModelFactory(private val teacherRepository: TeacherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TeacherViewModel(teacherRepository) as T
    }
}