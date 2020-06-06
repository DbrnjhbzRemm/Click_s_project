package com.example.test

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    lateinit var vText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vText = findViewById<Button>(R.id.storage)
        vText.setTextColor(0xFFFFFFFFF.toInt())
        vText.setOnClickListener {
            Log.e("tag", "НАЖАТА")
            val i = Intent(this,SecondActivity::class.java)
            i.putExtra("tag1", vText.text)
            startActivityForResult(i, 0)
        }
        Log.v("tag", "Был запущен onCreate")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            var str = data.getStringExtra("tag2")

            vText.text = str
        }
    }

    override fun onStart(){
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}