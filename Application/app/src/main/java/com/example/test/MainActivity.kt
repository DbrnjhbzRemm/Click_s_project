package com.example.test



import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.second_activity.*
import java.io.InputStream
import java.io.OutputStream
import java.util.*



open class MainActivity : AppCompatActivity() {

    //val bt_mac:String = "98:D3:31:F6:23:69" //Miks bluetooth
//    val bt_mac:String = "98:D3:31:20:47:FB" //our bluetooth
//    set cost values here
    private val sound_costs = arrayOf<Int>(0,200,200,200,200,200,200,200,200)
    private val color_costs = arrayOf<Int>(0,150,150,150,150,150,150,150)
    private val upgrade_perclick_cost = 200
    private val upgrade_crit_cost = 400
    private val upgrade_combo_cost = 350
    private val upgrade_perclick_value = 1
    private val upgrade_combo_value = 3
    private val upgrade_crit_chance = 40
    private val start_perclick_value = 1
    private val start_crit_chance = 30
    private val start_combo_value = 2

    private lateinit var vScore: TextView
    private lateinit var vBrightness: SeekBar
    private lateinit var vVolume: SeekBar
    private lateinit var vUpgrade_perclick: ToggleButton
    private lateinit var vUpgrade_crit: ToggleButton
    private lateinit var vUpgrade_combo: ToggleButton
    private lateinit var soundbuttons: Array<ToggleButton>
    private lateinit var colorbuttons: Array<ToggleButton>

