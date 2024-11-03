package com.rans_innovations.remind

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.LocalTime

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
                val reminderData = parseJsonMap(intent.getStringExtra("EXTRA_REMINDER") ?: "")
                val reminder = Reminder.fromMap(reminderData)
                val days = parseJsonList(reminder.selectedDays).map {
                    mapToDay(it)
                }

                context?.also { ctx ->
                    if (days.isEmpty()) {
                        NotificationHelper(ctx).showNotification(
                            reminder.title,
                            reminder.description
                        )
                        //if it is a one time reminder, delete from the database after completing
                        SQLiteHelper(ctx).deleteReminder(reminder.id)
                        return
                    }
                    val today = LocalDateTime.now().dayOfWeek
                    val isContained = days.find { it.value == today.value }
                    if (isContained != null) {
                        NotificationHelper(ctx).showNotification(
                            reminder.title,
                            reminder.description
                        )
                    }
                    ReminderScheduler(ctx).scheduleReminder(reminder)
                }
            }


        }


    }
}