package com.android.cuifypmanagementsystem.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.cuifypmanagementsystem.datamodels.Teacher

@Dao
interface TeacherDao {
    @Insert
    suspend fun addTeacher(teacher: Teacher)
    @Update
    suspend fun updateTeacher(teacher: Teacher)
    @Delete
    suspend fun deleteTeacher(teacher: Teacher)

    @Query("delete from Teacher where firestoreId = :firestoreId")
    suspend fun deleteTeacherRecordById(firestoreId : String)

    @Query("select * from Teacher")
    suspend fun getAllTeachers() : List<Teacher>

}