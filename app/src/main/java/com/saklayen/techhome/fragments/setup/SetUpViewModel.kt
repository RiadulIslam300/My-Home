package com.saklayen.techhome.fragments.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.room.database.TechHomeDatabase

class SetUpViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SetupRepository
    private val allDevices: LiveData<List<Device?>?>?

    fun insert(device: Device?) {
        repository.insert(device)
    }

    fun update(device: Device?) {
        repository.update(device)
    }
    fun getAllDevices(): LiveData<List<Device?>?>? {
        return allDevices
    }

    init {
        val deviceDao = TechHomeDatabase.INSTANCE!!.deviceDao()
        repository = SetupRepository(application,deviceDao)
        allDevices = repository.getAllDevices()
    }

}