package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.ToggleButton
import android.widget.EditText
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity: Activity() {

//    var soundbuttonsopened

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val soundbuttons = arrayOf<ToggleButton>(_1,_2,_3,_4,_5,_6,_7,_8,_9)
//        R - all resources
        for (togglebutton in soundbuttons) {
            togglebutton.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))

                } else {
                    buttonView.setBackgroundColor(getColor(R.color.colorPrimary))
                }
            }
        }


    }


}