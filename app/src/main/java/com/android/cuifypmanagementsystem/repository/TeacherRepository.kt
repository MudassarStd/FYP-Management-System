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

class TeacherRepository(
    private val applicationContext: Context,
    private val database: MainDatabase,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private lateinit var uid : String

    private var _teachers = MutableLiveData<List<Teacher>>()
    val teachers : LiveData<List<Teacher>> get() = _teachers


    private var _registrationResult = MutableLiveData<Result<Void?>>()
    val registrationResult : LiveData<Result<Void?>> get() = _registrationResult


//    private fun tempPassword() : String{
//
//    }

    // firebase API

    // teacher registration
    fun registerTeacher(teacher : Teacher)
    {
        val tempPassword = generateRandomPassword(8)

        firebaseAuth.createUserWithEmailAndPassword(teacher.email, tempPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    uid = firebaseAuth.currentUser!!.uid
                    _registrationResult.value = Result.Success(null)
                }
                else{
                    _registrationResult.value = Result.Failure(task.exception ?: Exception("Registration Failed"))
                }

            }
    }



    fun generateRandomPassword(length: Int = 8): String {
        require(length in 8..12) { "Password length must be between 8 and 12 characters." }

        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" // Digits 
        return (1..length)
            .map { chars.random() } // Pick random characters from the set
            .joinToString("") // Join them into a single string
    }



    // adding teacher's info
    suspend fun addTeacherToCloud(teacher : Teacher){
        // Use SetOptions.merge() to update the document if it already exists
        firestore.collection("teachers").document(uid)
            .set(teacher, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Teacher added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error adding teacher: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




    // ----------- room operations -----------
    suspend fun addTeacherLocally(teacher: Teacher)
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