package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.LoggedInUserData
import com.android.cuifypmanagementsystem.repository.UserAuthRepository
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.utils.UserAuthSharedPreferenceHelper
import kotlinx.coroutines.launch

class UserAuthViewModel(private val userAuthRepository: UserAuthRepository) : ViewModel() {

    private val _userAuthState = MutableLiveData<Result<LoggedInUserData>>()
    val userAuthState : LiveData<Result<LoggedInUserData>> get() = _userAuthState


    private val _passwordResetEmailState = MutableLiveData<Result<Void?>>()
    val passwordResetEmailState : LiveData<Result<Void?>> get() = _passwordResetEmailState

    private val _passwordChangeResult = MutableLiveData<Result<Void?>>()
    val passwordChangeResult: LiveData<Result<Void?>> get() = _passwordChangeResult

    fun userLogin(email : String, password : String){
        _userAuthState.value = Result.Loading
        viewModelScope.launch {
            _userAuthState.value = userAuthRepository.userLogin(email, password).also{result ->
                if (result is Result.Success){
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

//    fun resetUserAuthState(){
//        _userAuthState.value =
//    }
}




