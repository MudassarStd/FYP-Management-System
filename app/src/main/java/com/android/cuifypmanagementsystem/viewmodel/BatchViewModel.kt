package com.android.cuifypmanagementsystem.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.repository.BatchRepository
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.launch

class BatchViewModel(private val batchRepository: BatchRepository) : ViewModel() {


    private var listBatches : List<Batch> = listOf()

    private val _batches = MutableLiveData<List<Batch>>()
    val batches : LiveData<List<Batch>> get() = _batches

    private val _batchesFromCloud = MutableLiveData<Result<List<Batch>>>()
    val batchesFromCloud : LiveData<Result<List<Batch>>> get() = _batchesFromCloud

    // handling batch by Id
    private val _batch = MutableLiveData<Batch>()
    val batch: LiveData<Batch> get() = _batch


    init {
        Log.d("TestingBatchLogic", "Inside batch VM  init")
        fetchAllBatches()
    }


    // cloud operations
    fun addBatch(batch : Batch){
        viewModelScope.launch {
            batchRepository.addBatch(batch)
        }
    }

    fun fetchAllBatches() {
//        _batchesFromCloud.value = Result.Loading
        viewModelScope.launch {
            _batchesFromCloud.value = batchRepository.fetchAllBatches()
        }
    }


//    fun fetchAvailableBatchesForActivity() {
////        _batchesFromCloud.value = Result.Loading
//        viewModelScope.launch {
//            _batchesFromCloud.value = batchRepository.fetchAvailableBatchesForActivity()
//        }
//    }

    fun updateBatchActivityStatus(batchId : String ,status : Boolean){
        viewModelScope.launch {
            batchRepository.updateBatchActivityStatus(batchId, status)
        }
    }






    // ---------------------- Room operations ----------------------


    fun insert(batch : Batch)
    {
        viewModelScope.launch {
            batchRepository.insert(batch)
        }
    }

    fun update(batch : Batch)
    {
        viewModelScope.launch {
            batchRepository.update(batch)
        }
    }
    fun delete(batch : Batch)
    {
        viewModelScope.launch {
            batchRepository.delete(batch)
            getAllBatches() // refreshing
        }
    }

    fun getAllBatches(){
        viewModelScope.launch {
            listBatches = batchRepository.getAllBatches()
            _batches.postValue(listBatches)
        }
    }

    fun getBatchById(id : Int){
        viewModelScope.launch {
            _batch.postValue(batchRepository.getBatchById(id))
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
