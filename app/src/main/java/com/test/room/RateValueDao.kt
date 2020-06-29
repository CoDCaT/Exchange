package com.test.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RateValueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateVideoData(value: RateValueRoom)

    @Query("SELECT * FROM ratevalueroom")
    fun getAllRateValues(): MutableList<RateValueRoom>
}