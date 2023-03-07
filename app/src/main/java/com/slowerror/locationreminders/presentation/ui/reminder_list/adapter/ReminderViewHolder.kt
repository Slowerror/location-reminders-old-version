package com.slowerror.locationreminders.presentation.ui.reminder_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.slowerror.locationreminders.databinding.ItemReminderBinding
import com.slowerror.locationreminders.domain.model.Reminder

class ReminderViewHolder private constructor(
    private val binding: ItemReminderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        reminder: Reminder,
        reminderClickListener: ReminderClickListener,
        switchClickListener: SwitchClickListener
    ) {
        binding.title.text = reminder.title
        binding.location.text = reminder.namePoi
        binding.geofenceSwitch.setOnClickListener {
            switchClickListener.onClick(reminder.id)
        }

        itemView.setOnClickListener {
            reminderClickListener.onClick(reminder.id)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ReminderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemReminderBinding.inflate(inflater, parent, false)
            return ReminderViewHolder(binding)
        }
    }
}