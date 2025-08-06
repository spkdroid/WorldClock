package com.spkd.worldclock.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "timezone_table")
@Parcelize
data class TimeZone(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "timezone") val timeZoneName: String,
    @ColumnInfo(name = "display_name") val displayName: String = "",
    @ColumnInfo(name = "is_selected") val isSelected: Boolean = false
) : Parcelable