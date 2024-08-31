package com.android.cuifypmanagementsystem.teacher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.cuifypmanagementsystem.utils.Result
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.FypIdea
import com.android.cuifypmanagementsystem.teacher.repository.GeneralRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val generalRepository: GeneralRepository
) : ViewModel() {


    private val _fypIdeaAdditionStatus = MutableLiveData<Result<Void?>>()
    val fypIdeaAdditionStatus: LiveData<Result<Void?>> get() = _fypIdeaAdditionStatus

    private val _fypIdeas = MutableLiveData<Result<List<FypIdea>>>()
    val fypIdeas: LiveData<Result<List<FypIdea>>> get() = _fypIdeas


    fun addFypIdea(fypIdea : FypIdea) {
        _fypIdeaAdditionStatus.value = Result.Loading
        viewModelScope.launch {
            _fypIdeaAdditionStatus.value = generalRepository.addFypIdea(fypIdea)
        }
    }

    fun fetchFypIdeas() {
        _fypIdeas.value = Result.Loading
        viewModelScope.launch {
            _fypIdeas.value = generalRepository.getFypIdeas()
        }
    }
}