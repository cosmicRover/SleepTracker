package com.example.android.trackmysleepquality.sleeptracker

import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

/**An adapter class to adapt the data existing data for recycler view
 * with TextItemViewHolder
 * */

class SleepNightAdapter: RecyclerView.Adapter<TextItemViewHolder>(){
    /**TODO: Implement rest of the required methods*/

    /**Holds the array of sleepnight data for the recyclerView (tableView) to use*/
    var data = listOf<SleepNight>()

    /** The recycler view needs to know how many items to display, thus getItemCount
     * It is like number of rows on swift
     * */
    override fun getItemCount() = data.size

    /**This method customizes the viewholders (cells from swift) for each recylcer view objects */
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()
    }

}