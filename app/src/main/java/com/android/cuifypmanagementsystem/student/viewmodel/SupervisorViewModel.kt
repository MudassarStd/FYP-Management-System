package com.android.cuifypmanagementsystem.student.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.student.repository.MainRepository
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupervisorViewModel@Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _supervisors = MutableLiveData<Result<List<Teacher>>>()
    val supervisors : LiveData<Result<List<Teacher>>> get() = _supervisors



    fun fetchSupervisors() {
        viewModelScope.launch(Dispatchers.IO) {
            _supervisors.postValue(mainRepository.fetchSupervisors())
        }
    }

    fun requestSupervisor(groupId : String?, groupBatch: String?, teacherId : String?) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.requestSupervisor(groupId, groupBatch, teacherId)
        }
    }
}