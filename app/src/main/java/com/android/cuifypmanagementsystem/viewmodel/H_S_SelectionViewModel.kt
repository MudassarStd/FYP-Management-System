package com.android.cuifypmanagementsystem.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.datamodels.Teacher

class H_S_SelectionViewModel() : ViewModel() {
    init {
        Log.d("H_S_SelectionViewModelInstanceTesting", "ViewModel instance created: ${this.hashCode()}")
    }

    // Existing LiveData and methods
    val selectedHead = MutableLiveData<Teacher?>()
    val selectedSecretory = MutableLiveData<Teacher?>()
    val selectedBatch = MutableLiveData<Batch?>()

    fun setSelectedHead(teacher: Teacher?) {
        selectedHead.value = teacher
        Log.d("H_S_SelectionViewModelInstanceTesting", "Selected head set: $teacher, ViewModel instance: ${this.hashCode()}")
    }

    fun setSelectedSecretory(teacher: Teacher?) {
        selectedSecretory.value = teacher
        Log.d("H_S_SelectionViewModelInstanceTesting", "Selected secretory set: $teacher, ViewModel instance: ${this.hashCode()}")
    }

    fun setSelectedBatch(batch: Batch?) {
        selectedBatch.value = batch
        Log.d("H_S_SelectionViewModelInstanceTesting", "Selected secretory set: $batch, ViewModel instance: ${this.hashCode()}")
    }
}

