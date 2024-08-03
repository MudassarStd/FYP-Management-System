package com.android.cuifypmanagementsystem.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.cuifypmanagementsystem.room.datamodels.Batch
import com.android.cuifypmanagementsystem.room.datamodels.Teacher

@Database(entities = [Batch::class, Teacher::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun batchDao(): BatchDao
    abstract fun teacherDao() : TeacherDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    "main_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
