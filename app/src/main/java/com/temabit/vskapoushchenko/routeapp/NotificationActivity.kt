package com.temabit.vskapoushchenko.routeapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.temabit.vskapoushchenko.routeapp.entities.NotificationEntity
import com.temabit.vskapoushchenko.routeapp.adapters.NotificationAdapter
import com.temabit.vskapoushchenko.routeapp.entities.NotificationType
import java.util.*
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.temabit.vskapoushchenko.routeapp.adapters.MyContextWrapper


class NotificationActivity : AppCompatActivity(),
                             NavigationView.OnNavigationItemSelectedListener{

    private lateinit var recyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView

    private val PREF_NAME = "com.temabit.vskapoushchenko.routeapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initializeRecources()
        setSupportActionBar(toolbar)

        //initializing action bar
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        //Receiving and populating recycler view with notifications
        val adapter = NotificationAdapter(initializeNotifications())
        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(NotificationTochListener())
    }


    inner class NotificationTochListener : RecyclerView.OnItemTouchListener{
        override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            if (e.action == MotionEvent.ACTION_UP) {
                val child = rv.findChildViewUnder(e.x, e.y)
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
    }

    private fun initializeRecources(){
        toolbar = findViewById(R.id.toolbar) as Toolbar
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
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
        menuInflater.inflate(R.menu.notification, menu)
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

    //Configuring activity with locale saved in shared preferences
    override fun attachBaseContext(newBase: Context) {
        Log.i("info", "Notification Activity onattachBaseCo()***************")
        val localePrefs = newBase.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val locale = localePrefs.getString("locale", "en")

        val context = MyContextWrapper.wrap(newBase, Locale(locale))
        super.attachBaseContext(context)
    }

    //Mocked list of notifications
    private fun initializeNotifications() : List<NotificationEntity>{
        return listOf(
                NotificationEntity(1, "Nivea и FCB So Paolo сделали практически невозможное," +
                        " и соединили солнцезащитный крем, чтение журнала на пляже и отслеживание ребенка в одной" +
                        " печатной рекламе. На странице был размещен съемный браслет из водостойкой бумаги, который " +
                        "можно прикрепить на запястье ребенка, загрузить приложение и синхронизировать его с браслетом." +
                        " Родители могут установить предел расстояния, насколько далеко их малыш может гулять и " +
                        "получают уведомление, если это расстояние пересечено. Так что можно сидеть, расслабляться" +
                        " и наслаждаться отдыхом благодаря этой рекламе.",
                        Date(System.currentTimeMillis()), NotificationType.ADVERTISEMENT_NOTIFICATION),

                NotificationEntity(2, "Glacial задумался над проблемой, с которой потребители сталкиваются," +
                        " и решил ее с помощью рекламы..." +
                        "Это рекламное объявление в журнале помогает клиентам охладить бутылки с пивом в два раза быстрее. " +
                        "Все, что нужно читателю, - это намочить рекламу водой, обернуть ее вокруг пива и поставить в морозильник," +
                        " чтобы охлаждение заняло в половину меньше времени, чем обычно. Как? Реклама содержит частицы соли, которые" +
                        " помогают ускорить процесс.",
                        Date(System.currentTimeMillis()), NotificationType.ADVERTISEMENT_NOTIFICATION),

                NotificationEntity(3, "This notification is used when a new ticket is created in any of the categories",
                        Date(System.currentTimeMillis()), NotificationType.SERVICE_NOTIFICATION))
    }
}



