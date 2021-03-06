package com.spkd.worldclock.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeZone(@PrimaryKey val uid: String,
                 @ColumnInfo(name = "timezone") val timeZoneName: String?)