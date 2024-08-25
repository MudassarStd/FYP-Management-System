package com.android.cuifypmanagementsystem

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.utils.UserAuthSharedPreferenceHelper
import com.android.cuifypmanagementsystem.viewmodel.GlobalSharedViewModel
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication  : Application() {

    private val globalSharedViewModel: GlobalSharedViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(GlobalSharedViewModel::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {

        // initializing shared prefs
        UserAuthSharedPreferenceHelper.initialize(applicationContext)
    }

    fun getGlobalSharedViewModel(): GlobalSharedViewModel {
        return globalSharedViewModel
    }
}