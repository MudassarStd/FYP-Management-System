package com.android.cuifypmanagementsystem

import android.app.Application
import com.android.cuifypmanagementsystem.repository.TeacherRepository
import com.android.cuifypmanagementsystem.room.MainDatabase

class MainApplication  : Application() {

    lateinit var teacherRepository: TeacherRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val database = MainDatabase.getDatabase(applicationContext)
        teacherRepository = TeacherRepository(applicationContext, database)
    }
}