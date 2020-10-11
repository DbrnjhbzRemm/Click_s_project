package com.example.test



import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
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

//    private val TAG = "MyApp"

    private val sound_costs = arrayOf<Int>(0,200,200,200,200,200,200,200,200)
    private val color_costs = arrayOf<Int>(0,50,50,50,50,50,50,50)
    private val upgrade_perclick_cost = 400
    private val upgrade_crit_cost = 100
    private val upgrade_combo_cost = 300
//    private val upgrade_perclick_value = 1
//    private val upgrade_combo_value = 3
//    private val upgrade_crit_chance = 40
//    private val start_perclick_value = 1
//    private val start_crit_chance = 30
//    private val start_combo_value = 2

    //debug
    private val send_init_score:Boolean = true
    private val pizdec:Boolean = false
    private val upgrade_price_payment:Boolean = false

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
//    private var checked_sound: Int = 0
//    private var checked_color: Int = 0
    private var clicks_amount: Int = 0
    private var colors_opened = arrayOf<Boolean>(false, false, false, false, false, false, false, false)
    private var sounds_opened = arrayOf<Boolean>(false, false, false, false, false, false, false, false, false)
    lateinit var OS:OutputStream
    lateinit var IS:InputStream
//    lateinit var bt_socket:BluetoothSocket
    val MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB")
    private lateinit var bluetoothAdapter: BluetoothAdapter


//    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
//
//        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
//            device.createRfcommSocketToServiceRecord(MY_UUID)
//        }
////        private lateinit var mmSocket:BluetoothSocket
//
//        public override fun run() {
//            // Cancel discovery because it otherwise slows down the connection.
//            // !!! Only the original thread that created a view hierarchy can touch its views.
//            bluetoothAdapter.cancelDiscovery()
////            mmSocket?.connect() //java.io.IOException: read failed, socket might closed or timeout, read ret: -1
//
//            mmSocket?.use { socket ->
////                 Connect to the remote device through the socket. This call blocks
////                 until it succeeds or throws an exception.
//                socket.connect()
////                 The connection attempt succeeded. Perform work associated with
////                 the connection in a separate thread.
//            }
////                manageMyConnectedSocket(socket)
//
////            val btserv=MyBluetoothService()
////            }
//
////            while(true)
////            {
////                Thread.sleep(1000)
////            }
//        }
//
//        // Closes the client socket and causes the thread to finish.
//        fun cancel() {
//            mmSocket?.close()
//        }
//    }


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

    @ExperimentalStdlibApi
    private fun bt_get_score()
    {
        //            clicks_amount+=1
        btsend("gets 0")
//            val bytes_req:Int = (clicks_amount+1)/10
        while(IS.available()<3)
            vScore.text="awaiting... ${IS.available().toString()}"
        var read_array:ByteArray = ByteArray(IS.available())
        IS.read(read_array)
//            vUpgrade_combo.text = read_array.size.toString()
//            sounds.text=read_array.contentToString()
        var bt_read_list_bytes = read_array.toMutableList()
        bt_read_list_bytes.removeLast()
        bt_read_list_bytes.removeLast()
        var bt_read_str:String=""
        //var bt_read_list:Array<Char> //make array instead of list
        for (e in bt_read_list_bytes) //convert every element to char, join them and convert to int (=score)
        {
            bt_read_str="$bt_read_str${e.toChar()}"
        }
        clicks_amount=bt_read_str.toInt()
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
    fun bt_send_color(buttonView: CompoundButton)
    {
        val col:Int
        when(buttonView)
        {
            white -> col=0xfff0e0
            orange -> col=0xFF3000
            purple -> col=0xe000ff
            green -> col=0x00ff00
            red -> col=0xff0000
            blue -> col=0x0000ff
            yellow -> col=0xFF5000
            pink -> col=0xFF5050
            else -> col=0
        }

//        sounds.text=col.toString()
        var col_red=Color.red(col).toString()
        var col_blue=Color.blue(col).toString()
        var col_green=Color.green(col).toString()

//        for (colval in arrayOf(col_red,col_green,col_blue)){
        //это короче добавление цифр до трехзначного числа
        if(col_red.length==1)
            col_red= "00$col_red"
        if(col_red.length==2)
            col_red= "0$col_red"
        if(col_blue.length==1)
            col_blue= "00$col_blue"
        if(col_blue.length==2)
            col_blue= "0$col_blue"
        if(col_green.length==1)
            col_green= "00$col_green"
        if(col_green.length==2)
            col_green= "0$col_green"
//        }

//        bright.text=col_red
//        skin.text=col_green
//        sound.text=col_blue
        btsend("scol $col_red$col_green$col_blue")
//        btsend("sred $col_red")
//        btsend("sblu $col_blue")
//        btsend("sgre $col_green")
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
    private fun btsend(command:String)
    {
//        bt_socket.connect()
//        OS = bt_socket.outputStream
        OS.write(command.toByteArray())
//        OS.flush()
//        OS.close()
    }

    @ExperimentalStdlibApi
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


//        val UUID = 00000000-0000-1000-8000-00805F9B34FB

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
                    bt_send_color(buttonView)
                }
            }
        }

        vUpgrade_perclick.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_perclick_cost && !upgrade_doubleclick) {
                    bt_get_score()
                    if(upgrade_price_payment) {
                        clicks_amount -= upgrade_perclick_cost
                        if(clicks_amount%2==1)
                            clicks_amount+=1 //при нечётном числе навсегда перестаёт работать комбо
                        btsend("sset $clicks_amount")
                    }
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))

                    upgrade_doubleclick=true

                    if (pizdec) {
                        btsend("\n")
                        for (i in 1..1000000)
                            vScore.text = (i * i).toString()
                    }
