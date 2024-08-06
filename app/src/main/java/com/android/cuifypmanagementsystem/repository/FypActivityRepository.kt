package com.android.cuifypmanagementsystem.repository

import android.widget.Toast
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.room.datamodels.FypActivity
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FypActivityRepository(private val firestore: FirebaseFirestore,
                            private val roomDatabase: MainDatabase) {


    suspend fun fetchFypActivityData() : Result<List<FypActivity>> {
        return try{
            val snapshot = firestore.collection("Activities")
                .get().await()
            val fypActivities = snapshot.toObjects(FypActivity::class.java)
            Result.Success(fypActivities)
        }
        catch (e:Exception) {
            Result.Failure(e)
        }

    }

    suspend fun startFypActivity(fypActivityRecord: FypActivity) : Result<Void?>{

        return try{
            firestore.collection("Activities")
                .add(fypActivityRecord)
                .await()

            Result.Success(null)
        }
        catch (e : Exception)
        {
            Result.Failure(e)
        }

    }
}