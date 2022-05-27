package com.saklayen.techhome.db_action

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase
import android.util.Log


class DataSource(context: Context) {
    private val dBhelper: DB_Helper = DB_Helper(context)
    private var sqLiteDatabase: SQLiteDatabase? = null
    private val context: Context = context

    fun open() {
        sqLiteDatabase = dBhelper.writableDatabase
    }

    fun close() {
        sqLiteDatabase!!.close()
    }

    fun insertDevice(dataMap: HashMap<String, String>): Long {
        open()
        val contentValues = ContentValues()
        for (i in dataMap.keys) {
            contentValues.put(i, dataMap[i])
            Log.e("report data-->", "insertReportInfo: " + i + " --->" + dataMap[i])
        }
       /* val currentTime: Date = Calendar.getInstance().getTime()
        contentValues.put(Tables.USER_PROFILE__created_at, java.lang.String.valueOf(currentTime))*/
        Log.e("content", "insertLevelWiseData:----CONTENT VALUES----->$contentValues")
        val res = sqLiteDatabase!!.insert(Table.TABLE_NAME_DEVICE, null, contentValues)
        Log.e("res---->", "insertDevice: "+res )
        close()
        return res
    }

    fun insertReport(dataMap: HashMap<String, String>): Long {
        open()
        val contentValues = ContentValues()
        for (i in dataMap.keys) {
            contentValues.put(i, dataMap[i])
            Log.e("user data-->", "insertUserInfo: " + i + " --->" + dataMap[i])
        }
        /* val currentTime: Date = Calendar.getInstance().getTime()
         contentValues.put(Tables.USER_PROFILE__created_at, java.lang.String.valueOf(currentTime))*/
        Log.e("content", "insertLevelWiseData:----CONTENT VALUES----->$contentValues")
        val res = sqLiteDatabase!!.insert(Table.TABLE_NAME_REPORT, null, contentValues)
        Log.e("res---->", "insertDevice: "+res )
        close()
        return res
    }

    fun retriveReportId(deviceId: String): String{
        open()
        var id = ""
        val query = "SELECT ${Table.REPORT_ID} from ${Table.TABLE_NAME_REPORT} WHERE ${Table.DEVICE_ID} = $deviceId ORDER BY ${Table.REPORT_ID} DESC"
        val cursor: Cursor  = sqLiteDatabase!!.rawQuery(query,null)
        cursor?.moveToFirst()
        id = cursor.getString(cursor.getColumnIndex(Table.REPORT_ID)).toString()
        cursor.close()
        Log.e("report id", "retriveReportId: $id")
        close()

        return id

    }

    fun retriveDevicePower(deviceId: String): Int{
        open()
        var power = 0
        val query =" SELECT ${Table.DEVICE_POWER} from ${Table.TABLE_NAME_DEVICE} WHERE ${Table.DEVICE_ID} = $deviceId"
        val cursor: Cursor  = sqLiteDatabase!!.rawQuery(query,null)

        if (cursor != null && cursor.count> 0){
            cursor?.moveToFirst()
            power = cursor.getString(cursor.getColumnIndex(Table.DEVICE_POWER)).toInt()
            cursor.close()
        }

        close()

        return power
    }

    fun retriveTotalSecondsUsed(yearMOnth: String, deviceId: String): Int{
        open()
        val i = 0
        var totalTimeInMonth = 0

        val query = "select * from ${Table.TABLE_NAME_REPORT} where ${Table.YEAR_MONTH} = '$yearMOnth' and end_time != \"0\" and ${Table.DEVICE_ID} = '$deviceId' "
        Log.e("query--->", "retriveTotalSecondsUsed: "+query )
        val cursor: Cursor  = sqLiteDatabase!!.rawQuery(query,null)
        if (cursor != null && cursor.count>0){
            cursor.moveToFirst()
            Log.e("cursor count--->", "retriveTotalSecondsUsed: "+cursor.count )
            for (i in 0..cursor.count-1) {
                Log.e("time--->", "retriveTotalSecondsUsed: "+timeUsed(cursor.getString(cursor.getColumnIndex(Table.REPORT_ID))) )
                totalTimeInMonth += timeUsed(cursor.getString(cursor.getColumnIndex(Table.REPORT_ID)))
                //cursor.moveToNext()
                if (!cursor.isLast){
                    cursor.moveToNext()
                }
            }
        }
        Log.e("total sec time--->", "retriveTotalSecondsUsed: "+totalTimeInMonth )

        close()

        return totalTimeInMonth

    }


