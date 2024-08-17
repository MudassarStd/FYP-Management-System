package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cuifypmanagementsystem.datamodels.UserAuthData
import com.android.cuifypmanagementsystem.repository.UserAuthRepository
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.launch

class UserAuthViewModel(private val userAuthRepository: UserAuthRepository) : ViewModel() {

    private val _userAuthState = MutableLiveData<Result<UserAuthData>>()
    val userAuthState : LiveData<Result<UserAuthData>> get() = _userAuthState

    fun userLogin(email : String, password : String){
        viewModelScope.launch {
            _userAuthState.value = userAuthRepository.userLogin(email, password)
        }
    }
}




