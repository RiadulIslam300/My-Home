package com.saklayen.techhome.room.database

import android.os.AsyncTask
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.models.Report
import com.saklayen.techhome.room.dao.DeviceDao
import com.saklayen.techhome.room.dao.ReportDao
import com.saklayen.techhome.utils.Checker

class Report_OperationAsyncTask(reportDao: ReportDao, dbOperaionType: Int) : AsyncTask<Report?, Void?, Void?>() {
    private val reportDao: ReportDao = reportDao
    private val dbOperationType: Int = dbOperaionType
    override fun doInBackground(vararg params: Report?): Void? {
        when (dbOperationType) {
            Checker.INSERT_OPERATION -> reportDao.insert(params[0])
            Checker.UPDATE_OPERATION -> reportDao.update(params[0])
            else -> {
            }
        }
        return null
    }


}