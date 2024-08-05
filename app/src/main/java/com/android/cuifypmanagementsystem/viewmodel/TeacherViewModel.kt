package com.android.cuifypmanagementsystem.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.GLOBAL_TESTING_TAG
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherViewModel(private val teacherRepository: TeacherRepository) : ViewModel() {

    val teachers : LiveData<List<Teacher>> get() = teacherRepository.teachers

    private val _teachersFromCloud = MutableLiveData<Result<List<Teacher>>>()
    val teachersFromCloud : LiveData<Result<List<Teacher>>> get() = _teachersFromCloud


    private var _teacherRegistrationResult = MutableLiveData<Result<Void?>>()
    val teacherRegistrationResult : LiveData<Result<Void?>> get() = _teacherRegistrationResult

    init {
        getAllTeachersFromCloud()
//       viewModelScope.launch {
//           teacherRepository.getAllFromRoom()
//       }
    }

    fun registerTeacher(teacher: Teacher){
        _teacherRegistrationResult.value = Result.Loading
        viewModelScope.launch {
           _teacherRegistrationResult.value = teacherRepository.registerTeacher(teacher)
        }
    }

    private fun getAllTeachersFromCloud(){
        _teachersFromCloud.value = Result.Loading
        viewModelScope.launch {
            _teachersFromCloud.value = teacherRepository.getAllTeachersFromCloud()
        }
    }


            // code for manage teachers




//    fun addTeacher(teacher: Teacher)
//    {
//        viewModelScope.launch(Dispatchers.IO) {
//            teacherRepository.addTeacherLocally(teacher)
//        }
//    }

    fun updateTeacher(teacher: Teacher)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.updateTeacher(teacher)
        }
    }

    fun deleteTeacherRecord(uid : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.deleteTeacherRecord(uid)
        }
    }



}