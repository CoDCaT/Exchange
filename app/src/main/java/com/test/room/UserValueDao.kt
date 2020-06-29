package com.test.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserValueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateVideoData(value: UserValueRoom)

    @Query("SELECT * FROM UserValueRoom WHERE name LIKE :name")
    fun getUserValuesByName(name: String): UserValueRoom
}