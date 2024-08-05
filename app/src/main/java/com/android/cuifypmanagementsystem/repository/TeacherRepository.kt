package com.android.cuifypmanagementsystem.repository

import EmailSender.sendRegistrationEmail
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.cuifypmanagementsystem.apiservice.MailerSendEmail
import com.android.cuifypmanagementsystem.apiservice.MailerSendResponse
import com.android.cuifypmanagementsystem.apiservice.MailerSendService
import com.android.cuifypmanagementsystem.apiservice.Recipient
import com.android.cuifypmanagementsystem.apiservice.RetrofitClient
import com.android.cuifypmanagementsystem.apiservice.Sender
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.GLOBAL_TESTING_TAG
import com.android.cuifypmanagementsystem.utils.NetworkUtils.isInternetAvailable
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TeacherRepository(
    private val applicationContext: Context,
    private val database: MainDatabase,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private lateinit var uid : String

    private var _teachers = MutableLiveData<List<Teacher>>()
    val teachers : LiveData<List<Teacher>> get() = _teachers

    // ---------------- firebase APIs operations ----------------

    // teacher registration using firebase auth
    suspend fun registerTeacher(teacher: Teacher): Result<Void?> {
        return try {
            val tempPassword = generateRandomPassword(8)
            val authResult = firebaseAuth.createUserWithEmailAndPassword(teacher.email, tempPassword).await()
            uid = authResult.user?.uid ?: throw Exception("User ID is null")
            val cloudResult = addTeacherToCloud(teacher)
            if(cloudResult is Result.Success)
            {
                // ??? IF to update admin about sending email or resending it ???
                // sending email (runs on background (IO) thread)
                withContext(Dispatchers.IO){
                    sendRegistrationEmail(teacher.email, tempPassword, teacher.name)
                }
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

    // adding teacher's info into firestore
    private suspend fun addTeacherToCloud(teacher : Teacher) : Result<Void?>{
        return try{
            // storing custom data of teacher
            val teacherData = mapOf(
                "name" to teacher.name,
                "email" to teacher.email,
                "role" to teacher.role,
                "department" to teacher.department,
                "registrationTimeStamp" to teacher.registrationTimeStamp
                )
            firestore.collection("teachers").document(uid)
                .set(teacherData, SetOptions.merge()).await()

            // persisting teacher's data locally
            teacher.firestoreId = uid
            addTeacherLocally(teacher)

            Result.Success(null)
        }
        catch (e : Exception){
            Result.Failure(e)
        }
    }


    suspend fun getAllTeachersFromCloud() : Result<List<Teacher>>{
        return try{
            if (isInternetAvailable(applicationContext))
            {
                val snapshot = firestore.collection("teachers").get().await()
                Log.d("CloudTeacherFetchTesting", "Snapshot: ${snapshot}")
                val teachersList = snapshot.documents.map { document ->
                    val teacher = document.toObject(Teacher::class.java)!! // Automatic mapping
                    teacher.firestoreId = document.id // Manually set firestoreId
                    teacher
                }
                Log.d("CloudTeacherFetchTesting", "Teacher List: ${teachersList}")
                Result.Success(teachersList)
            }
            else{
                // fetch from room
                Result.Success(getAllFromRoom())
            }

        }
        catch (e : Exception){
            Result.Failure(e)
        }
    }


    // teacher account deletion
    suspend fun deleteTeacherRecord(uid : String){


        try {

            val documentPath = "teachers/${uid}"
            firestore.document(documentPath)
                .delete()
                .await()
            // also del from local db
            deleteTeacherRecordById(uid)

            // Implement Refresh Login after deleting records

        }
        catch (e : Exception)
        {
            Log.d(GLOBAL_TESTING_TAG, "Deletion Failed: ${e.message} ")
        }



            // Implement to delete user account


        }




    private fun generateRandomPassword(length: Int = 8): String {
        require(length in 8..12) { "Password length must be between 8 and 12 characters." }

        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" // Digits
        return (1..length)
            .map { chars.random() } // Pick random characters from the set
            .joinToString("") // Join them into a single string
    }


    // Send email to user

    // ----------- room operations -----------

    private suspend fun addTeacherLocally(teacher: Teacher)
    {
        database.teacherDao().addTeacher(teacher)
        getAllFromRoom()
    }
    suspend fun updateTeacher(teacher: Teacher)
    {
        database.teacherDao().updateTeacher(teacher)
        getAllFromRoom()
    }
    suspend fun deleteTeacher(teacher: Teacher)
    {
        database.teacherDao().deleteTeacher(teacher)
        getAllFromRoom()
    }

    private suspend fun deleteTeacherRecordById(firestoreId : String) {
        database.teacherDao().deleteTeacherRecordById(firestoreId)
    }

//    suspend fun getAll()
//    {
//        _teachers.postValue(database.teacherDao().getAllTeachers())
//    }
//
    suspend fun getAllFromRoom() : List<Teacher>
    {
        return database.teacherDao().getAllTeachers()
    }
}