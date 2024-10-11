package com.rans_innovations.remind

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            "SET_ALARM_NOTIFICATION" -> {
                val title = intent.getStringExtra("EXTRA_TITLE") ?: "Forgetting something"
                val content =
                    intent.getStringExtra("EXTRA_CONTENT") ?: "Look sharp. You have a reminder"
                val days = parseJsonList(intent.getStringExtra("EXTRA_REMINDER_DAYS") ?: "[]")
                context?.also { ctx ->
                    if (days.isEmpty()) {
                        NotificationHelper(ctx).showNotification(title, content)
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