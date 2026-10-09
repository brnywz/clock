package com.brnywz.satissaati

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        try {
            context.startActivity(alarmIntent)
        } catch (_: Exception) {
            // If Android blocks a background activity launch, the next alarm is still scheduled.
        }
        Schedule.scheduleNext(context)
    }
}
