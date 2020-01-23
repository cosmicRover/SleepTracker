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

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

/**RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() for the regular recycle view but ListAdapter
 * is for efficient recycleView update operations. It calls SleepNightDiffCallback() to figure out
 * if it needs to update anything
 * */
class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    /**store data and return the size, and a setter to notify when the value had changed*/
//    var data =  listOf<SleepNight>()
//        set(value){
//        field = value
//            notifyDataSetChanged() //reloads everything! Very slow operation for large data sets (tableView.reloadData() o_o)
//        }

//    override fun getItemCount() = data.size, don't need this one ListAdapter

    /**Bind the ViewHolder(cell) properties to their given positions from data. holder references to ViewHolder class */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get position and view (cell) resources
        val item = getItem(position) /**ListAdapter way of getting the item*/
        val res = holder.itemView.context.resources

        //assign the resources
        holder.bindData(item, res)
    }

    /**Gets called when RecyclerView needs a new view holder. Using the ViewHolder view(cell) class */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate layout, create a view from existing resource and pass it to recycler view
        return ViewHolder.from(parent)
    }

    /**create a class for the newly created view holder and extend RecyclerView*/
    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        private val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        private val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
        private val quality: TextView = itemView.findViewById(R.id.quality_string)

        fun bindData(item: SleepNight, res: Resources) {
            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQuality, res)
            qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
        }

        /**extracted the view inflation code to a companion object*/
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(view)
            }
        }

    }
}

class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>(){

    /**if items have the same id, they are same*/
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    /***/
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
       return false
    }

}



















