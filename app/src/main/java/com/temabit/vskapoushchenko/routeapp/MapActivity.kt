package com.temabit.vskapoushchenko.routeapp

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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

class MapActivity : AppCompatActivity(),
                    NavigationView.OnNavigationItemSelectedListener{

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private lateinit var listOfRoutes: List<RouteEntity>
    private lateinit var routeName: TextView
    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView

    private val PREF_NAME = "com.temabit.vskapoushchenko.routeapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listOfRoutes = initializeRoutes()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_map)
        initializeResources()

        //initializing action bar
        toolbar.title = resources.getString(R.string.title_activity_map)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)


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


        // Adding the MapboxMap
        mapView!!.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap

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

    private fun initializeResources(){
        toolbar = findViewById(R.id.toolbar) as Toolbar
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
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

        // Executing the request
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

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    //Mocked method
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
