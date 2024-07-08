package com.android.cuifypmanagementsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.cuifypmanagementsystem.repository.TeacherRepository

class TeacherViewModel(app : Application) : AndroidViewModel(app) {
    private val repo = TeacherRepository(app)
}