package com.android.cuifypmanagementsystem.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.cuifypmanagementsystem.room.datamodels.Batch

@Database(entities = [Batch::class], version = 1, exportSchema = false)
abstract class TestDb : RoomDatabase() {
    abstract fun batchDao(): BatchDao

    companion object {
        @Volatile
        private var INSTANCE: TestDb? = null

        fun getDatabase(context: Context): TestDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestDb::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
