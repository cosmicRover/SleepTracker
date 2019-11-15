//this class holds the funcs to make database calls using room

package com.example.android.trackmysleepquality.database
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao{
    @Insert //fun for inserting
    fun insert(night: SleepNight)

    @Update //fun for updating a row
    fun update(night: SleepNight)

    @Query(value = "SELECT * from daily_sleep_quality_table WHERE nightId = :key") //sql query to return items if primary key matches
    fun get(key: Long): SleepNight //return a SleepNight data

    @Query(value = "DELETE FROM daily_sleep_quality_table") //delete everything from table
    fun clear()

    @Query(value = "SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")//return everything in a descending order
    fun getAllNights(): LiveData<List<SleepNight>> //returns a list of sleep night live data

    @Query(value = "SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")//get tonight's data
    fun getTonight(): SleepNight? //optional because it can return null
}
