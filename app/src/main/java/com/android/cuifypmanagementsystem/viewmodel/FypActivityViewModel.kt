package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.repository.FypActivityRepository
import com.android.cuifypmanagementsystem.room.datamodels.FypActivity
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.launch

class FypActivityViewModel(private val fypActivityRepository: FypActivityRepository) : ViewModel(){

    private val _fypActivitiesFetch = MutableLiveData<Result<List<FypActivity>>>()
    val fypActivitiesFetch : LiveData<Result<List<FypActivity>>> get() = _fypActivitiesFetch

    private val _fypActivityStartStatus = MutableLiveData<Result<Void?>>()
    val fypActivityStartStatus : LiveData<Result<Void?>> get() = _fypActivityStartStatus

    init {
        _fypActivitiesFetch.value = Result.Loading
        viewModelScope.launch {
            _fypActivitiesFetch.value = fypActivityRepository.fetchFypActivityData()
        }
    }


    fun startFypActivity(fypActivityRecord : FypActivity){
        _fypActivityStartStatus.value = Result.Loading
        viewModelScope.launch {
            _fypActivityStartStatus.value = fypActivityRepository.startFypActivity(fypActivityRecord)
        }
    }


}