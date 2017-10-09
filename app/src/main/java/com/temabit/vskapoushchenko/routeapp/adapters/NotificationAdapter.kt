package com.temabit.vskapoushchenko.routeapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.temabit.vskapoushchenko.routeapp.R
import com.temabit.vskapoushchenko.routeapp.entities.NotificationEntity
import java.text.SimpleDateFormat


/**
 * Created by v.skapoushchenko on 05.10.2017.
 */
class NotificationAdapter(val items: List<NotificationEntity>) : RecyclerView.Adapter<NotificationAdapter.MyNotificationHolder>(){
    private var notificationEntityList: List<NotificationEntity> = items
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy")

    class MyNotificationHolder(view: View) : RecyclerView.ViewHolder(view){
        var type: TextView = view.findViewById(R.id.type) as TextView
        var date: TextView = view.findViewById(R.id.date) as TextView
        var content: TextView = view.findViewById(R.id.content) as TextView

    }

    override fun getItemCount(): Int = notificationEntityList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotificationHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_list_raw, parent, false)
        return MyNotificationHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyNotificationHolder, position: Int) {
        val notification = notificationEntityList[position]
        holder.type.text = notification.type.title
        holder.date.text = dateFormatter.format(notification.date)
        holder.content.text = notification.text
    }
}