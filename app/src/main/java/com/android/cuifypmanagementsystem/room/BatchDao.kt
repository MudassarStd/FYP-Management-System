package com.android.cuifypmanagementsystem.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.cuifypmanagementsystem.room.datamodels.Batch


@Dao
interface BatchDao {
    @Insert
    suspend fun insert(batch : Batch)
    @Delete
    suspend fun delete(batch : Batch)
    @Update
    suspend fun update(batch : Batch)
    @Query("select * from Batch order by semester asc")
    suspend fun getAllBatches() : List<Batch>
    @Query("select * from Batch where id = :id ")
    suspend fun getBatchById(id : Int) : Batch
}