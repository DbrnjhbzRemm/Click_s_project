package com.example.test

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_device_list.*
import java.util.*

@Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
class DeviceList : AppCompatActivity() {
    //widgets
    lateinit var vConnect: TextView
    lateinit var vListDevice: ListView

    //Bluetooth
    private var myBluetooth: BluetoothAdapter? = null
    private var pairedDevices: Set<BluetoothDevice>? = null
    private var EXTRA_ADDRESS = "device_address"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)

        //Calling widgets
        vConnect = button_connect
        vListDevice = listDevice

        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter()
        if (myBluetooth == null) {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(applicationContext, "Bluetooth Device Not Available", Toast.LENGTH_LONG)
                .show()
        } else if (!myBluetooth!!.isEnabled) {
            //Ask to the user turn the bluetooth on
            val turnBTon = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnBTon, 1)
        }

        vConnect.setOnClickListener { pairedDevicesList() }
    }
    private fun pairedDevicesList() {
        pairedDevices = myBluetooth!!.bondedDevices
        val list: ArrayList<*> = ArrayList<Any?>()
        if ((pairedDevices as MutableSet<BluetoothDevice>?)!!.size > 0) {
            for (bt in (pairedDevices as MutableSet<BluetoothDevice>?)!!) {
                list.add((bt.name + "\n" + bt.address) as Nothing)
                //Get the device's name and the address
            }
        } else {
            Toast.makeText(applicationContext, "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show()
        }
        val adapter: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        vListDevice.adapter = adapter
        vListDevice.onItemClickListener = myListClickListener
        //Method called when the device from the list is clicked
    }
    private val myListClickListener =
        OnItemClickListener { av, v, arg2, arg3 ->
            // Get the device MAC address, the last 17 chars in the View
            val info = (v as TextView).text.toString()
            val address = info.substring(info.length - 17)

            // Make an intent to start next activity.
            val i = Intent(this@DeviceList, MainActivity::class.java)

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address)
            //this will be received at ledControl (class) Activity
            startActivity(i)
        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_device_list, menu)
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
}