package com.android.cuifypmanagementsystem.room

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
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
}