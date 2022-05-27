package com.saklayen.techhome.fragments.setup

import android.app.Application
import androidx.lifecycle.LiveData
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.room.dao.DeviceDao
import com.saklayen.techhome.room.database.TechHomeDatabase

class SetupRepository internal constructor(application: Application, deviceDao: DeviceDao?) {
    private var deviceDao = deviceDao
    private  var allDevices: LiveData<List<Device?>?>? = null
    fun insert(device: Device?) {
        deviceDao!!.insert(device)
    }

    fun update(device: Device?) {
        deviceDao!!.update(device)
    }


    fun getAllDevices(): LiveData<List<Device?>?>? {
        return allDevices
    }

    init {
        val dataBase = TechHomeDatabase.INSTANCE
        if (dataBase != null) {
            allDevices = deviceDao!!.getAllDevices()
        }

    }
}