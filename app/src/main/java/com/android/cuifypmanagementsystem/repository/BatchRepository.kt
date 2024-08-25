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


    suspend fun addBatch(batch : Batch) : Result<Void?>{
        return try {
            firestore.collection("batches").add(batch).await()
            Result.Success(null)
        }
        catch(e : Exception){
            Result.Failure(e)
        }
    }

    suspend fun updateBatch(batch: Batch) : Result<Void?> {

        return try {
                // Get a reference to the document with the matching ID
            val documentRef = firestore.collection("batches").document(batch.firestoreId!!)

            // Update the batch data in the document
            documentRef.set(batch).await()
            Result.Success(null)

        }
            catch (e: Exception) {
                Result.Failure(e)
        }
    }


    suspend fun updateBatchOnActivityClosure(batchId: String) : Result<Void?> {

        return try {
                // Get a reference to the document with the matching ID
            val docRef = firestore.collection("batches").document(batchId)

            // Update the batch data in the document
            docRef.update("fypActivityStatus", null).await()
            Result.Success(null)

        }
            catch (e: Exception) {
                Result.Failure(e)
        }
    }



    suspend fun deleteBatch(batchId: String): Result<Void?> {
        return try {
            // Get a reference to the document with the matching ID
            val documentRef =
                FirebaseFirestore.getInstance().collection("batches").document(batchId)

            // Delete the batch document
            documentRef.delete().await()
            Result.Success(null)

        } catch (e: Exception) {
            Result.Failure(e)
        }
    }




    suspend fun fetchAllActiveAndAvailableBatches(): Result<List<Batch>> {
        return try {
            // Adjust the query to filter batches where fypActivityStatus is not null
            val query = firestore.collection("batches")
                .whereNotEqualTo("fypActivityStatus", null)

            val snapshot = query.get().await()

            val batchList = snapshot.documents.map { doc ->
                val batch = doc.toObject(Batch::class.java)!!
                batch.firestoreId = doc.id
                batch
            }

            Log.d("TestingBatchLogic", "Filtered batch list: $batchList")
            Result.Success(batchList)
        } catch (e: Exception) {
            Log.d("TestingBatchLogic", "Exception: ${e.message}")
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


    suspend fun getBatchById(batchId : String) : Batch? {
        return try {
            val docSnapshot = firestore.collection("batches").document(batchId).get().await()
            if (docSnapshot.exists()){
                val batchDoc = docSnapshot.toObject(Batch::class.java)
                batchDoc
            }

            else{
                null
            }
        }
        catch (e : Exception){
            null
        }
    }











    // ---------------------- Room operations ----------------------

    suspend fun insert(batch : Batch) = batchDao.insert(batch)
    
//    suspend fun update(batch : Batch) = batchDao.update(batch)

    suspend fun delete(batch : Batch) = batchDao.delete(batch)
    
    suspend fun getAllBatches(): List<Batch> {
        return batchDao.getAllBatches()
    }

    suspend fun getBatchById(id : Int) : Batch
    {
        return batchDao.getBatchById(id)
    }



}
