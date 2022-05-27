package com.saklayen.techhome.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
data class Device(
    @field: PrimaryKey(autoGenerate = true)
    @field: ColumnInfo(name = "id")
    val id: Int,
    @field: ColumnInfo(name = "device_name")
    val device: String,
    @field: ColumnInfo(name = "power")
    val power: Int)
{

}