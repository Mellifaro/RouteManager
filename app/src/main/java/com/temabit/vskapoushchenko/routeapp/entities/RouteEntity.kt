package com.temabit.vskapoushchenko.routeapp.entities

import com.mapbox.mapboxsdk.geometry.LatLng

/**
 * Created by v.skapoushchenko on 06.10.2017.
 */
data class RouteEntity(val name: String, val listOfPoints: List<LatLng>)