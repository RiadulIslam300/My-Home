package com.saklayen.techhome.db_action

class Table {
    companion object{
        const val DATABASE_NAME  = "tech_home"
        const val DROP_TABLE   = "DROP TABLE IF EXISTS"
        const val DATABASE_VIRSION    = 1

        const val UPDATED_AT = "updated_at"

        const val TABLE_NAME_DEVICE = "device"
        const val DEVICE_ID = "device_id"
        const val DEVICE_NAME = "device_name"
        const val DEVICE_POWER = "power"

        const val CREATE_TABLE_DEVICE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME_DEVICE (" +
                "$DEVICE_ID   INTEGER PRIMARY KEY, " +
                "$DEVICE_NAME  VARCHAR, " +
                "$DEVICE_POWER  VARCHAR, " +
                "$UPDATED_AT DATETIME DEFAULT (DATETIME(CURRENT_TIMESTAMP, 'LOCALTIME')))"

        const val TABLE_NAME_REPORT = "report"
        const val REPORT_ID = "report_id"
        const val YEAR_MONTH = "year_month"
        const val DAY = "day"
        const val START_TIME = "start_time"
        const val END_TIME = "end_time"


        const val CREATE_TABLE_REPORT = "CREATE TABLE IF NOT EXISTS $TABLE_NAME_REPORT (" +
                "$REPORT_ID   INTEGER PRIMARY KEY, " +
                "$DEVICE_ID   INTEGER, " +
                "$YEAR_MONTH  VARCHAR, " +
                "$DAY  VARCHAR, " +
                "$START_TIME  DATETIME, " +
                "$END_TIME  DATETIME, " +
                "$UPDATED_AT DATETIME DEFAULT (DATETIME(CURRENT_TIMESTAMP, 'LOCALTIME')))"
    }
}