package com.android.cuifypmanagementsystem.teacher.activities.mygroups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.student.repository.StudentGroupRepository
import com.android.cuifypmanagementsystem.teacher.datamodel.GroupDisplayModel
import com.android.cuifypmanagementsystem.teacher.repository.GroupsRepository
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupsViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val studentGroupRepository: StudentGroupRepository
) : ViewModel() {

    private lateinit var teacherId : String

    private var _groups = MutableLiveData<Result<List<GroupDisplayModel>>>(Result.Loading)
    val groups : LiveData<Result<List<GroupDisplayModel>>> get() = _groups

    private var _requests = MutableLiveData<Result<List<GroupDisplayModel>>>(Result.Loading)
    val requests : LiveData<Result<List<GroupDisplayModel>>> get() = _requests

    private var _groupAdditionToSupervisionStatus = MutableLiveData<Boolean>()
    val groupAdditionToSupervisionStatus : LiveData<Boolean> get() = _groupAdditionToSupervisionStatus



    fun fetchGroups(groupDataType: GroupDataType) {
        viewModelScope.launch(Dispatchers.IO) {
            if (groupDataType == GroupDataType.GROUPS) {
                _groups.postValue(groupsRepository.fetchGroups(teacherId, groupDataType))
            } else {
                _requests.postValue(groupsRepository.fetchGroups(teacherId, groupDataType))
            }
        }
    }



    fun addGroupToSupervision(groupId: String, batch : String) {
        viewModelScope.launch(Dispatchers.IO) {
            _groupAdditionToSupervisionStatus.postValue(groupsRepository.addGroupToSupervision(groupId, teacherId, batch))
            // also pass supervisor Id to Student Group
            studentGroupRepository.updateSupervisor(teacherId = teacherId, groupId = groupId)
        }
    }

    fun rejectGroupforSupervision(groupId: String) {

    }

    fun setTeacherId(id : String) {
        teacherId = id
    }

}