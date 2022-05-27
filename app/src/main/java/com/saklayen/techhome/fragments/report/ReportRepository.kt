package com.saklayen.techhome.fragments.report

import android.app.Application
import androidx.lifecycle.LiveData
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.models.Report
import com.saklayen.techhome.room.dao.DeviceDao
import com.saklayen.techhome.room.dao.ReportDao
import com.saklayen.techhome.room.database.Device_OperationAsyncTask
import com.saklayen.techhome.room.database.Report_OperationAsyncTask
import com.saklayen.techhome.room.database.TechHomeDatabase
import com.saklayen.techhome.utils.Checker
import java.time.YearMonth

class ReportRepository internal constructor(application: Application, reportDao: ReportDao?, yearMonth: YearMonth) {
    private var reportDao = reportDao
    private  var allReports: LiveData<List<Report?>?>? = null
    fun insert(report: Report?) {
        reportDao!!.insert(report)
    }

    fun getAllReports(yearMonth: YearMonth): LiveData<List<Report?>?>? {
        return allReports
    }

    init {
        val dataBase = TechHomeDatabase.INSTANCE
        if (dataBase != null) {
            allReports = reportDao!!.getReport(yearMonth)
        }

    }
}