package com.android.cuifypmanagementsystem.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DepartmentViewModel : ViewModel() {
    val _selectedDepartments = MutableLiveData<List<String>>(mutableListOf())
    val selectedDepartments: LiveData<List<String>> get()  = _selectedDepartments

    fun selected(list : List<String>)
    {
        _selectedDepartments.postValue(list)
    }
}
