package com.brnywz.satissaati

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        showHome()
        Schedule.scheduleNext(this)
        requestExactAlarmPermissionIfNeeded()
    }

    private fun showHome() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
            setBackgroundColor(Color.rgb(20, 20, 20))
        }
        val title = TextView(this).apply {
            text = "brnywzsatissaati"
            textSize = 29f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        val details = TextView(this).apply {
            text = "Hafta içi her saat 10:45–17:45 arasında alarm.\n\nSesli uyarı + siren:\nSATIŞINI YAZ!!"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 28, 0, 28)
        }
        val test = Button(this).apply {
            text = "Alarmı test et"
            setOnClickListener { startActivity(Intent(this@MainActivity, AlarmActivity::class.java)) }
        }
        val enable = Button(this).apply {
            text = "Alarmı etkinleştir"
            setOnClickListener {
                Schedule.scheduleNext(this@MainActivity)
                requestExactAlarmPermissionIfNeeded()
            }
        }
        val disable = Button(this).apply {
            text = "Alarmı kapat"
            setOnClickListener { Schedule.cancel(this@MainActivity) }
        }
        listOf(title, details, test, enable, disable).forEach {
            layout.addView(it, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 10 })
        }
        setContentView(layout)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }

    private fun requestExactAlarmPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!manager.canScheduleExactAlarms()) {
                try {
                    startActivity(Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                        Uri.parse("package:$packageName")
                    ))
                } catch (_: Exception) { }
            }
        }
    }
}
