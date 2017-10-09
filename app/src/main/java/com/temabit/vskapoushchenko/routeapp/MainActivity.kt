package com.temabit.vskapoushchenko.routeapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.util.*
import android.content.Intent
import android.util.Log
import android.widget.*
import com.temabit.vskapoushchenko.routeapp.adapters.MyContextWrapper
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    lateinit var chooseLangText : TextView
    lateinit var langSpinner : Spinner
    lateinit var forwardButton : Button
    lateinit var currentLocale : String
    var currentLocaleId : Int? = null

    val localeMap :HashMap<Int, String> = hashMapOf(0 to "en", 1 to "ru", 2 to "uk")

    private val PREF_NAME = "com.temabit.vskapoushchenko.routeapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("info", "Main Activity onCreate()***************")

        chooseLangText = findViewById(R.id.choose_language) as TextView
        langSpinner = findViewById(R.id.spinner) as Spinner
        forwardButton = findViewById(R.id.button_forward) as Button

        val prefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val localeId = prefs.getInt("localeId", 2)
        Log.i("localId", """Locale id is $localeId""")

        val options = resources.getStringArray(R.array.language_list)
        langSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options)

        //Button listener
        forwardButton.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        //The only way to make listener not to run twice
        langSpinner.post {
            langSpinner.setSelection(localeId)
            langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val sharedPref = applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    if(sharedPref.getString("locale", null) == localeMap[position]){
                        return
                    }
                    when(position){
                        0 -> {
                            Log.i("info", "Listeneter pos 0***************")
                            val editor = sharedPref.edit()
                            editor.putString("locale", "en")
                            editor.putInt("localeId", 0)
                            editor.apply()
                        }
                        1 -> {
                            Log.i("info", "Listeneter pos 1***************")
                            val editor = sharedPref.edit()
                            editor.putString("locale", "ru")
                            editor.putInt("localeId", 1)
                            editor.apply()
                        }
                        2 -> {
                            Log.i("info", "Listeneter pos 2***************")
                            val editor = sharedPref.edit()
                            editor.putString("locale", "uk")
                            editor.putInt("localeId", 2)
                            editor.apply()
                        }
                    }
                    Log.i("info", "START NEW ACTIVITY***************")
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        Log.i("info", "Main Activity onattachBaseCo()***************")
        val localePrefs = newBase.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val locale = localePrefs.getString("locale", "en")
        val localeId = localePrefs.getInt("localeId", 0)
        currentLocale = locale
        currentLocaleId = localeId

        val context = MyContextWrapper.wrap(newBase, Locale(currentLocale))
        super.attachBaseContext(context)
    }
}
