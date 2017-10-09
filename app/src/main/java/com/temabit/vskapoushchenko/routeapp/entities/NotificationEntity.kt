package com.temabit.vskapoushchenko.routeapp.entities

import java.util.*

/**
 * Created by v.skapoushchenko on 05.10.2017.
 */
data class NotificationEntity(val id: Long, val text: String, val date: Date, val type: NotificationType)