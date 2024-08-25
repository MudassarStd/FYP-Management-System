package com.android.cuifypmanagementsystem.repository

import EmailSender.sendRegistrationEmail
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.cuifypmanagementsystem.datamodels.FypActivityRole
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.utils.Constants.GLOBAL_TESTING_TAG
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.TEACHER_COLLECTION
import com.android.cuifypmanagementsystem.utils.NetworkUtils.isInternetAvailable
import com.android.cuifypmanagementsystem.utils.RandomPasswordGenerator.generateRandomPassword
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TeacherRepository @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val database: MainDatabase,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private var _teachers = MutableLiveData<List<Teacher>>()
    val teachers: LiveData<List<Teacher>> get() = _teachers

    // ---------------- Firebase API Operations ----------------

    suspend fun registerTeacher(teacher: Teacher): Result<Void?> {
        return try {
            val tempPassword = generateRandomPassword(8)
            val authResult = firebaseAuth.createUserWithEmailAndPassword(teacher.email, tempPassword).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID is null")

            val cloudResult = addTeacherToCloud(teacher, uid)
            if (cloudResult is Result.Success) {
                withContext(Dispatchers.IO) {
                    sendRegistrationEmail(teacher.email, tempPassword, teacher.name)
                }
                Result.Success(null)
            } else {
                cloudResult
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    private suspend fun addTeacherToCloud(teacher: Teacher, uid: String): Result<Void?> {
        return try {
            val teacherData = mapOf(
                "name" to teacher.name,
                "email" to teacher.email,
                "department" to teacher.department,
                "supervisor" to teacher.supervisor,
                "fypHeadOrSecretory" to teacher.fypHeadOrSecretory,
                "fypActivityRole" to teacher.fypActivityRole,
                "registrationTimeStamp" to teacher.registrationTimeStamp
            )
            firestore.collection(TEACHER_COLLECTION).document(uid)
                .set(teacherData, SetOptions.merge()).await()

            teacher.firestoreId = uid
//            addTeacherLocally(teacher)

            Result.Success(null)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getAllTeachersFromCloud(): Result<List<Teacher>> {
        return try {
//            if (isInternetAvailable(applicationContext)) {
                val snapshot = firestore.collection(TEACHER_COLLECTION).get().await()
                val teachersList = snapshot.documents.map { document ->
                    document.toObject(Teacher::class.java)?.apply { firestoreId = document.id } ?: throw Exception("Teacher mapping failed")
                }
                Result.Success(teachersList)
//            }
//            else {
//                Result.Success(getAllFromRoom())
//            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getNotFypHeadSecretaries(): Result<List<Teacher>> {
        return try {
            if (isInternetAvailable(applicationContext)) {
                val snapshot = firestore.collection(TEACHER_COLLECTION)
                    .whereEqualTo("fypHeadOrSecretory", 0)
                    .get()
                    .await()
                val teachersList = snapshot.documents.map { document ->
                    document.toObject(Teacher::class.java)?.apply { firestoreId = document.id } ?: throw Exception("Teacher mapping failed")
                }
                Result.Success(teachersList)
            } else {
                Result.Success(getAllFromRoom().filter { it.fypHeadOrSecretory == 0 })
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun assignFypRoles(fypHeadId: String, fypHeadRole: FypActivityRole, fypSecretoryId: String, fypSecretoryRole: FypActivityRole): Result<Void?> {
        return try {
            val batch = firestore.batch()

            val headDocRef = firestore.collection(TEACHER_COLLECTION).document(fypHeadId)
            val secretoryDocRef = firestore.collection("teachers").document(fypSecretoryId)

            batch.update(headDocRef, mapOf(
                "fypHeadOrSecretory" to 1,
                "fypActivityRole.activityId" to fypHeadRole.activityId,
                "fypActivityRole.activityRole" to fypHeadRole.activityRole
            ))

            batch.update(secretoryDocRef, mapOf(
                "fypHeadOrSecretory" to 1,
                "fypActivityRole.activityId" to fypSecretoryRole.activityId,
                "fypActivityRole.activityRole" to fypSecretoryRole.activityRole
            ))

            batch.commit().await()
            Log.d("TestingRoleUpdate", "Successfully Updated FYP activity Roles")
            Result.Success(null)
        } catch (e: Exception) {
            Log.d("TestingRoleUpdate", "Error Role Updating: ${e.message}")
            Result.Failure(e)
        }
    }

    suspend fun updateFypRoles(currentRoleHolderId: String, newRoleHolderId: String, role: FypActivityRole): Result<Void?> {
        return try {
            val batch = firestore.batch()

            val currentTeacherDocRef = firestore.collection(TEACHER_COLLECTION).document(currentRoleHolderId)
            val newTeacherDocRef = firestore.collection(TEACHER_COLLECTION).document(newRoleHolderId)

            batch.update(currentTeacherDocRef, mapOf(
                "fypHeadOrSecretory" to 0,
                "fypActivityRole" to null
            ))

            batch.update(newTeacherDocRef, mapOf(
                "fypHeadOrSecretory" to 1,
                "fypActivityRole.activityId" to role.activityId,
                "fypActivityRole.activityRole" to role.activityRole
            ))



            batch.commit().await()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun rollbackUpdateFypRole(currentTeacherId: String, newTeacherId: String, role: FypActivityRole) : Result<Void?>  {
        return try {
            val batch = firestore.batch()

            val currentTeacherDocRef = firestore.collection(TEACHER_COLLECTION).document(currentTeacherId)
            val newTeacherDocRef = firestore.collection(TEACHER_COLLECTION).document(newTeacherId)

            batch.update(newTeacherDocRef, mapOf(
                "fypHeadOrSecretory" to 0,
                "fypActivityRole" to null
            ))

            batch.update(currentTeacherDocRef, mapOf(
                "fypHeadOrSecretory" to 1,
                "fypActivityRole.activityId" to role.activityId,
                "fypActivityRole.activityRole" to role.activityRole
            ))

            batch.commit().await()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getHeadSecretoryById(fypHeadId: String, fypSecretoryId: String): Pair<Teacher?, Teacher?> {
        return try {
            val headTask = firestore.collection(TEACHER_COLLECTION).document(fypHeadId).get()
            val secretaryTask = firestore.collection(TEACHER_COLLECTION).document(fypSecretoryId).get()

            val headSnapshot = headTask.await()
            val secretarySnapshot = secretaryTask.await()

            val headTeacher = headSnapshot.toObject(Teacher::class.java)
            val secretaryTeacher = secretarySnapshot.toObject(Teacher::class.java)

            Pair(headTeacher, secretaryTeacher)
        } catch (e: Exception) {
            Pair(null, null)
        }
    }

    suspend fun deleteTeacherRecord(uid: String): Result<Void?> {
        return try {
            firestore.collection(TEACHER_COLLECTION).document(uid).delete().await()
            deleteTeacherRecordById(uid)
            Result.Success(null)
        } catch (e: Exception) {
            Log.d(GLOBAL_TESTING_TAG, "Deletion Failed: ${e.message}")
            Result.Failure(e)
        }
    }

    suspend fun freeHeadSecretoryOnActivityClosure(fypHeadId: String, fypSecretoryId: String): Result<Void?> {
        return try {
            val batch = firestore.batch()

        val headDocRef = firestore.collection(TEACHER_COLLECTION).document(fypHeadId)
        val secretoryDocRef = firestore.collection(TEACHER_COLLECTION).document(fypSecretoryId)

        batch.update(
            headDocRef, mapOf(
                "fypHeadOrSecretory" to 0,
                "fypActivityRole" to null
            )
        )
        batch.update(
            secretoryDocRef, mapOf(
                "fypHeadOrSecretory" to 0,
                "fypActivityRole" to null
            )
        )

        batch.commit().await()
        Result.Success(null)
    }
     catch (e: Exception) {
        Result.Failure(e)
    }
    }

    suspend fun getTotalRegisteredTeacherCount() : Long {
       return firestore.collection(TEACHER_COLLECTION).get().await().size().toLong()
    }


    // ---------------- Room Database Operations ----------------

    private suspend fun addTeacherLocally(teacher: Teacher) {
        database.teacherDao().addTeacher(teacher)
        refreshTeachersFromRoom()
    }

    suspend fun updateTeacher(teacher: Teacher) {
        database.teacherDao().updateTeacher(teacher)
        refreshTeachersFromRoom()
    }

    suspend fun deleteTeacher(teacher: Teacher) {
        database.teacherDao().deleteTeacher(teacher)
        refreshTeachersFromRoom()
    }

    private suspend fun deleteTeacherRecordById(firestoreId: String) {
        database.teacherDao().deleteTeacherRecordById(firestoreId)
    }

    private suspend fun getAllFromRoom(): List<Teacher> {
        return database.teacherDao().getAllTeachers()
    }

    private suspend fun refreshTeachersFromRoom() {
        _teachers.postValue(getAllFromRoom())
    }
}
