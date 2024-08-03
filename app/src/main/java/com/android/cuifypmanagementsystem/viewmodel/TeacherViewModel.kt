package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.room.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherViewModel(private val teacherRepository: TeacherRepository) : ViewModel() {

    val teachers : LiveData<List<Teacher>> get() = teacherRepository.teachers


    val registrationResult : LiveData<Result<Void?>> get() = teacherRepository.registrationResult

    init {
       viewModelScope.launch {
           teacherRepository.getAll()
       }
    }

    fun registerTeacher(teacher: Teacher){
        teacherRepository.registerTeacher(teacher)

    }

    fun addTeacher(teacher: Teacher)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.addTeacherLocally(teacher)
        }
    }

    fun updateTeacher(teacher: Teacher)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.updateTeacher(teacher)
        }
    }

    fun deleteTeacher(teacher: Teacher)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.deleteTeacher(teacher)
        }
    }



}