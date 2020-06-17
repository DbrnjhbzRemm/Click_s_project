package com.example.test

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var vText: TextView
    lateinit var vConnect: TextView
    lateinit var vScore: TextView
    private val REQUEST_ENABLE_BT = 1

    var bluetoothAdapter: BluetoothAdapter? = null


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
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
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
        if (!bluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
    }
}