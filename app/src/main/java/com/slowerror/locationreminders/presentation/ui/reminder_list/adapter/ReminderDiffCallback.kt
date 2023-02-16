package com.slowerror.locationreminders.presentation.ui.reminder_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.slowerror.locationreminders.domain.model.Reminder

class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem == newItem
    }
}