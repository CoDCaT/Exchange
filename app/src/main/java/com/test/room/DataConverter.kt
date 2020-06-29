package com.test.room

import androidx.room.TypeConverter
import java.util.*

class DataConverter {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun timestampToDate(time: Long): Date? = Date(time)
}