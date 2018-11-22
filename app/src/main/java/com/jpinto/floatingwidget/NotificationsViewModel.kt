package com.jpinto.floatingwidget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel: ViewModel() {


    private lateinit var notifications: MutableLiveData<List<String>>

    fun getNotifications(): LiveData<List<String>> {
        if (!:: notifications.isInitialized) {
            notifications = MutableLiveData()
            loadNotifications()
        }
        return notifications
    }

    fun loadNotifications(list: List<String> =  emptyList()) {

        notifications.value = list
    }

    fun removeItem(removeItem: String) {
        Log.e("notification" , "${notifications.value}")
        Log.e("removeItem" , "$removeItem")

        val filteredList = notifications.value?.filter { it != removeItem} ?: emptyList()
        Log.e("filteredList" , "$filteredList")
        loadNotifications(filteredList)
    }

}