    //set start variables here
    private var upgrade_doubleclick: Boolean = false
    private var upgrade_crit: Boolean = false
    private var upgrade_combo: Boolean = false
    private var checked_sound: Int = 0
    private var checked_color: Int = 0
    private var clicks_amount: Int = 148
    private var colors_opened = arrayOf<Boolean>(false, false, false, false, false, false, false, false)
    private var sounds_opened = arrayOf<Boolean>(false, false, false, false, false, false, false, false, false)
    lateinit var OS:OutputStream
    lateinit var IS:InputStream

//    set id linking here
    @RequiresApi(Build.VERSION_CODES.M) //gets cost of element and returns can you buy it or not
    private fun check_cost(b:ToggleButton):Boolean
    {
        val cost:Int
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
            white -> cost=color_costs[0]
            orange -> cost=color_costs[1]
            purple -> cost=color_costs[2]
            green -> cost=color_costs[3]
            red -> cost=color_costs[4]
            blue -> cost=color_costs[5]
            yellow -> cost=color_costs[6]
            pink -> cost=color_costs[7]
            else -> cost=0
        }
//        if(cost>0)
            return check_availability(b,cost, false)
//        else
//            return true
    }

    private fun get_num_sound(b:CompoundButton):String{//полный пиздец
        val num:String
        when (b)
        {
            _1 -> num="1"
            _2 -> num="2"
            _3 -> num="3"
            _4 -> num="4"
            _5 -> num="5"
            _6 -> num="6"
            _7 -> num="7"
            _8 -> num="8"
            _9 -> num="9"
            else -> num="0"
        }
        return num
    }

    @RequiresApi(Build.VERSION_CODES.M) //check if you can or can not buy smth, setcol=True sets text color available or not
    private fun check_availability(b:ToggleButton, cost:Int, setcol:Boolean):Boolean
    {
        if(clicks_amount>=cost)
        {
            if (setcol)
                b.setTextColor(getColor(R.color.colorFont))
            return true
        }
        else
        {
            if (setcol)
                b.setTextColor(getColor(R.color.colorClose))
            return false
        }
    }

    // parse three color ints from hex and send them to bt
    fun btsend_color(buttonView: CompoundButton)
    {
        val col:Int
        when(buttonView)
        {
            white -> col=R.color.colorFont
            orange -> col=R.color.colorOrangeButton
            purple -> col=R.color.colorPurpleButton
            green -> col=R.color.colorGreenButton
            red -> col=R.color.colorRedButton
            blue -> col=R.color.colorBlueButton
            yellow -> col=R.color.colorYellowButton
            pink -> col=R.color.colorPinkButton
            else -> col=0
        }
        val col_red=Color.red(col)
        val col_blue=Color.blue(col)
        val col_green=Color.green(col)
        btsend("sred $col_red")
        btsend("sblu $col_blue")
        btsend("sgre $col_green")
    }

    @RequiresApi(Build.VERSION_CODES.M)//checks every component can you use it or not, reveals it
    private fun score_count()
    {
        vScore.text=clicks_amount.toString()
        for (i in 0..8)
            if (!sounds_opened[i] && check_availability(soundbuttons[i],sound_costs[i],false)) {
                sounds_opened[i] = true
                check_availability(soundbuttons[i],sound_costs[i],true)
            }
//            check_availability(soundbuttons[i],sound_costs[i],true)
        for (i in 0..7)
            if (check_availability(colorbuttons[i],color_costs[i],false)) {
                colorbuttons[i].visibility = View.VISIBLE
                colors_opened[i] = true
            }
            else {
                if (!colors_opened[i])
                    colorbuttons[i].visibility = View.GONE
            }
        if(!upgrade_combo)  check_availability(vUpgrade_combo,upgrade_combo_cost,true)
        if(!upgrade_crit)   check_availability(vUpgrade_crit,upgrade_crit_cost,true)
        if(!upgrade_doubleclick)    check_availability(vUpgrade_perclick,upgrade_perclick_cost,true)
    }

    //send string via bluetooth
    private fun btsend(comand:String)
    {
        OS.write(comand.toByteArray())
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

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//        val UUID =

        soundbuttons = arrayOf(_1,_2,_3,_4,_5,_6,_7,_8,_9)
        colorbuttons = arrayOf(white,orange, purple, green, red, blue, yellow, pink)
        for (togglebutton in soundbuttons) {
            togglebutton.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked && sounds_opened[1]) {// это страшный костыль, но дедлайн горит, прошу прощения перед читающим это кем бы он ни был
                    soundbuttons.forEach {
                        if (it != togglebutton) it.isChecked = false
                    }
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    btsend("strc "+get_num_sound(buttonView))
                } else {
                    buttonView.setBackgroundColor(getColor(R.color.colorPrimary))
//                    buttonView.isChecked=true
                }
            }
        }
        for (togglebutton in colorbuttons) {
            togglebutton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    colorbuttons.forEach {
                        if (it != togglebutton) it.isChecked = false
                    }
                    if (!check_cost(togglebutton) && !colors_opened[1]) //страшный костыль
                        buttonView.isChecked = false
                    btsend_color(buttonView)
                }
            }
        }

        vUpgrade_perclick.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_perclick_cost && !upgrade_doubleclick) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    clicks_amount -= upgrade_perclick_cost
                    upgrade_doubleclick=true
                    btsend("spcl 2")
                    score_count()
                }
            }
        }
        vUpgrade_combo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_combo_cost && !upgrade_combo) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    clicks_amount -= upgrade_combo_cost
                    upgrade_combo=true
                    btsend("comu 5")
                    score_count()
                }
            }
        }
        vUpgrade_crit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_crit_cost && !upgrade_crit) {
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    clicks_amount -= upgrade_crit_cost
                    upgrade_crit=true
                    btsend("ccha 70")
                    score_count()
                }
            }
        }
        vScore.setOnClickListener {
            clicks_amount+=1
//            vScore.text=clicks_amount.toString()
            score_count()
        }

        seekBar_brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val sto:Float=100f
                var a:Float=i/sto
                btsend("sbrt $a")
//                bright.text="sbrt $a"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        seekBar_sound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var a:Int=i/3
                btsend("sbrt $a")
//                sound.text="sbrt $a"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

//        val device = bluetoothAdapter?.getRemoteDevice("98:D3:31:20:47:FB")

//        bluetoothAdapter.bondedDevices()
//        val bt_device: BluetoothDevice
//        var k=0
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        Log.d("","start")
        if (pairedDevices!=null&&pairedDevices.isNotEmpty())
            pairedDevices.forEach { device ->
//                soundbuttons[k].text=device.name
//                if (device.address == bt_mac) {
                if(device.name.equals("HC-06")){
                    Log.d("","found")
                    val bt_device: BluetoothDevice = device
                    if(bt_device.createBond())
                        Log.d("","device bonded")
                    val bt_socket: BluetoothSocket =
                        bt_device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                    bt_socket.connect()
                    if(bt_socket.isConnected)
                        Log.d("","device connected")
                    OS = bt_socket.outputStream
                    IS = bt_socket.inputStream

                }
//                k+=1
            }

        soundbuttons[0].isChecked=true
        colorbuttons[0].isChecked=true

        score_count()
    }
}
