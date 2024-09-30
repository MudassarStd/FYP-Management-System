package com.android.cuifypmanagementsystem.student.repository

import android.util.Log
import com.android.cuifypmanagementsystem.datamodels.GroupRequest
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.TEACHER_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun fetchSupervisors(): Result<List<Teacher>> {
        return try {
            val snapshot = firestore.collection(TEACHER_COLLECTION).get().await()
            val teachersList = snapshot.documents.map { document ->
                document.toObject(Teacher::class.java)?.apply { firestoreId = document.id } ?: throw Exception("Teacher mapping failed")
            }
            Result.Success(teachersList)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


    suspend fun requestSupervisor(
        groupId: String?,
        groupBatch: String?,
        teacherId: String?
    ): Result<Unit> {
        if (groupId == null || groupBatch == null || teacherId == null) {
            return Result.Failure(IllegalArgumentException("Invalid input: groupId, groupBatch, or teacherId is null."))
        }

        val teacherDocRef = firestore.collection("teachers").document(teacherId)

        return try {
            val document = teacherDocRef.get().await()
            if (!document.exists()) {
                return Result.Failure(NoSuchElementException("Teacher document not found for teacherId: $teacherId"))
            }

            val teacherFromFirestore = document.toObject(Teacher::class.java)
                ?: return Result.Failure(IllegalStateException("Failed to convert document to Teacher"))

            val existingGroupRequest = teacherFromFirestore.groupRequests?.find { it.batch == groupBatch }

            if (existingGroupRequest != null) {
                existingGroupRequest.requests = existingGroupRequest.requests!!.toMutableList().apply { add(groupId) }
            } else {
                val newGroupRequest = GroupRequest(batch = groupBatch, requests = listOf(groupId))
                teacherFromFirestore.groupRequests = (teacherFromFirestore.groupRequests?.toMutableList()
                    ?: mutableListOf()).apply { add(newGroupRequest) }
            }

            teacherDocRef.update("groupRequests", teacherFromFirestore.groupRequests).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("RequestSupervisor", "Error handling teacherId: $teacherId", e)
            Result.Failure(e)
        }
    }

}