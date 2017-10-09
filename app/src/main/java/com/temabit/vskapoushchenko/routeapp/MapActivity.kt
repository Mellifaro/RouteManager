package com.temabit.vskapoushchenko.routeapp

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import com.mapbox.directions.DirectionsCriteria
import com.mapbox.directions.MapboxDirections
import com.mapbox.directions.service.models.DirectionsResponse
import com.mapbox.directions.service.models.Waypoint
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.temabit.vskapoushchenko.routeapp.entities.RouteEntity
import retrofit.Response
import java.io.IOException

class MapActivity : AppCompatActivity() {
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private lateinit var listOfRoutes: List<RouteEntity>
    private lateinit var routeName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listOfRoutes = initializeRoutes()

        //!!!!Change and correct exception
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_map)


        val routePosition = intent.getIntExtra("adapterPosition", 0)
        routeName = findViewById(R.id.route_name) as TextView
        routeName.text = listOfRoutes[routePosition].name

        // Create a mapView
        mapView = findViewById(R.id.mapview) as MapView
        mapView!!.onCreate(savedInstanceState)

        val routesButton = findViewById(R.id.routes_button)
        routesButton.setOnClickListener{
            val intent = Intent(this, RoutesActivity::class.java)
            startActivity(intent)
        }


        // Add a MapboxMap
        mapView!!.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap

            //Centre camera
            mapboxMap.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(50.27, 30.31))
                    .zoom(4.0)
                    .build()

            val routeEntity = listOfRoutes[routePosition]
            val listOfPoints = routeEntity.listOfPoints
            listOfPoints.forEach {
                mapboxMap.addMarker(MarkerViewOptions()
                        .position(it))
            }
            for(i in 0..listOfPoints.size-2) {
                drawRoute(listOfPoints[i], listOfPoints[i+1])
            }
        }
    }

    private fun drawRoute(start: LatLng, finish: LatLng) {
        val startWaypoint = Waypoint(start.longitude, start.latitude)
        val finishWaypoint = Waypoint(finish.longitude, finish.latitude)

        val client = MapboxDirections.Builder()
                .setAccessToken(getString(R.string.access_token))
                .setOrigin(startWaypoint)
                .setDestination(finishWaypoint)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .build()

        // Execute the request
        var response: Response<DirectionsResponse>? = null
        try {
            response = client.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val route = response!!.body().routes[0]
        val waypoints = route.geometry.waypoints

        val points = arrayOfNulls<LatLng>(waypoints.size)

        for (i in 0 ..(waypoints.size - 1)) {
            points[i] = LatLng(waypoints[i].latitude, waypoints[i].longitude)
        }

        val polyline = mapboxMap?.addPolyline(PolylineOptions()
                .add(*points)
                .color(Color.parseColor("#be1212"))
                .width(4f))
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
