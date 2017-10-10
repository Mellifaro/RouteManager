package com.temabit.vskapoushchenko.routeapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.util.*
import android.content.Intent
import android.content.SharedPreferences
import android.widget.*
import com.temabit.vskapoushchenko.routeapp.adapters.MyContextWrapper
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private lateinit var chooseLangText : TextView
    private lateinit var langSpinner : Spinner
    private lateinit var forwardButton : Button
    private lateinit var currentLocale : String
    private var currentLocaleId : Int? = null
    private lateinit var sharedPreferences : SharedPreferences

    private val localeMap :HashMap<Int, String> = hashMapOf(0 to "en", 1 to "ru", 2 to "uk")

    private val PREF_NAME = "com.temabit.vskapoushchenko.routeapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeRecources()

        val languageList = resources.getStringArray(R.array.language_list)
        langSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languageList)

        forwardButton.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        //OnItemTouchListener for spinner
        langSpinner.post {
            val localeId = sharedPreferences.getInt("localeId", 2)
            langSpinner.setSelection(localeId)
            langSpinner.onItemSelectedListener = SpinnerOnItemTouchListener()
        }
    }

    inner class SpinnerOnItemTouchListener : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if(sharedPreferences.getString("locale", null) == localeMap[position]){
                return
            }

            val editor = sharedPreferences.edit()
            when(position){
                0 -> {
                    editor.putString("locale", "en")
                    editor.putInt("localeId", 0)
                }
                1 -> {
                    editor.putString("locale", "ru")
                    editor.putInt("localeId", 1)
                }
                2 -> {
                    editor.putString("locale", "uk")
                    editor.putInt("localeId", 2)
                }
            }
            editor.apply()

            val intent = Intent(applicationContext, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun initializeRecources(){
        chooseLangText = findViewById(R.id.choose_language) as TextView
        langSpinner = findViewById(R.id.spinner) as Spinner
        forwardButton = findViewById(R.id.button_forward) as Button
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun attachBaseContext(newBase: Context) {
        val localePrefs = newBase.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val locale = localePrefs.getString("locale", "en")
        val localeId = localePrefs.getInt("localeId", 0)
        currentLocale = locale
        currentLocaleId = localeId

        val context = MyContextWrapper.wrap(newBase, Locale(currentLocale))
        super.attachBaseContext(context)
    }
}
