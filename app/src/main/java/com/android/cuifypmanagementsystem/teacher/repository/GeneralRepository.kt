package com.android.cuifypmanagementsystem.teacher.repository

import com.android.cuifypmanagementsystem.datamodels.FypIdea
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.FYP_IDEAS_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GeneralRepository @Inject constructor(
    private val firestore : FirebaseFirestore
){

    suspend fun addFypIdea(fypIdea: FypIdea): Result<Void?> {
        return try {
            firestore.collection(FYP_IDEAS_COLLECTION)
                .add(fypIdea)
                .await() // This suspends the function until the task is complete
            Result.Success(null)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getFypIdeas(): Result<List<FypIdea>> {
        return try {
            val snapshot = firestore.collection(FYP_IDEAS_COLLECTION)
                .orderBy("dateTime", Query.Direction.DESCENDING) // Sort directly in Firestore
                .get()
                .await() // This suspends the function until the task is complete

            val fypIdeas = snapshot.documents.mapNotNull { document ->
                document.toObject(FypIdea::class.java)?.apply {
                    firestoreId = document.id
                }
            }

            Result.Success(fypIdeas)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}