package com.test.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserValueRoom(
    @PrimaryKey
    var name: String,
    var value: Double
)