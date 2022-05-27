package com.saklayen.techhome.room.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.models.Report
import com.saklayen.techhome.room.dao.DeviceDao
import com.saklayen.techhome.room.dao.ReportDao

@Database(entities = [Device::class,Report::class], version = 1)
abstract class TechHomeDatabase : RoomDatabase() {


    abstract fun deviceDao(): DeviceDao?
    abstract fun reportDao(): ReportDao?

    companion object {
        var INSTANCE: TechHomeDatabase? = null
        fun getDatabase(context: Context): TechHomeDatabase {
            if (INSTANCE == null) {
                synchronized(TechHomeDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(context.applicationContext, TechHomeDatabase::class.java,
                                "tech_home_database")
                                .fallbackToDestructiveMigration()
                                .addCallback(sRoomDatabaseCallback)
                                .build()
                    }
                }
            }
            return this.INSTANCE!!
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}