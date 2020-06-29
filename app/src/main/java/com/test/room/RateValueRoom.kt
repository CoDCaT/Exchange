package com.test.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RateValueRoom(
    @PrimaryKey
    var name: String,
    var value: String,
    var date: Date,
    var baseValue: String
)