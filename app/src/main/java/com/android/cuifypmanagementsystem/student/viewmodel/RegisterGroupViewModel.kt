package com.android.cuifypmanagementsystem.student.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.student.datamodel.GroupMember
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.student.repository.StudentGroupRepository
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterGroupViewModel @Inject constructor(
    private val studentGroupRepository: StudentGroupRepository
) : ViewModel() {

    // LiveData to keep track of selected group members
    private val _groupMembers = MutableLiveData<MutableList<GroupMember>>(mutableListOf())
    val groupMembers: LiveData<MutableList<GroupMember>> = _groupMembers

    private val _availableStudents = MutableLiveData<Result<List<Student>>>()
    val availableStudents: LiveData<Result<List<Student>>> = _availableStudents

    private val _filterParameters = MutableLiveData<FilterParameters>()
    val filterParameters: LiveData<FilterParameters> = _filterParameters


    fun fetchAllAvailableStudents(department: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _availableStudents.postValue(studentGroupRepository.fetchAllAvailableStudents( department))
        }
    }

    // Adds a member to the group and posts the updated list
    fun addMember(member: GroupMember) {
        _groupMembers.value?.let {
            if (!it.contains(member)) {
                it.add(member)
                _groupMembers.postValue(it) // Notify observers of change
            }
        }
    }

    // Removes a member from the group and posts the updated list
    fun removeMember(member: GroupMember) {
        _groupMembers.value?.let {
            it.remove(member)
            _groupMembers.postValue(it) // Notify observers of change
        }
    }

    // Clears all selected members and posts the updated list (Optional)
    fun clearMembers() {
        _groupMembers.value?.let {
            it.clear()
            _groupMembers.postValue(it) // Notify observers of change
        }
    }

    // members filtration
    fun setMemberFilterParameters(email : String, department : String) {
        _filterParameters.value = FilterParameters(email, department)
    }

    data class FilterParameters(
        val currentEmail: String = "",
        val department: String = ""
    )
}
