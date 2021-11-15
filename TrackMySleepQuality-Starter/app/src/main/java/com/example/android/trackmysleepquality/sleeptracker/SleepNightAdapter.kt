package com.example.android.trackmysleepquality.sleeptracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightDownBinding
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
* Two ways of adding headers
*
* First method
*
* One way to add headers to a list is to modify your adapter to use a different ViewHolder
* by checking indexes where your header needs to be shown.
* The Adapter will be responsible to show a header at the top of the table.
*
* Second method
*
* Another way to add headers is to modify the backing dataset for your data grid.
* You can modify the list to include items to represent a header.
*
* Using a different ViewHolder by checking indexes for headers gives more freedom on
* the layout of the header.
* */

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM_DOWN = 1
private const val ITEM_VIEW_TYPE_ITEM_UP = 2

class SleepNightAdapter(private val clickListener: SleepNightListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(Header)
                else -> listOf(Header) + list.map { SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM_DOWN -> SleepViewHolderDown.from(parent)
            ITEM_VIEW_TYPE_ITEM_UP -> SleepViewHolderUp.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SleepViewHolderDown -> {
                val nightItem = getItem(position) as SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
            is SleepViewHolderUp -> {
                val nightItem = getItem(position) as SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Header -> ITEM_VIEW_TYPE_HEADER
            is SleepNightItem -> if ((getItem(position) as SleepNightItem).sleepNight.sleepQuality < 3) ITEM_VIEW_TYPE_ITEM_DOWN else ITEM_VIEW_TYPE_ITEM_UP
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class SleepViewHolderDown private constructor(private val binding: ListItemSleepNightDownBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            // Useful when using binding adapters in a RecyclerView
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SleepViewHolderDown {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightDownBinding.inflate(layoutInflater, parent, false)
                return SleepViewHolderDown(binding)
            }
        }
    }

    class SleepViewHolderUp private constructor(private val binding: ListItemSleepNightUpBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            // Useful when using binding adapters in a RecyclerView
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SleepViewHolderUp {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightUpBinding.inflate(layoutInflater, parent, false)
                return SleepViewHolderUp(binding)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    /* Two items with equal id are the same */
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    /* Two items with the same properties values are the same */
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

/*
* A sealed class defines a closed type, which means that all subclasses of DataItem must
* be defined in this file. As a result, the number of subclasses is known to the compiler.
*
* It's not possible for another part of your code to define a new type of DataItem that
* could break your adapter.
* */
sealed class DataItem {
    abstract val id: Long
}

data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
    override val id = sleepNight.nightId
}

object Header : DataItem() {
    override val id = Long.MIN_VALUE
}