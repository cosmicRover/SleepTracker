package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
create an abstract class for sleep database with android's RoomDatabase
annotate it with Databse that will hold entities for SleepNight, starts at version 1 and exportSchema to false
*/
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase(){
    abstract val sleepDbDao: SleepDatabaseDao //provide reference to the DAO

    companion object{

        /*
        * a volatile var will never be cached and all the changes will be available to all threads
        * all the time
        */
        @Volatile
        private var INSTANCE: SleepDatabase? = null //a var to hold DAO instance

        //return a DAO instance. Takes an application context as param
        fun getInstance(context: Context):SleepDatabase{
            /*
            synchronized makes sure that no duplicate requests to this method exists
             */
            synchronized(this){
                var instance = INSTANCE //takes advantage of kotlin smartCast

                //if database don't exist, build one
                if (instance == null){
                    //pass app context, reference to class, db name. Then fallback, and build
                    instance = Room.databaseBuilder(context.applicationContext,
                            SleepDatabase::class.java, "sleep_history_database")
                            .fallbackToDestructiveMigration().build()
                }

                return instance

            }
        }
    }
}
