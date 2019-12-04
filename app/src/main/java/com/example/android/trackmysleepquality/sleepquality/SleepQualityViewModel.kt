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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

/**
passing in the key and a databaseDao to the viewModel
 */
class SleepQualityViewModel (private val sleepnightKey: Long = 0L, val databse: SleepDatabaseDao) : ViewModel(){

    /** init a job and a scope for coroutine*/
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /** cancel job on cleared() */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /** init getter and setter to signal transition back to sleep tracker  */
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigeToSleepTracker: LiveData<Boolean?> get() = _navigateToSleepTracker

    /**reset val when navigation is complete*/
    fun onDoneNavigation(){
        _navigateToSleepTracker.value = null
    }

    /**get database and update sleep quality, then signal nav val to navigate*/
    fun onRateSleepQuality(quality: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val tonight = databse.get(sleepnightKey) ?: return@withContext
                tonight.sleepQuality = quality
                databse.update(tonight)
            }
            _navigateToSleepTracker.value = true
        }
    }

}




















