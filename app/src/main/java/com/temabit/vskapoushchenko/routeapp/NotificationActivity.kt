package com.temabit.vskapoushchenko.routeapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.temabit.vskapoushchenko.routeapp.entities.NotificationEntity
import com.temabit.vskapoushchenko.routeapp.adapters.NotificationAdapter
import com.temabit.vskapoushchenko.routeapp.entities.NotificationType
import java.util.*
import android.content.Intent
import android.util.Log


class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        val adapter = NotificationAdapter(initializeNotifications())
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
                        Log.i("tag", child.toString())
                    }
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }

        })
    }

    private fun initializeNotifications() : List<NotificationEntity>{
        return listOf(
                NotificationEntity(1, "Nivea и FCB S?o Paolo сделали практически невозможное," +
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



