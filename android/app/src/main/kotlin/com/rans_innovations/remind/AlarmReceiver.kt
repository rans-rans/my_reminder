package com.rans_innovations.remind

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {

            //because scheduled alarms doesn't survive device reboot, we reschedule again upon reboot
            Intent.ACTION_BOOT_COMPLETED -> {
                context?.also { ctx ->
                    val dbHelper = SQLiteHelper(ctx)
                    val remindersMapData = dbHelper.getAllReminders()
                    val reminders = remindersMapData.map { Reminder.fromMap(it) }
                    reminders.forEach { ReminderScheduler(ctx).scheduleReminder(it) }
                }
            }

            "SET_ALARM_NOTIFICATION" -> {
                val title = intent.getStringExtra("EXTRA_TITLE") ?: "Forgetting something?"
                val id = intent.getStringExtra("EXTRA_ID") ?: ""
                val content =
                    intent.getStringExtra("EXTRA_DESCRIPTION") ?: "Look sharp. You have a reminder"
                val days = parseJsonList(intent.getStringExtra("EXTRA_REMINDER_DAYS") ?: "[]")
                context?.also { ctx ->
                    if (days.isEmpty()) {
                        NotificationHelper(ctx).showNotification(title, content)
                        //if it is a one time reminder, delete from the database after completing
                        SQLiteHelper(ctx).deleteReminder(id)
                        return
                    }
                    val mappedDays = days.map { mapToDay(it) }
                    val today = LocalDateTime.now().dayOfWeek
                    val isContained = mappedDays.find { it.value == today.value }
                    if (isContained != null) {
                        NotificationHelper(ctx).showNotification(title, content)
                    }
                }
            }


        }


    }
}