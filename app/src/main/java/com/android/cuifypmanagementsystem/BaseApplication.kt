package com.android.cuifypmanagementsystem

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.repository.UserAuthRepository
import com.android.cuifypmanagementsystem.repository.BatchRepository
import com.android.cuifypmanagementsystem.repository.FypActivityRepository
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.viewmodel.H_S_SelectionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class BaseApplication  : Application() {

    lateinit var teacherRepository: TeacherRepository
    lateinit var fypActivityRepository: FypActivityRepository
    lateinit var batchRepository: BatchRepository
    lateinit var userAuthRepository: UserAuthRepository

    private val h_s_selectionViewModel: H_S_SelectionViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(H_S_SelectionViewModel::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        initialize()

    }


    private fun initialize() {
        val roomDatabase = MainDatabase.getDatabase(applicationContext)
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()


        teacherRepository = TeacherRepository(applicationContext, roomDatabase, firestore, firebaseAuth)
        fypActivityRepository = FypActivityRepository(firestore, roomDatabase)
        batchRepository = BatchRepository(firestore, roomDatabase)
        userAuthRepository = UserAuthRepository(firestore, firebaseAuth)
    }

    fun getH_S_SelectionViewModel(): H_S_SelectionViewModel {
        return h_s_selectionViewModel
    }
}