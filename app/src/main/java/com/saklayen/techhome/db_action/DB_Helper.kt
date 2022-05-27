package com.saklayen.techhome.db_action

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DB_Helper(context: Context?) : SQLiteOpenHelper(context, Table.DATABASE_NAME, null, Table.DATABASE_VIRSION) {
    var oldv = 0
    var newv = 0
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(Table.CREATE_TABLE_DEVICE)
        sqLiteDatabase.execSQL(Table.CREATE_TABLE_REPORT)

        Log.e("DB Create test", "onCreate:  created db")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        oldv = i
        newv = i1
        Log.e("versionDB", "N=$newv old=$oldv")
        if (i == 1 && i1 == 2) {
//            sqLiteDatabase.execSQL(Tables.DROP_TABLE + TABLE_NAME_CLASS);
//            sqLiteDatabase.execSQL(Tables.DROP_TABLE + Tables.TABLE_NAME_TEACHER);
            onCreate(sqLiteDatabase)
            return
        }
        sqLiteDatabase.execSQL(Table.DROP_TABLE + Table.TABLE_NAME_DEVICE)
        sqLiteDatabase.execSQL(Table.DROP_TABLE + Table.TABLE_NAME_REPORT
        )

        onCreate(sqLiteDatabase)
    }
}