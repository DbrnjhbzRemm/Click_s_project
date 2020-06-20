package com.example.test

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*
import java.security.AccessController.getContext as getContext


@Suppress("DEPRECATION")
open class MainActivity : AppCompatActivity() {
    private lateinit var vStorage: TextView
    private lateinit var vConnect: TextView
    private lateinit var vScore: TextView
    private lateinit var vBrightness: SeekBar
    private lateinit var vSounds: SeekBar

    protected var progress: ProgressDialog? = null
    protected var myBluetooth: BluetoothAdapter? = null
    private var address: String? = null
    private var isBtConnected = false
    protected var btSocket: BluetoothSocket? = null
    private var EXTRA_ADDRESS = "device_address"

    //SPP UUID. Look for it
    val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newint = intent
        address = newint.getStringExtra(EXTRA_ADDRESS)

        vStorage = storage
        vConnect = connect
        vScore = score
        vBrightness = seekBar_brightness
        vSounds = seekBar_sound

        ConnectBT().execute()

        vStorage.setOnClickListener {
            val i = Intent(this, SecondActivity::class.java)
            startActivityForResult(i, 0)
        }
        vConnect.setOnClickListener {
            val i = Intent(this, DeviceList::class.java)
            startActivityForResult(i, 0)
        }

        vSounds.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
//                    lumn.setText(progress.toString())
                    try {
                        btSocket!!.outputStream.write(progress.toString().toByteArray())
                    } catch (e: IOException) {
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

//        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
//            Toast.makeText(this, "Bluetooth not support", Toast.LENGTH_LONG).show();
//            return;
//        }
//        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (myBluetoothAdapter == null) {
//            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
//            return;
//        }
//        val stInfo = bluetoothAdapter!!.name + " " + bluetoothAdapter!!.address
//        textInfo.setText(String.format("Это устройство: %s", stInfo))
    }

    private fun Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket!!.close() //close connection
            } catch (e: IOException) {
                msg("Error")
            }
        }
        finish() //return to the first layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
//        if (!myBluetoothAdapter!!.isEnabled) {
//            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
//        }
    }

    private fun msg(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_led_control, menu)
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

    @SuppressLint("StaticFieldLeak")
    inner class ConnectBT : AsyncTask<Void, Void, Void>() // UI thread
    {
        private var ConnectSuccess = true

        //if it's here, it's almost connected
        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(this@MainActivity, "Connecting", Toast.LENGTH_LONG).show()
//            progress = ProgressDialog.show(
//                context,
//                "Connecting...",
//                "Please wait!!!"
//            ) //show a progress dialog
        }

        override fun doInBackground(vararg devices: Void?): Void?
        //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth =
                        BluetoothAdapter.getDefaultAdapter() //get the mobile bluetooth device
                    val dispositivo: BluetoothDevice =
                        myBluetooth!!.getRemoteDevice(address) //connects to the device's address and checks if it's available
                    btSocket =
                        dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID) //create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    btSocket!!.connect() //start connection
                }
            } catch (e: IOException) {
                ConnectSuccess = false //if the try failed, you can check the exception here
            }
            return null
        }

        override fun onPostExecute(result: Void?) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result)
            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.")
            } else {
                msg("Connected.")
                isBtConnected = true
            }
            progress!!.dismiss()
        }
    }
}