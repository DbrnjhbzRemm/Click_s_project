package com.example.test



import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.second_activity.*



open class MainActivity : AppCompatActivity() {

//    set cost values here
    private val sound_costs = arrayOf<Int>(0,10,50,100,200,300,500,1000,5000)
    private val color_costs = arrayOf<Int>(0,20,40,80,160,400,1200)
    private val upgrade_perclick_cost = 100
    private val upgrade_crit_cost = 10
    private val upgrade_combo_cost = 50
    private val upgrade_perclick_value = 1
    private val upgrade_combo_value = 0
    private val upgrade_crit_value = 0
    private val start_perclick_value = 1
    private val start_crit_value = 0
    private val start_combo_value = 0

    private lateinit var vScore: TextView
    private lateinit var vBrightness: SeekBar
    private lateinit var vVolume: SeekBar
    private lateinit var vUpgrade_perclick: ToggleButton
    private lateinit var vUpgrade_crit: ToggleButton
    private lateinit var vUpgrade_combo: ToggleButton
    private lateinit var soundbuttons: Array<ToggleButton>
    private lateinit var colorbuttons: Array<ToggleButton>

//    private var upgrade_doubleclick: Boolean = false
//    private var upgrade_crit: Boolean = false
//    private var upgrade_combo: Boolean = false

    private var checked_sound: Int = 0
    private var checked_color: Int = 0
    private var clicks_amount: Int = 0

//    set id linking here
    @RequiresApi(Build.VERSION_CODES.M)
    private fun check_cost(b:ToggleButton):Boolean
    {
        var cost:Int
        when (b)
        {
            _1 -> cost=sound_costs[0]
            _2 -> cost=sound_costs[1]
            _3 -> cost=sound_costs[2]
            _4 -> cost=sound_costs[3]
            _5 -> cost=sound_costs[4]
            _6 -> cost=sound_costs[5]
            _7 -> cost=sound_costs[6]
            _8 -> cost=sound_costs[7]
            _9 -> cost=sound_costs[8]
            orange -> cost=color_costs[0]
            purple -> cost=color_costs[1]
            green -> cost=color_costs[2]
            red -> cost=color_costs[3]
            blue -> cost=color_costs[4]
            yellow -> cost=color_costs[5]
            pink -> cost=color_costs[6]
            else -> return false
        }
        return check_availability(b,cost)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun check_availability(b:ToggleButton, cost:Int):Boolean
    {
        if(clicks_amount>=cost)
        {
            b.setTextColor(getColor(R.color.colorFont))
            return true
        }
        else
        {
            b.setTextColor(getColor(R.color.colorClose))
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun score_count()
    {
        vScore.text=clicks_amount.toString()
        for (i in 0..8) check_availability(soundbuttons[i],sound_costs[i])
        for (i in 0..6)
            if (check_availability(colorbuttons[i],color_costs[i]))
                colorbuttons[i].visibility=View.VISIBLE
            else
                colorbuttons[i].visibility=View.GONE
        check_availability(vUpgrade_combo,upgrade_combo_cost)
        check_availability(vUpgrade_crit,upgrade_crit_cost)
        check_availability(vUpgrade_perclick,upgrade_perclick_cost)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.second_activity)
        vScore = score
        vBrightness = seekBar_brightness
        vVolume = seekBar_sound
        vUpgrade_combo = combo
        vUpgrade_crit = crit
        vUpgrade_perclick = perclick
//        vScore.text=clicks_amount.toString()

        soundbuttons = arrayOf(_1,_2,_3,_4,_5,_6,_7,_8,_9)
        colorbuttons = arrayOf(orange, purple, green, red, blue, yellow, pink)
        for (togglebutton in soundbuttons) {
            togglebutton.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked) {
                    if(check_cost(togglebutton)) {
                        buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    }
                } else {
                    buttonView.setBackgroundColor(getColor(R.color.colorPrimary))
                }
            }
        }
        for (togglebutton in colorbuttons) {
//            togglebutton.textOn=" "
//            togglebutton.textOff=" "
            togglebutton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (!check_cost(togglebutton))
                        buttonView.isChecked = false
                }
            }
        }

        vUpgrade_perclick.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_perclick_cost) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    clicks_amount -= upgrade_perclick_cost
                    score_count()
                }
            }
        }
        vUpgrade_combo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_combo_cost) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    clicks_amount -= upgrade_combo_cost
                    score_count()
                }
            }
        }
        vUpgrade_crit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_crit_cost) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    clicks_amount -= upgrade_crit_cost
                    score_count()
                }
            }
        }
        vScore.setOnClickListener {
            clicks_amount+=1
//            vScore.text=clicks_amount.toString()
            score_count()
        }
        score_count()
    }
}
