package com.temabit.vskapoushchenko.routeapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.mapbox.mapboxsdk.geometry.LatLng
import com.temabit.vskapoushchenko.routeapp.adapters.MyContextWrapper
import com.temabit.vskapoushchenko.routeapp.adapters.RouteAdapter
import com.temabit.vskapoushchenko.routeapp.entities.RouteEntity
import java.util.*

class RoutesActivity : AppCompatActivity(),
                       NavigationView.OnNavigationItemSelectedListener{
    private var listOfRoutes: List<RouteEntity> = initializeRoutes()
    private lateinit var recyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView

    private val PREF_NAME = "com.temabit.vskapoushchenko.routeapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)
        initializeResources()

        //initializing action bar
        toolbar.title = resources.getString(R.string.title_activity_routes)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        val adapter = RouteAdapter(listOfRoutes)
        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(RoutesTouchListener())
    }

    inner class RoutesTouchListener : RecyclerView.OnItemTouchListener{
        override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

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

    private fun initializeResources(){
        toolbar = findViewById(R.id.toolbar) as Toolbar
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
        recyclerView = findViewById(R.id.route_recycler_view) as RecyclerView
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

    override fun attachBaseContext(newBase: Context) {
        Log.i("info", "Notification Activity onattachBaseCo()***************")
        val localePrefs = newBase.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val locale = localePrefs.getString("locale", "en")

        val context = MyContextWrapper.wrap(newBase, Locale(locale))
        super.attachBaseContext(context)
    }
}
