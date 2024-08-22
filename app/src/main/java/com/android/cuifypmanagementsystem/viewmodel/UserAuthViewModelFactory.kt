package com.android.cuifypmanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.repository.BatchRepository
import com.android.cuifypmanagementsystem.repository.UserAuthRepository


class UserAuthViewModelFactory(private val userAuthRepository: UserAuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserAuthViewModel(userAuthRepository) as T
    }
}