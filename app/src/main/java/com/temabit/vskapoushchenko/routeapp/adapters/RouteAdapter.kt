package com.temabit.vskapoushchenko.routeapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.temabit.vskapoushchenko.routeapp.R
import com.temabit.vskapoushchenko.routeapp.entities.RouteEntity

/**
 * Created by v.skapoushchenko on 06.10.2017.
 */
class RouteAdapter(items: List<RouteEntity>) : RecyclerView.Adapter<RouteAdapter.MyRouteHolder>() {

    private var routeEntityList: List<RouteEntity> = items

    class MyRouteHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title = view.findViewById(R.id.route_title) as TextView
        var coordinates = view.findViewById(R.id.coordinates) as TextView
    }

    override fun onBindViewHolder(holder: RouteAdapter.MyRouteHolder, position: Int) {
        val route = routeEntityList[position]
        holder.title.text = route.name
        holder.coordinates.text = convertCoordsToString(route)
    }

    override fun getItemCount(): Int = routeEntityList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteAdapter.MyRouteHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.route_list_raw, parent, false)
        return MyRouteHolder(itemView)
    }

    private fun convertCoordsToString(route: RouteEntity): String {
        val result = StringBuffer("")
        val listOfPoints = route.listOfPoints
        for(i in 0 until listOfPoints.size){
            val point = listOfPoints[i]
            result.append(i + 1).append(". Latitude = ").append(point.latitude).append(", Longitude = ").append(point.longitude).append(";\n")
        }
        return result.toString()
    }

}