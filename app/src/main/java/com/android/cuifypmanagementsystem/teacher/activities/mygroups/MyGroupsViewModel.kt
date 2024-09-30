package com.android.cuifypmanagementsystem.teacher.activities.mygroups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.Group
import com.android.cuifypmanagementsystem.datamodels.GroupDisplayModel
import com.android.cuifypmanagementsystem.teacher.repository.GroupsRepository
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupsViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository
) : ViewModel() {

    private lateinit var teacherId : String

    private var _groups = MutableLiveData<Result<List<GroupDisplayModel>>>()
    val groups : LiveData<Result<List<GroupDisplayModel>>> get() = _groups

    fun fetchGroups(groupDataType: GroupDataType) {
        viewModelScope.launch(Dispatchers.IO) {
            _groups.postValue(groupsRepository.fetchGroups(teacherId, groupDataType))
        }
    }

    fun setTeacherId(id : String) {
        teacherId = id
    }
}