//                    Thread({
//                        Thread.sleep(10000)
//                        btsend("scpl 2")
//                    }).start()
                    score_count()
                    btsend("spcl 2") //временное исправление бага, при котором при апгрейде perclick
                    //btsend("cmul 4") //крит и комбо дают столько же, сколько обычный клик
//                    btsend("")
                }
            }
        }
        vUpgrade_combo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_combo_cost && !upgrade_combo) {
                    bt_get_score()
                    if(upgrade_price_payment) {
                        clicks_amount -= upgrade_combo_cost
                        btsend("sset $clicks_amount")
                    }
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    upgrade_combo=true
                    score_count()
                    btsend("comu 5")
                }
            }
        }
        vUpgrade_crit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if(clicks_amount>=upgrade_crit_cost && !upgrade_crit) {
                    bt_get_score()
                    if(upgrade_price_payment) {
                        clicks_amount -= upgrade_crit_cost
                        btsend("sset $clicks_amount")
                    }
                    buttonView.setBackgroundColor(getColor(R.color.colorClose))
                    upgrade_crit=true
                    score_count()
                    btsend("cmul 3")
                }
            }
        }
        vScore.setOnClickListener {
            bt_get_score()


//            clicks_amount=IS.read().toString()
            score_count()
        }

        var brightness_set:Float=0f //ползунок яркости
        seekBar_brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val sto:Float=100f
                brightness_set=i/100f

//                bright.text="sbrt $a"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                btsend("sbrt $brightness_set")
            }
        })

        var a:Int=0 //ползунок звука
        seekBar_sound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                a=i/3
//                sound.text="sbrt $a"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                btsend("svol $a")
            }
        })

//        lateinit var bt_device_connect_thread: Thread


        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        if (pairedDevices!=null&&pairedDevices.isNotEmpty())
            pairedDevices.forEach { device ->
                if(device.name.equals("HC-06")){
//                    bt_device_connect_thread = ConnectThread(device)

                    val bt_socket:BluetoothSocket =
                        device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(
                            "00001101-0000-1000-8000-00805F9B34FB"))

                    bt_socket.connect()

                    OS = bt_socket.outputStream
                    IS = bt_socket.inputStream

                    if(send_init_score)
                        btsend("sset $clicks_amount")
                }
            }

//        bt_device_connect_thread.start()

        soundbuttons[0].isChecked=true
        colorbuttons[0].isChecked=true
        score_count()

//                inputAsString=""
//                if(IS.available()>0) {
//                    inputAsString = IS.reader().readText()
//                    score.text=inputAsString
//                }
//                else
//                    score.text="no data"
//                sound.text=inputAsString
//                if(inputAsString.length>0){
//                    score.text=inputAsString
//                    skin.text="Skin"
//                }
//                else
//                    skin.text="no data"

//            }

    }
}

//class MyBluetoothService(
//    // handler that gets info from Bluetooth service
//    private val handler: Handler
//) {
//
//    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
//
//        private val mmInStream: InputStream = mmSocket.inputStream
//        private val mmOutStream: OutputStream = mmSocket.outputStream
//        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
//
//        override fun run() {
//            var numBytes: Int // bytes returned from read()
//
//            // Keep listening to the InputStream until an exception occurs.
//            while (true) {
//                // Read from the InputStream.
//                numBytes = mmInStream.read(mmBuffer)
//
//                // Send the obtained bytes to the UI activity.
////                val readMsg = handler.obtainMessage(
////                        MESSAGE_READ, numBytes, -1,
////                        mmBuffer)
////                readMsg.sendToTarget()
//            }
//        }
//
//        // Call this from the main activity to send data to the remote device.
//        fun write(command:String) {
//            mmOutStream.write(command.toByteArray())
//
//            // Share the sent message with the UI activity.
////            val writtenMsg = handler.obtainMessage(
////                    MESSAGE_WRITE, -1, -1, mmBuffer)
////            writtenMsg.sendToTarget()
//        }
//
//        // Call this method from the main activity to shut down the connection.
//        fun cancel() {
//            mmSocket.close()
//        }
//    }
//}