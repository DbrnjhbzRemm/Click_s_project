package com.example.test

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var vText: TextView
    lateinit var vConnect: TextView
    lateinit var vScore: TextView
    lateinit var vBrightness: SeekBar
    private val REQUEST_ENABLE_BT = 1

    var myBluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vText = storage
        vConnect = connect
        vScore = score

        vText.setOnClickListener {
            val i = Intent(this, SecondActivity::class.java)
            startActivityForResult(i, 0)
        }
        vConnect.setOnClickListener {
            val i = Intent(this, SecondActivity::class.java)
            startActivityForResult(i, 0)
        }
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this, "Bluetooth not support", Toast.LENGTH_LONG).show();
            return;
        }
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            return;
        }
//        val stInfo = bluetoothAdapter!!.name + " " + bluetoothAdapter!!.address
//        textInfo.setText(String.format("Это устройство: %s", stInfo))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        if (!myBluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
    }
}