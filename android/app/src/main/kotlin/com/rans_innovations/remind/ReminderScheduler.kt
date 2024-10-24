package com.rans_innovations.remind

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import kotlin.math.abs

class ReminderScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    fun scheduleReminder(reminder: Reminder) {
        val timeMap = parseJsonMap(reminder.timeOfDay)
        val hour = timeMap["hour"] as Int
        val minute = timeMap["minute"] as Int

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            setAction("SET_ALARM_NOTIFICATION")
            putExtra("EXTRA_DESCRIPTION", reminder.description)
            putExtra("EXTRA_ID", reminder.id)
            putExtra("EXTRA_REMINDER_DAYS", reminder.selectedDays)
            putExtra("EXTRA_TITLE", reminder.title)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            abs(reminder.id.hashCode()),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val selectedDays = parseJsonList(reminder.selectedDays).map {
            mapToDay(it)
        }
        val setTime = if (selectedDays.isNotEmpty()) {
            findDifferenceBetweenNextOccurrence(selectedDays, hour, minute)
        } else {
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }.timeInMillis
        }

        if (selectedDays.isNotEmpty()) {
            //TODO there is still a one minute delay
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, setTime, AlarmManager.INTERVAL_DAY, pendingIntent
            )
            return
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, setTime, pendingIntent
        )

    }


    fun cancelReminder(alarmId: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                abs(alarmId.hashCode()),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}