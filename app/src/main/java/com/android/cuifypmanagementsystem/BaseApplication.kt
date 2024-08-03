package com.android.cuifypmanagementsystem

import android.app.Application
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class BaseApplication  : Application() {

    lateinit var teacherRepository: TeacherRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val roomDatabase = MainDatabase.getDatabase(applicationContext)
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()

        teacherRepository = TeacherRepository(applicationContext, roomDatabase, firestore, firebaseAuth)
    }
}