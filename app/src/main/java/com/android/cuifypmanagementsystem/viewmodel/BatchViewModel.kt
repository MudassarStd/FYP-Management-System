package com.android.cuifypmanagementsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.repository.BatchRepository
import kotlinx.coroutines.launch

class BatchViewModel(app : Application) : AndroidViewModel(app) {
    private val repo : BatchRepository by lazy { BatchRepository(app) }



    private var listBatches : List<Batch> = listOf()
    private val _batches = MutableLiveData<List<Batch>>()
    val batches : LiveData<List<Batch>> get() = _batches

    // handling batch by Id
    private val _batch = MutableLiveData<Batch>()
    val batch: LiveData<Batch> get() = _batch


    fun insert(batch : Batch)
    {
        viewModelScope.launch {
            repo.insert(batch)
        }
    }

    fun update(batch : Batch)
    {
        viewModelScope.launch {
            repo.update(batch)
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

    fun getBatchById(id : Int){
        viewModelScope.launch {
            _batch.postValue(repo.getBatchById(id))
        }
    }

    // return false if batch already exists
    fun validateBatch(batchName: String, semester: Int): Boolean {
        return !listBatches.any { it.name == batchName || it.semester == semester }
    }

    fun validateBatchForEditing(batchName: String, semester: Int): Boolean {
        return !listBatches.any { it.name == batchName && it.semester == semester }
    }
}
