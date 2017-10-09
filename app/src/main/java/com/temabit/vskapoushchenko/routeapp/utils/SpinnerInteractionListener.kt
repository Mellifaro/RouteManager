package com.temabit.vskapoushchenko.routeapp.utils

import android.view.View
import android.widget.AdapterView
import android.view.MotionEvent



/**
 * Created by v.skapoushchenko on 04.10.2017.
 */
class SpinnerInteractionListener : AdapterView.OnItemSelectedListener, View.OnTouchListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    var userSelect = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        userSelect = true
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        if (userSelect) {
            // Your selection handling code here

            userSelect = false
        }
    }

}