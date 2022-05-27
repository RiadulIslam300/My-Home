package com.saklayen.techhome.room.database

import android.os.AsyncTask
import androidx.room.Database
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.room.dao.DeviceDao
import com.saklayen.techhome.utils.Checker

class Device_OperationAsyncTask(deviceDao: DeviceDao, dbOperaionType: Int) : AsyncTask<Device?, Void?, Void?>() {
    private val deviceDao: DeviceDao = deviceDao
    private val dbOperationType: Int = dbOperaionType
    override fun doInBackground(vararg params: Device?): Void? {
        when (dbOperationType) {
            Checker.INSERT_OPERATION -> deviceDao.insert(params[0])
            Checker.UPDATE_OPERATION -> deviceDao.update(params[0])
            else -> {
            }
        }
        return null
    }


}