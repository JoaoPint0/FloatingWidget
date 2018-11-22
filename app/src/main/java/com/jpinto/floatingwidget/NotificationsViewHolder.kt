package com.jpinto.floatingwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notification_view_item.view.*

class NotificationsViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    fun bind(notification: String?) {

        if (notification == null) {
            view.notification_type.text = "Loading"
        } else {
            showNotification(notification)
        }
    }

    private fun showNotification(notification: String) {

        view.notification_type.text = notification
    }


    companion object {
        fun create(parent: ViewGroup): NotificationsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_view_item, parent, false)
            return NotificationsViewHolder(view)
        }
    }
}
