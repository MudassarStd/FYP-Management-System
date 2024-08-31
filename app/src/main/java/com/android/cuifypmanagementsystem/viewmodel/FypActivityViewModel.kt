package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.repository.FypActivityRepository
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecordUiModel
import com.android.cuifypmanagementsystem.repository.BatchRepository
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FypActivityViewModel @Inject constructor(
    private val fypActivityRepository: FypActivityRepository,
    private val teacherRepository: TeacherRepository,
    private val batchRepository: BatchRepository)
    : ViewModel(){

    private val _fypActivitiesFetch = MutableLiveData<Result<List<FypActivityRecordUiModel>>>()
    val fypActivitiesFetch : LiveData<Result<List<FypActivityRecordUiModel>>> get() = _fypActivitiesFetch

    private val _fypActivityStartStatus = MutableLiveData<Result<String>>()
    val fypActivityStartStatus : LiveData<Result<String>> get() = _fypActivityStartStatus

    private val _fypActivityInfo = MutableLiveData<FypActivityRecord>()
    val fypActivityInfo : LiveData<FypActivityRecord> get() = _fypActivityInfo

    private val _updateFypRoleResult = MutableLiveData<Result<Void?>>()
    val updateFypRoleResult : LiveData<Result<Void?>> get() = _updateFypRoleResult

    private val _activityClosureState = MutableLiveData<Result<Void?>>()
    val activityClosureState : LiveData<Result<Void?>> get() = _activityClosureState



    init {
    }

    fun fetchFypActivityDataWithCustomUIModel (activityStatus : Boolean) {
        _fypActivitiesFetch.value = Result.Loading
        viewModelScope.launch {
            val result = fypActivityRepository.fetchFypActivityData(activityStatus)
            if(result is Result.Success) {
                val UiModelList = result.data.map { activity ->
                    val fypHeadName = teacherRepository.getTeacherNameById(activity.fypHeadId)
                    val fypSecretoryName = teacherRepository.getTeacherNameById(activity.fypSecId)
                    val batchName = batchRepository.getBatchNameById(activity.batchId)
                    FypActivityRecordUiModel(activity, fypHeadName, fypSecretoryName, batchName)
                }
                _fypActivitiesFetch.postValue(Result.Success(UiModelList))
            } else {
                _fypActivitiesFetch.value = result as Result.Failure
            }
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

    fun rollbackFypRoleUpdate(activityId: String, currentTeacherId: String, role: String) {
        viewModelScope.launch {
            fypActivityRepository.rollbackFypRoleUpdate(activityId, currentTeacherId, role)
        }
    }

    fun closeActivity(activityId : String) {
        viewModelScope.launch {
            _activityClosureState.value = fypActivityRepository.closeActivity(activityId)
        }
    }


}