package com.slowerror.locationreminders.presentation.ui.reminder_list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slowerror.locationreminders.domain.model.Reminder

class ReminderAdapter(
    private val listener: ReminderClickListener,
    private val switchListener: SwitchClickListener
) : ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder.from(parent = parent)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = getItem(position)
        holder.bind(reminder, reminderClickListener = listener, switchClickListener = switchListener)
    }
}

class ReminderClickListener(val clickListener: (Long) -> Unit) {
    fun onClick(id: Long) = clickListener(id)
}

class SwitchClickListener(val clickListener: (Long) -> Unit) {
    fun onClick(id: Long) = clickListener(id)
}