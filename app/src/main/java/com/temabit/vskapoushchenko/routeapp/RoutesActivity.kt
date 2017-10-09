package com.temabit.vskapoushchenko.routeapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import com.mapbox.mapboxsdk.geometry.LatLng
import com.temabit.vskapoushchenko.routeapp.adapters.NotificationAdapter
import com.temabit.vskapoushchenko.routeapp.adapters.RouteAdapter
import com.temabit.vskapoushchenko.routeapp.entities.RouteEntity

class RoutesActivity : AppCompatActivity() {
    private var listOfRoutes: List<RouteEntity> = initializeRoutes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        val recyclerView = findViewById(R.id.route_recycler_view) as RecyclerView
        val adapter = RouteAdapter(listOfRoutes)
        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                //if is necessary to avoid starting intent twice
                if (e.action == MotionEvent.ACTION_DOWN) {
                    val child = rv.findChildViewUnder(e.x, e.y)
//                    Toast.makeText(applicationContext, recyclerView.getChildAdapterPosition(child).toString(), Toast.LENGTH_SHORT).show()
                    if (child != null) {
                        val intent = Intent(applicationContext, MapActivity::class.java)
                        intent.putExtra("adapterPosition", recyclerView.getChildAdapterPosition(child))
                        startActivity(intent)
                    }
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    private fun initializeRoutes(): List<RouteEntity>{
        return   listOf(RouteEntity("Трасса 1", listOf(LatLng(56.4430, 32.3726),
                LatLng(54.6244, 27.0200),
                LatLng(51.9063, 33.1200))),
                RouteEntity("Трасса 2", listOf(LatLng(52.8624, 27.4636),
                        LatLng(49.6215, 26.9384),
                        LatLng(51.3557, 35.8365))),
                RouteEntity("Трасса 3", listOf(LatLng(46.8002, 24.1982),
                        LatLng(54.4263, 35.4548),
                        LatLng(50.4978, 37.4370))))
    }
}
