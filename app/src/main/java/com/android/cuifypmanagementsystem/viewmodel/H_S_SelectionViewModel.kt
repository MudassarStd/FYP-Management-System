package com.android.cuifypmanagementsystem.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class H_S_SelectionViewModel() : ViewModel() {
    init {
        Log.d("H_S_SelectionViewModelInstanceTesting", "ViewModel instance created: ${this.hashCode()}")
    }

    // Existing LiveData and methods
    val selectedHead = MutableLiveData<String>()
    val selectedSecretory = MutableLiveData<String>()

    fun setSelectedHead(head: String) {
        selectedHead.value = head
        Log.d("H_S_SelectionViewModelInstanceTesting", "Selected head set: $head, ViewModel instance: ${this.hashCode()}")
    }

    fun setSelectedSecretory(secretory: String) {
        selectedSecretory.value = secretory
        Log.d("H_S_SelectionViewModelInstanceTesting", "Selected secretory set: $secretory, ViewModel instance: ${this.hashCode()}")
    }
}

