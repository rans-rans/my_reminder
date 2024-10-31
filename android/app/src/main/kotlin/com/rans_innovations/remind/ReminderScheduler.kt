package com.rans_innovations.remind

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class ReminderScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    fun scheduleReminder(reminder: Reminder) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            setAction("SET_ALARM_NOTIFICATION")
            putExtra("EXTRA_REMINDER", mapToJson(reminder.toMap()))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val initialDelay = findInitialDelay(reminder) + System.currentTimeMillis()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, initialDelay, pendingIntent
        )

    }


    fun cancelReminder(alarmId: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmId.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}