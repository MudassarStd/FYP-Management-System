package com.android.cuifypmanagementsystem.repository

import android.util.Log
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.FYP_ACTIVITY_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FypActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore) {


    suspend fun fetchFypActivityData(activityStatus : Boolean) : Result<List<FypActivityRecord>> {
        return try{
            val snapshot = firestore.collection(FYP_ACTIVITY_COLLECTION).whereEqualTo("status", activityStatus)
                .get().await()
            val fypActivities = snapshot.documents.map {doc ->
                val fypActivity = doc.toObject(FypActivityRecord::class.java)!!
                fypActivity.copy(firestoreId = doc.id)

            }
            Log.d("FYpActivityResultsjfsl","$fypActivities")
            Result.Success(fypActivities)
        }
        catch (e:Exception) {
            Result.Failure(e)
        }

    }

    suspend fun startFypActivity(fypActivityRecord: FypActivityRecord) : Result<String>{

        return try{
            val activityDocRef = firestore.collection(FYP_ACTIVITY_COLLECTION)
                .add(fypActivityRecord)
                .await()

            Result.Success(activityDocRef.id)
        }
        catch (e : Exception)
        {
            Log.d("fsdlfjsaoifhsadofhisd", "Failed to get activities")
            Result.Failure(e)
        }
    }

    suspend fun deleteFypActivity(activityId: String) {
        try {
            // Get a reference to the Firestore collection
            val activityDoc = firestore.collection(FYP_ACTIVITY_COLLECTION).document(activityId)

            // Perform the delete operation
            activityDoc.delete().await()

            // Optionally, you can log or handle success here
            Log.d("DeleteFypActivity", "Activity with ID $activityId deleted successfully")
        } catch (e: Exception) {
            // Handle any exceptions that occur
            Log.d("DeleteFypActivity", "Error deleting activity: ${e.message}")
            // Optionally, rethrow the exception or handle it as needed
            throw e
        }
    }

    suspend fun getActivityInfo(activityId: String): FypActivityRecord? {
        return try {
            val docSnapshot = firestore.collection(FYP_ACTIVITY_COLLECTION).document(activityId).get().await()

            if (docSnapshot.exists()) {
                val activity = docSnapshot.toObject(FypActivityRecord::class.java)
                Log.d("hfsjdhfsjd", "Document data: ${docSnapshot.data}")
                Log.d("hfsjdhfsjd", "Deserialized activity: $activity")
                activity
            } else {
                Log.d("hfsjdhfsjd", "Document does not exist for activityId: $activityId")
                null
            }
        } catch (e: Exception) {
            Log.e("hfsjdhfsjd", "Error fetching document: ${e.message}")
            null
        }
    }


    suspend fun updateFypRole(activityId: String, newTeacherId: String, role: String) : Result<Void?> {

        return try {
            // Get the document reference for the activity
            val docRef = firestore.collection(FYP_ACTIVITY_COLLECTION).document(activityId)

            // Create a map to hold the updates based on the role
            val roleUpdate = when (role) {
                "Head" -> mapOf("fypHeadId" to newTeacherId)
                "Secretory" -> mapOf("fypSecId" to newTeacherId)
                else -> throw IllegalArgumentException("Invalid role: $role")
            }

            // Execute the batch update
            firestore.runBatch { batch ->
                batch.update(docRef, roleUpdate)
            }.await()

            Result.Success(null)
        }
        catch (e : Exception) {

            Log.d("TESTING_UPDATE_ROLES_DURING_FYP_ACTIVITIES","Failed to update role in activity: ${e.message}")
            Result.Failure(e)
//            throw IllegalArgumentException("Exce: ${e.message}")

        }
    }

    suspend fun rollbackFypRoleUpdate(activityId: String, currentTeacherId: String, role: String) : Result<Void?>{

        return try {
            // Get the document reference for the activity
            val docRef = firestore.collection(FYP_ACTIVITY_COLLECTION).document(activityId)

            // Create a map to hold the updates based on the role
            val roleUpdate = when (role) {
                "Head" -> mapOf("fypHeadId" to currentTeacherId)
                "Secretory" -> mapOf("fypSecId" to currentTeacherId)
                else -> throw IllegalArgumentException("Invalid role: $role")
            }

            // Execute the batch update
            firestore.runBatch { batch ->
                batch.update(docRef, roleUpdate)
            }.await()

            Result.Success(null)
        }
        catch (e : Exception) {

            Log.d("TESTING_UPDATE_ROLES_DURING_FYP_ACTIVITIES","Failed to update role in activity: ${e.message}")
            Result.Failure(e)
//            throw IllegalArgumentException("Exce: ${e.message}")
        }
    }

    suspend fun closeActivity(activityId : String) : Result<Void?>{
        return try {

            val doc = firestore.collection(FYP_ACTIVITY_COLLECTION).document(activityId)

            doc.update("status", false).await()

            Result.Success(null)

        } catch (e: Exception) {
            Result.Failure(e)
        }

    }


}