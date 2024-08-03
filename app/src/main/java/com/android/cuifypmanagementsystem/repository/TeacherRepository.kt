package com.android.cuifypmanagementsystem.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.room.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class TeacherRepository(
    private val applicationContext: Context,
    private val database: MainDatabase,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private lateinit var uid : String

    private var _teachers = MutableLiveData<List<Teacher>>()
    val teachers : LiveData<List<Teacher>> get() = _teachers

    // firebase API

    // teacher registration
    suspend fun registerTeacher(teacher: Teacher): Result<Void?> {
        return try {
            val tempPassword = generateRandomPassword(8)
            val authResult = firebaseAuth.createUserWithEmailAndPassword(teacher.email, tempPassword).await()
            uid = authResult.user?.uid ?: throw Exception("User ID is null")
            val cloudResult = addTeacherToCloud(teacher)
            if(cloudResult is Result.Success)
            {
                Result.Success(null)
            }
            else{
                cloudResult
            }
            // on Success, dispatch email to registered user
        }   catch (e: Exception) {
            Result.Failure(e)
        }
    }

    // adding teacher's info
    private suspend fun addTeacherToCloud(teacher : Teacher) : Result<Void?>{
        return try{
            // storing custom data of teacher
            val teacherData = mapOf(
                "name" to teacher.name,
                "email" to teacher.email,
                "role" to teacher.role,
                "department" to teacher.depart,
                "registrationTimeStamp" to teacher.registrationTimeStamp
                )
            firestore.collection("teachers").document(uid)
                .set(teacherData, SetOptions.merge()).await()

            addTeacherLocally(teacher)
            Result.Success(null)
        }
        catch (e : Exception){
            Result.Failure(e)
        }
    }

    private fun generateRandomPassword(length: Int = 8): String {
        require(length in 8..12) { "Password length must be between 8 and 12 characters." }

        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" // Digits
        return (1..length)
            .map { chars.random() } // Pick random characters from the set
            .joinToString("") // Join them into a single string
    }


    // ----------- room operations -----------
    private suspend fun addTeacherLocally(teacher: Teacher)
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