    fun timeUsed(reportId: String): Int{
        var timeUsedInSeconds = 0
        val differenceQuery = "Select Cast ((\n" +
                "    JulianDay(end_time) - JulianDay(start_time)\n" +
                ") * 24 * 60 * 60 As Integer) AS kaka from report where ${Table.REPORT_ID} = '$reportId'"

        Log.e("kak--", "timeUsed: "+differenceQuery )

        val cursorT: Cursor  = sqLiteDatabase!!.rawQuery(differenceQuery,null)

        if (cursorT != null && cursorT.count>0) {
            cursorT.moveToFirst()
            if (cursorT.getString(cursorT.getColumnIndex("kaka")) != null) {
                timeUsedInSeconds = cursorT.getString(cursorT.getColumnIndex("kaka")).toInt()
            }
            return timeUsedInSeconds
        }
        else{
            return 0
        }


    }

    fun updateReport(reportId: String, userInfoToUpdate: HashMap<String, String>): Long {
        open()
        val columnName = "power"
        val contentValues = ContentValues()
        for (key in userInfoToUpdate.keys ) {
            contentValues.put(key, userInfoToUpdate[key])
        }
        /*val query: String = "UPDATE "+Table.TABLE_NAME_DEVICE+" SET "+columnName+" = "+ userInfoToUpdate[columnName] +"  WHERE "+Table.DEVICE_ID+" = "+deviceId+"";
        val cursor: Cursor  = sqLiteDatabase!!.rawQuery(query,null);*/
        val id = sqLiteDatabase!!.update(Table.TABLE_NAME_REPORT, contentValues, "" + Table.REPORT_ID + " = " + reportId + "", null).toLong()
        close()
        Log.e("update--->", "end time  UPDATE QUERY DONE--------->$id")
        return id
    }

    fun updateDeviceInfo(deviceId: String, userInfoToUpdate: HashMap<String, String>): Long {
        open()
        val columnName = "power"
        val contentValues = ContentValues()
        for (key in userInfoToUpdate.keys ) {
            contentValues.put(key, userInfoToUpdate[key])
        }
        val query: String = "UPDATE "+Table.TABLE_NAME_DEVICE+" SET "+columnName+" = "+ userInfoToUpdate[columnName] +"  WHERE "+Table.DEVICE_ID+" = "+deviceId+"";
        /*val cursor: Cursor  = sqLiteDatabase!!.rawQuery(query,null);*/
        val id = sqLiteDatabase!!.update(Table.TABLE_NAME_DEVICE, contentValues, "" + Table.DEVICE_ID + " = " + deviceId + "", null).toLong()
        close()
        Log.e("update--->", "updateUserInfo: USER INFO UPDATE QUERY DONE--------->$id")
        return id
    }

    fun getDevices(id: String): HashMap<String, String> {
        Log.e("PHONE TEST->", "getUserData: $id")
        open()
        val userData: HashMap<String, String> = HashMap()
        val query = "SELECT * FROM ${Table.TABLE_NAME_DEVICE} WHERE ${Table.DEVICE_ID} = '$id'"
        val cursor111: Cursor? = sqLiteDatabase!!.rawQuery(query, null)
        cursor111!!.moveToFirst()
        if (cursor111 != null && cursor111.count > 0) {
            userData[Table.DEVICE_NAME] = cursor111.getString(cursor111.getColumnIndex(Table.DEVICE_NAME))
            userData[Table.DEVICE_POWER] = cursor111.getString(cursor111.getColumnIndex(Table.DEVICE_POWER))
        }
        close()
        return userData
    }


}