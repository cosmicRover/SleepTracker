/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 * It takes in the database, applicationContext and returns it as
 * AndroidViewModel with application as parameter
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {
    /**co-routines are comprised of a job, scope
     *
     */

    private var viewModelJob = Job() //create a job
    //create an UiScope with a job that will run on the main thread
    //this scope can run on the main thread because when it returns, it will update th UI
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //keep track of tonight as mutable live data
    private var tonight = MutableLiveData<SleepNight?>()

    /**Navigation to SleepQuality fragment setup
     * making it a mutable live data and private val and use a getter to get it's value from
     * other classes.
     */

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight> get() = _navigateToSleepQuality //getter for above val

    //reset navigation val
    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }

    //initializer for this class (viewDidLoad())
    //note that init is not on the top of the class since
    //fun initializeTonight() depends on vals declared above
    init {
        initializeTonight()
    }

    //get all the data from database
    private val nights = database.getAllNights()

    //format nights to proper strings
    val formattedNights = Transformations.map(nights){ nights ->
        //use an util func to format the nights
        formatNights(nights, application.resources)
    }


    //launch a co-routine to init data for tonight
    private fun initializeTonight(){
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    //get tonight from database. Mark it as suspend because we are using it from a co-routine
    //it returns an optional SleepNight
    private suspend fun getTonightFromDatabase():SleepNight?{
        return withContext(Dispatchers.IO){//.IO runs on an optimized background thread
            var night = database.getTonight()

            if (night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }

    fun onStartTracking(){
        uiScope.launch{
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight){
        withContext(Dispatchers.IO){//.IO runs on an optimized background thread
            database.insert(night)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            val oldNight = tonight.value?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight //set the value for live data var
        }

    }

    private suspend fun update(night: SleepNight){
        withContext(Dispatchers.IO){
            database.update(night)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            tonight.value = null
            displaySnack()
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }


    //gotta cancel the jobs in this viewModel on viewDismiss
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**transformation maps. Turn button visible/invisible based on the state of other vals
     * they are linked directly to the layout using the viewmodel val there
     * */
    val startVisible = Transformations.map(tonight){
        null == it
    }

    val stopVisible = Transformations.map(tonight){
        null != it
    }

    val clearVisible = Transformations.map(nights){
        it?.isNotEmpty()
    }

    private var _snowSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean> get()= _snowSnackbar

    fun displaySnack(){
        _snowSnackbar.value = true
    }

    fun resetSnackbar(){
        _snowSnackbar.value = false
    }

}

