package com.saklayen.techhome.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.YearMonth
import java.util.*

@Entity(tableName = "report_table")
data class Report(
    @field: PrimaryKey(autoGenerate = true)
    @field: ColumnInfo(name = "id")
    val id: Int,
    @field: ColumnInfo(name = "device_id")
    val deviceId: Int,
    @field: ColumnInfo(name = "year_month")
    val yearMonth: YearMonth,
    @field: ColumnInfo(name = "day")
    val day: Int,
    @field: ColumnInfo(name = "start_time")
    val start: Time,
    @field: ColumnInfo(name = "stop_time")
    val stop: Time
) {
}