package com.brnywz.satissaati

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object Schedule {
    private const val REQUEST_CODE = 450

    fun scheduleNext(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context, REQUEST_CODE, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val next = nextWeekdayAlarm(Calendar.getInstance())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()) {
            // MainActivity directs the user to grant exact-alarm access.
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.timeInMillis, pending)
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, next.timeInMillis, pending
            )
        }
    }

    fun cancel(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context, REQUEST_CODE, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending)
    }

    fun nextWeekdayAlarm(now: Calendar): Calendar {
        val c = (now.clone() as Calendar).apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // Alarm times: 10:45, 11:45 ... 17:45, Monday-Friday.
        for (dayOffset in 0..7) {
            val day = (now.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, dayOffset)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 45)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val dayOfWeek = day.get(Calendar.DAY_OF_WEEK)
            val weekday = dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY
            if (!weekday) continue

            for (hour in 10..17) {
                val candidate = (day.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                }
                if (candidate.after(now)) return candidate
            }
        }
        // Defensive fallback; the loop should always find a weekday occurrence.
        return c.apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 45)
        }
    }
}
