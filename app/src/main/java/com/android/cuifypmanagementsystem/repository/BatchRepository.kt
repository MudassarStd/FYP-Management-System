package com.android.cuifypmanagementsystem.repository

import android.content.Context
import com.android.cuifypmanagementsystem.datamodels.Batch
import com.android.cuifypmanagementsystem.room.MainDatabase

class BatchRepository(app : Context) {
    private val db = MainDatabase.getDatabase(app).batchDao()
    
    suspend fun insert(batch : Batch) = db.insert(batch)
    
    suspend fun update(batch : Batch) = db.update(batch)

    suspend fun delete(batch : Batch) = db.delete(batch)
    
    suspend fun getAllBatches(): List<Batch> {
        return db.getAllBatches()
    }

    suspend fun getBatchById(id : Int) : Batch
    {
        return db.getBatchById(id)
    }

}
