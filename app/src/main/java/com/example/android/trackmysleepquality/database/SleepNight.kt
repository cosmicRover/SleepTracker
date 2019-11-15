package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//creating data model for room database

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
        @PrimaryKey(autoGenerate = true)//let it auto generate an unique key
        var nightId: Long = 0L,

        @ColumnInfo(name = "start_time_milli")//name of columns
        val startTimeMilli:Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time_milli")
        var endTimeMilli:Long = startTimeMilli,

        @ColumnInfo(name = "quality_rating")
        var sleepQuality:Int = -1
)
