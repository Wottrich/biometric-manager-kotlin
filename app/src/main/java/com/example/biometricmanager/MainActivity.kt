package com.example.biometricmanager

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.biometricmanager.dialog.BiometricDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val dialog = BiometricDialog(this, R.layout.activity_main)

            val btn = dialog.baseView?.findViewById<Button>(R.id.btnTest)
            btn?.setBackgroundColor(Color.RED)
            dialog.setCanceledButton(btn)


            dialog.setOnSuccess {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            dialog.show(this.supportFragmentManager)
        }
    }
}
