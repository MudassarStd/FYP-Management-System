package com.android.cuifypmanagementsystem.repository

import android.content.Context
import android.util.Log
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.room.MainDatabase
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BatchRepository(private val firestore : FirebaseFirestore,
    private val roomDatabase: MainDatabase) {

    private val batchDao = roomDatabase.batchDao()


    suspend fun addBatch(batch : Batch){
        try {
            firestore.collection("batches").add(batch).await()
        }
        catch(e : Exception){
            Log.d("fjsljfsldf", e.message.toString())
        }
    }

    suspend fun fetchAllBatches() : Result<List<Batch>> {

         return try {
            val snapshot = firestore.collection("batches").get().await()

            val batchList = snapshot.documents.map { doc ->
                val batch = doc.toObject(Batch::class.java)!!
                batch.firestoreId = doc.id
                batch
            }

             Log.d("TestingBatchLogic", "Inside fetchBatch list: ${batchList}")
            Result.Success(batchList)

        }
        catch (e : Exception){
            Log.d("TestingBatchLogic", "exception : ${e.message}")
            Result.Failure(e)

        }
    }

      suspend fun fetchAvailableBatchesForActivity() : Result<List<Batch>> {

         return try {
            val snapshot = firestore.collection("batches").whereEqualTo("fypActivityStatus" , false).get().await()

            val batchList = snapshot.documents.map { doc ->
                val batch = doc.toObject(Batch::class.java)!!
                batch.firestoreId = doc.id
                batch
            }

             Log.d("TestingBatchLogic", "Inside fetchBatch list: ${batchList}")
            Result.Success(batchList)

        }
        catch (e : Exception){
            Log.d("TestingBatchLogic", "exception : ${e.message}")
            Result.Failure(e)

        }
    }

    suspend fun updateBatchActivityStatus(batchId : String, status : Boolean)  {

         try {
                val batchDocRef = firestore.collection("batches").document(batchId)
                val batchStatusUpdate = hashMapOf(
                    "fypActivityStatus" to status as Any
                )
                batchDocRef.update(batchStatusUpdate)

        }
        catch (e : Exception){
            Log.d("TestingBatchLogic", "exception : ${e.message}")
        }
    }













    // ---------------------- Room operations ----------------------

    suspend fun insert(batch : Batch) = batchDao.insert(batch)
    
    suspend fun update(batch : Batch) = batchDao.update(batch)

    suspend fun delete(batch : Batch) = batchDao.delete(batch)
    
    suspend fun getAllBatches(): List<Batch> {
        return batchDao.getAllBatches()
    }

    suspend fun getBatchById(id : Int) : Batch
    {
        return batchDao.getBatchById(id)
    }

}
