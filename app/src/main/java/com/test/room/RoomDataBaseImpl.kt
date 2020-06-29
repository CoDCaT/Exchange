package com.test.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RateValueRoom::class, UserValueRoom::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class RoomDataBaseImpl : RoomDatabase() {
    abstract fun rateValueDao(): RateValueDao
    abstract fun userValueDao(): UserValueDao
}