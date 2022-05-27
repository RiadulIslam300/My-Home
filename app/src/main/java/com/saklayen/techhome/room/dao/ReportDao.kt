package com.saklayen.techhome.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.models.Report
import java.sql.Date
import java.time.YearMonth

@Dao
interface ReportDao {

    @Insert
    fun insert(report: Report?)

    @Update
    fun update(report: Report?)
    @Query("SELECT * FROM report_table WHERE year_month = :yrMonth ")
    fun getReport(yrMonth: YearMonth): LiveData<List<Report?>?>?

}