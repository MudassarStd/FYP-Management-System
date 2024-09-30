package com.android.cuifypmanagementsystem.room

import androidx.room.TypeConverter
import com.android.cuifypmanagementsystem.datamodels.GroupRequest
import com.android.cuifypmanagementsystem.student.datamodel.Group
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {

    @TypeConverter
    fun timestampToLong(timestamp: Timestamp): Long {
        return timestamp.toDate().time
    }

    @TypeConverter
    fun longToTimestamp(epochMillis: Long): Timestamp {
        return Timestamp(Date(epochMillis))
    }

    @TypeConverter
    fun fromGroupRequestList(value: List<GroupRequest>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toGroupRequestList(value: String): List<GroupRequest>? {
        val listType = object : TypeToken<List<GroupRequest>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromGroupList(value: List<Group>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toGroupList(value: String): List<Group>? {
        val listType = object : TypeToken<List<Group>>() {}.type
        return Gson().fromJson(value, listType)
    }
}