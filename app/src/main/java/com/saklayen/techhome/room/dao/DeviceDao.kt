package com.saklayen.techhome.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.saklayen.techhome.models.Device

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(device: Device?)

    @Update
    fun update(device: Device?)
    @Query("SELECT * FROM device_table")
    fun getAllDevices(): LiveData<List<Device?>?>?

}