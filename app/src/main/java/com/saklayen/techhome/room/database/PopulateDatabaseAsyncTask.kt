package com.saklayen.techhome.room.database

import android.os.AsyncTask
import com.saklayen.techhome.note.Utils
import com.saklayen.techhome.utils.Checker

class PopulateDatabaseAsyncTask internal constructor(db: TechHomeDatabase, operationType: Int, operationTable: Int) : AsyncTask<Void, Void, Void>() {
    private val deviceDao = db.deviceDao()
    private val reportDao = db.reportDao()
    val operationType = operationType
    val operationTable = operationTable
    override fun doInBackground(vararg params: Void): Void? {
        if (operationTable == Checker.deviceOperations){
            if (operationType == Checker.INSERT_OPERATION){
                deviceDao!!.insert(Checker.device)
            }
            else if (operationType == Checker.UPDATE_OPERATION){
                deviceDao!!.update(Checker.device)
            }
        }
        else if (operationTable == Checker.reportOperations){
            if (operationType == Checker.INSERT_OPERATION){
                reportDao!!.insert(Checker.report)
            }
            else if (operationType == Checker.UPDATE_OPERATION){
                reportDao!!.update(Checker.report)
            }
        }

        return null
    }
}