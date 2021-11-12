package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.SleepViewHolder>(SleepNightDiffCallback()) {

    /*
     When notifyDataSetChanged() is called, the RecyclerView redraws the whole list,
     not just the changed items. This marks the entire list as potentially invalid.
     As a result, RecyclerView rebinds and redraws every item in the list,
     including items that are not visible on screen. This is a lot of unnecessary work.
     RecyclerView has a class called DiffUtil which is for calculating the differences
     between two lists.
     It uses an algorithm called Eugene W. Myers's difference algorithm to figure out
     the minimum number of changes to make from the old list to produce the new list.
     Once DiffUtil figures out what has changed, RecyclerView can use that information
     to update only the items that were changed, added, removed, or moved, which is much
     more efficient than redoing the entire list.
     */
/*    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/


    /**
     * Called when RecyclerView needs a new [SleepViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        return SleepViewHolder.from(parent)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [SleepViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [SleepViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
//        val item: SleepNight = data[position]
        val item: SleepNight = getItem(position)
        holder.bind(item)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
//    override fun getItemCount() = data.size

    class SleepViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SleepNight
        ) {
            binding.sleep = item
            // Usefull when using binding adapters in a RecyclerView
            binding.executePendingBindings()
//            val res: Resources = itemView.context.resources
//                /*        val sleepLength: TextView = binding.findViewById(R.id.sleep_length)
//        val quality: TextView = binding.findViewById(R.id.quality_string)
//        val qualityImage: ImageView = binding.findViewById(R.id.quality_image)*/
//            binding.sleepLength.text =
//                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
//            binding.qualityImage.setImageResource(
//                when (item.sleepQuality) {
//                    0 -> R.drawable.ic_sleep_0
//                    1 -> R.drawable.ic_sleep_1
//                    2 -> R.drawable.ic_sleep_2
//                    3 -> R.drawable.ic_sleep_3
//                    4 -> R.drawable.ic_sleep_4
//                    5 -> R.drawable.ic_sleep_5
//                    else -> R.drawable.ic_sleep_active
//                }
//            )
        }
        companion object {
            fun from(parent: ViewGroup): SleepViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val view: View = layoutInflater
//                    .inflate(R.layout.list_item_sleep_night, parent, false)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
//                return SleepViewHolder(view)
                return SleepViewHolder(binding)
            }
        }
    }

}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight) =
        oldItem.nightId == newItem.nightId

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight)=
        oldItem == newItem

}