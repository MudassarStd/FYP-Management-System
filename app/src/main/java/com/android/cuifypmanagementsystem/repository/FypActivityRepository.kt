package com.android.cuifypmanagementsystem.repository

import android.util.Log
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.datamodels.FypActivityRecord
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FypActivityRepository(private val firestore: FirebaseFirestore,
                            private val roomDatabase: MainDatabase) {


    suspend fun fetchFypActivityData() : Result<List<FypActivityRecord>> {
        return try{
            val snapshot = firestore.collection("Activities")
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
            val activityDocRef = firestore.collection("Activities")
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
            val activityDoc = firestore.collection("Activities").document(activityId)

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
            val docSnapshot = firestore.collection("Activities").document(activityId).get().await()

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


}