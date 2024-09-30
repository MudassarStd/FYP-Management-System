package com.android.cuifypmanagementsystem.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.auth.model.StudentRegistration
import com.android.cuifypmanagementsystem.datamodels.LoggedInUserData
import com.android.cuifypmanagementsystem.auth.repository.UserAuthRepository
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.student.repository.RegistrationRepository
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.utils.UserAuthSharedPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
    private val studentRegistrationRepository: RegistrationRepository
)
    : ViewModel() {

    private val _userAuthState = MutableLiveData<Result<LoggedInUserData>>()
    val userAuthState : LiveData<Result<LoggedInUserData>> get() = _userAuthState

    private val _passwordResetEmailState = MutableLiveData<Result<Void?>>()
    val passwordResetEmailState : LiveData<Result<Void?>> get() = _passwordResetEmailState

    private val _passwordChangeResult = MutableLiveData<Result<Void?>>()
    val passwordChangeResult: LiveData<Result<Void?>> get() = _passwordChangeResult

    private val _studentRegistrationState = MutableLiveData<Result<Unit>>()
    val studentRegistrationState: LiveData<Result<Unit>> get() = _studentRegistrationState

    private var rememberMeOnLogin : Boolean = false

    fun userLogin(email : String, password : String){
        _userAuthState.value = Result.Loading
        viewModelScope.launch {
            _userAuthState.value = userAuthRepository.userLogin(email, password).also{result ->
                if (result is Result.Success && rememberMeOnLogin){
                    UserAuthSharedPreferenceHelper.saveUserAuthData(result.data)
                }
            }
        }
    }

    fun userLogout(){
        UserAuthSharedPreferenceHelper.clearUserAuthData()
        viewModelScope.launch {
            userAuthRepository.userLogout()
        }
    }


    fun sendPasswordResetEmail (email : String){
        _passwordResetEmailState.value = Result.Loading
        viewModelScope.launch {
            _passwordResetEmailState.value = userAuthRepository.sendPasswordResetEmail(email)
        }
    }


    fun userChangePassword(oldPassword : String, newPassword : String){
        _passwordChangeResult.value = Result.Loading
        viewModelScope.launch {
            _passwordChangeResult.value = userAuthRepository.userChangePassword(oldPassword, newPassword)
        }
    }

    fun setRememberMeState(flag : Boolean) {
        rememberMeOnLogin = flag
    }


    // ================================= Student Registration ======================================

    fun registerStudent(studentRegistration: StudentRegistration, student: Student) {
        _studentRegistrationState.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _studentRegistrationState.postValue(studentRegistrationRepository.registerStudent(studentRegistration, student))
        }
    }
}




