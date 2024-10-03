package com.android.cuifypmanagementsystem.student.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.student.datamodel.Group
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.student.repository.StudentGroupRepository
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentGroupViewModel @Inject constructor(
    private val studentGroupRepository: StudentGroupRepository
) : ViewModel() {

    // MutableLiveData to hold the result of the group registration
    private val _registerGroupResult = MutableLiveData<Result<Unit>>(Result.Loading)
    val registerGroupResult: LiveData<Result<Unit>> = _registerGroupResult

    private val _fetchGroupResult = MutableLiveData<Result<Group?>>()
    val fetchGroupResult: LiveData<Result<Group?>> = _fetchGroupResult

    private val _student = MutableLiveData<Student?>()
    val student: LiveData<Student?> = _student

    fun registerGroup(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = studentGroupRepository.registerGroup(group)
            _registerGroupResult.postValue(result)
        }
    }

    fun fetchMyGroup(uid: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = studentGroupRepository.fetchMyGroup(uid)
            _fetchGroupResult.postValue(result)
        }
    }

    fun getStudentById(studentId: String) {
        viewModelScope.launch {
            _student.value = studentGroupRepository.getStudentById(studentId)
        }
    }
}