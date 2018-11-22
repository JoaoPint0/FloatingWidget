package com.jpinto.floatingwidget

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NotificationsAdapter(val viewModel: NotificationsViewModel) : RecyclerView.Adapter<NotificationsViewHolder>() {

    var notificationsList : List<String> = emptyList()

    fun setNotifications(list: List<String>) {
        notificationsList = list
        Log.e("Friends Adapter", "Friends list $notificationsList")
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {

        val notification = notificationsList[position]
        holder.bind(notification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        return NotificationsViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    fun removeItem(adapterPosition: Int) {

        val removed = notificationsList.elementAt(adapterPosition)
        viewModel.removeItem(removed)
    }
}