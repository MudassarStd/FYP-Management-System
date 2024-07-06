package com.android.cuifypmanagementsystem.viewmodel.repository

import android.content.Context
import com.android.cuifypmanagementsystem.room.Batch
import com.android.cuifypmanagementsystem.room.TestDb

class BatchRepository(app : Context) {
    private val db = TestDb.getDatabase(app).batchDao()
    suspend fun insert(batch : Batch)
    {
        db.insert(batch)
    }
    suspend fun delete(batch : Batch)
    {
        db.delete(batch)
    }
    suspend fun getAllBatches(): List<Batch> {
        return db.getAllBatches()
    }


}