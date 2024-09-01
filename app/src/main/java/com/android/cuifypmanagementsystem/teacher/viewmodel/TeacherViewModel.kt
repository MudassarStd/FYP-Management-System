package com.android.cuifypmanagementsystem.teacher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.teacher.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.android.cuifypmanagementsystem.utils.Result
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository
) : ViewModel() {

    private val _teacher = MutableLiveData<Result<Teacher>>()
    val teacher: LiveData<Result<Teacher>> = _teacher

    private val _updateProfileResult = MutableLiveData<Result<Void?>>()
    val updateProfileResult: LiveData<Result<Void?>> = _updateProfileResult

    fun getTeacherById(teacherId: String) {
        _teacher.value = Result.Loading
        viewModelScope.launch {
            val result = teacherRepository.getTeacherById(teacherId)
            _teacher.value = result
        }
    }

    fun updateTeacher(teacher: Teacher) {
        _updateProfileResult.value = Result.Loading
        viewModelScope.launch {
            val result = teacherRepository.updateTeacher(teacher)
            _updateProfileResult.value = result
        }
    }

}