package com.android.cuifypmanagementsystem.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.room.datamodels.Teacher

class TeacherRepository(private val applicationContext: Context, private val database: MainDatabase) {

    private var _teachers = MutableLiveData<List<Teacher>>()
    val teachers : LiveData<List<Teacher>> get() = _teachers

    suspend fun addTeacher(teacher: Teacher)
    {
        database.teacherDao().addTeacher(teacher)
        getAll()
    }
    suspend fun updateTeacher(teacher: Teacher)
    {
        database.teacherDao().updateTeacher(teacher)
        getAll()
    }
    suspend fun deleteTeacher(teacher: Teacher)
    {
        database.teacherDao().deleteTeacher(teacher)
        getAll()
    }
    suspend fun getAll()
    {
        _teachers.postValue(database.teacherDao().getAllTeachers())
    }
}