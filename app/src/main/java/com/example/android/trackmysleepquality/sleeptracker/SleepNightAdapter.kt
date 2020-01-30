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
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

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
        holder.bindData(item)
    }

    /**Gets called when RecyclerView needs a new view holder. Using the ViewHolder view(cell) class */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate layout, create a view from existing resource and pass it to recycler view
        return ViewHolder.from(parent)
    }

    /**create a class for the newly created view holder and extend RecyclerView*/
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root){

        fun bindData(item: SleepNight) {
            binding.sleep = item
            binding.executePendingBindings() //let binding adapter bind the pending data by itself
        }

        /**extracted the view inflation code to a companion object*/
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

//                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(binding)
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



















