package com.android.cuifypmanagementsystem.teacher.repository

import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.TEACHER_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.android.cuifypmanagementsystem.utils.Result
import kotlinx.coroutines.tasks.await


class TeacherRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){

    suspend fun getTeacherById(teacherId: String): Result<Teacher> {
        return try {
            // Fetch the teacher document by ID from Firestore
            val documentSnapshot = firestore.collection(TEACHER_COLLECTION).document(teacherId).get().await()

            // Convert the document to a Teacher object
            val teacher = documentSnapshot.toObject(Teacher::class.java)

            teacher?.let {
                Result.Success(teacher)
            } ?: Result.Failure(NoSuchElementException("Teacher not found"))

        } catch (e: Exception) {
            // Handle errors
            Result.Failure(Exception("Failed to fetch teacher: ${e.message}", e))
        }
    }

    suspend fun updateTeacher(teacher: Teacher): Result<Void?> {
        return try {
            firestore.collection(TEACHER_COLLECTION).document(teacher.firestoreId!!).set(teacher).await()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Failure(Exception("Failed to update teacher: ${e.message}", e))
        }
    }
}