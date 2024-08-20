package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.repository.FypActivityRepository
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FypActivityViewModel(private val fypActivityRepository: FypActivityRepository) : ViewModel(){

    private val _fypActivitiesFetch = MutableLiveData<Result<List<FypActivityRecord>>>()
    val fypActivitiesFetch : LiveData<Result<List<FypActivityRecord>>> get() = _fypActivitiesFetch

    private val _fypActivityStartStatus = MutableLiveData<Result<String>>()
    val fypActivityStartStatus : LiveData<Result<String>> get() = _fypActivityStartStatus

    private val _fypActivityInfo = MutableLiveData<FypActivityRecord>()
    val fypActivityInfo : LiveData<FypActivityRecord> get() = _fypActivityInfo


    private val _updateFypRoleResult = MutableLiveData<Result<Void?>>()
    val updateFypRoleResult : LiveData<Result<Void?>> get() = _updateFypRoleResult

    init {
        _fypActivitiesFetch.value = Result.Loading
        viewModelScope.launch {
            _fypActivitiesFetch.value = fypActivityRepository.fetchFypActivityData()
        }
    }

    fun startFypActivity(fypActivityRecord : FypActivityRecord){
        _fypActivityStartStatus.value = Result.Loading
        viewModelScope.launch {
            _fypActivityStartStatus.value = fypActivityRepository.startFypActivity(fypActivityRecord)
        }
    }

    fun deleteFypActivity(activityId : String){
        viewModelScope.launch(Dispatchers.IO) {
            fypActivityRepository.deleteFypActivity(activityId)
        }
    }

    fun getActivityInfo(activityId : String){
        viewModelScope.launch() {
            _fypActivityInfo.value = fypActivityRepository.getActivityInfo(activityId)
        }
    }

    fun updateFypRole(activityId: String, newTeacherId: String, role: String) {
        viewModelScope.launch {
            _updateFypRoleResult.value = fypActivityRepository.updateFypRole(activityId, newTeacherId, role)
        }
    }

    fun rollbackFypRoleUpdate(actionchangeroleActivityid: String) {

    }
}