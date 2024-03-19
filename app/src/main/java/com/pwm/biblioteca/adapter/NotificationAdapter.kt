package com.pwm.biblioteca.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pwm.biblioteca.utils.Notification
import com.pwm.biblioteca.databinding.LayoutNotificaBinding

class NotificationAdapter(private var notificationList: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private var binding: LayoutNotificaBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(notification: Notification){
            binding.tvNotifica.text = notification.testo

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var binding = LayoutNotificaBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification)
    }
}