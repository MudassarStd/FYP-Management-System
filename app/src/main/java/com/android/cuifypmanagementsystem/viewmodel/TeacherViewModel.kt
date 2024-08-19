package com.android.cuifypmanagementsystem.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.cuifypmanagementsystem.datamodels.FypActivityRole
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.GLOBAL_TESTING_TAG
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherViewModel(private val teacherRepository: TeacherRepository) : ViewModel() {

    val teachers : LiveData<List<Teacher>> get() = teacherRepository.teachers

    private val _teachersFromCloud = MutableLiveData<Result<List<Teacher>>>()
    val teachersFromCloud : LiveData<Result<List<Teacher>>> get() = _teachersFromCloud

    private val _notFypHeadSecretaries = MutableLiveData<Result<List<Teacher>>>()
    val notFypHeadSecretaries : LiveData<Result<List<Teacher>>> get() = _notFypHeadSecretaries

    private var _teacherRegistrationResult = MutableLiveData<Result<Void?>>()
    val teacherRegistrationResult : LiveData<Result<Void?>> get() = _teacherRegistrationResult

    private var _teacherRoleUpdateStatusForActivity = MutableLiveData<Result<Void?>>()
    val teacherRoleUpdateStatusForActivity : LiveData<Result<Void?>> get() = _teacherRoleUpdateStatusForActivity

    private val _fypHeadSecretaryById = MutableLiveData<Pair<Teacher?, Teacher?>>()
    val fypHeadSecretaryById: LiveData<Pair<Teacher?, Teacher?>> = _fypHeadSecretaryById


    init {
//        getAllTeachersFromCloud()
//        getNotFypHeadSecretaries()
//       viewModelScope.launch {
//           teacherRepository.getAllFromRoom()
//       }
    }

    fun registerTeacher(teacher: Teacher){
        _teacherRegistrationResult.value = Result.Loading
        viewModelScope.launch {
           _teacherRegistrationResult.value = teacherRepository.registerTeacher(teacher)
            getAllTeachersFromCloud() // refreshes list
        }
    }

    fun getAllTeachersFromCloud(){
        Log.d("DisplayTeacherDebuggerAttached", "getAllTeachersFromCloud() called in VM")
        _teachersFromCloud.value = Result.Loading
        viewModelScope.launch {
            _teachersFromCloud.value = teacherRepository.getAllTeachersFromCloud()
        }
    }

    fun getNotFypHeadSecretaries(){
        _teachersFromCloud.value = Result.Loading
        viewModelScope.launch {
            _teachersFromCloud.value = teacherRepository.getNotFypHeadSecretaries()
        }
    }

    fun updateTeacherRoles(fypHeadId: String, fypHeadRole: FypActivityRole, fypSecretoryId: String, fypSecretoryRole: FypActivityRole) {
        _teacherRoleUpdateStatusForActivity.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _teacherRoleUpdateStatusForActivity.postValue(teacherRepository.updateTeacherRoles(fypHeadId, fypHeadRole, fypSecretoryId, fypSecretoryRole))
        }
    }

    fun getHeadSecretoryById(fypHeadId : String, fypSecretoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _fypHeadSecretaryById.postValue(teacherRepository.getHeadSecretoryById(fypHeadId, fypSecretoryId))
        }
    }




            // code for manage teachers




//    fun addTeacher(teacher: Teacher)
//    {
//        viewModelScope.launch(Dispatchers.IO) {
//            teacherRepository.addTeacherLocally(teacher)
//        }
//    }

    fun updateTeacher(teacher: Teacher)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.updateTeacher(teacher)
        }
    }

    fun deleteTeacherRecord(uid : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.deleteTeacherRecord(uid)
        }
    }



}