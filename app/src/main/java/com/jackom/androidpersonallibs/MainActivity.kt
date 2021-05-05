package com.jackom.androidpersonallibs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.jackom.loglib.LogConfigAbstract
import com.jackom.loglib.LogFacade

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn -> {
                startActivity(Intent(this, LogDemoActivity::class.java))
            }
        }
    }


}