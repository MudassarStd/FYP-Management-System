package com.android.cuifypmanagementsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.room.Batch
import com.android.cuifypmanagementsystem.viewmodel.repository.BatchRepository
import kotlinx.coroutines.launch

class BatchViewModel(app : Application) : AndroidViewModel(app) {
    private val repo : BatchRepository by lazy { BatchRepository(app) }

    private var listBatches : List<Batch> = listOf()
    private val _batches = MutableLiveData<List<Batch>>()
    val batches : LiveData<List<Batch>> get() = _batches


    fun insert(batch : Batch)
    {
        viewModelScope.launch {
            repo.insert(batch)
        }
    }
    fun delete(batch : Batch)
    {
        viewModelScope.launch {
            repo.delete(batch)
            getAllBatches() // refreshing
        }
    }

    fun getAllBatches(){
        viewModelScope.launch {
            listBatches = repo.getAllBatches()
            _batches.postValue(listBatches)
        }
    }


    // return false if batch already exists
    fun validateBatch(batchName: String, semester: Int): Boolean {
        return !listBatches.any { it.name == batchName || it.semester == semester }
    }



}
