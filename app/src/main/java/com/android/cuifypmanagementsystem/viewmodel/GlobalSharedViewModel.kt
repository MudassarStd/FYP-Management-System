package com.android.cuifypmanagementsystem.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.datamodels.Teacher


class GlobalSharedViewModel() : ViewModel() {
    init {
        Log.d("H_S_SelectionViewModelInstanceTesting", "ViewModel instance created: ${this.hashCode()}")
    }

    private val _activityUpdateTriggered = MutableLiveData<Boolean>()
    val activityUpdateTriggered : LiveData<Boolean> get() = _activityUpdateTriggered


    // Existing LiveData and methods
    val selectedHead = MutableLiveData<Teacher?>()
    val selectedSecretory = MutableLiveData<Teacher?>()
    val selectedBatch = MutableLiveData<Batch?>()

    fun setSelectedHead(teacher: Teacher?) {
        selectedHead.value = teacher
    }

    fun setSelectedSecretory(teacher: Teacher?) {
        selectedSecretory.value = teacher
    }

    fun setSelectedBatch(batch: Batch?) {
        selectedBatch.value = batch
    }


    // ------------- AllFypActivity Fragments communication -------------------
    fun informActivityUpdate(status : Boolean) {
        _activityUpdateTriggered.value = status
    }
}

