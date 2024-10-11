package com.rans_innovations.remind

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private lateinit var methodChannel: MethodChannel


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)


        val dbHelper = SQLiteHelper(applicationContext)
        methodChannel =
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "reminder-channel")
        val reminderScheduler = ReminderScheduler(applicationContext)

        PermissionHandler(this).requestPermissions()


        methodChannel.setMethodCallHandler { call, result ->

            when (call.method) {
                "insert-reminder" -> {
                    val data = call.arguments as Map<*, *>
                    val reminder = Reminder.fromMap(data)
                    //
                    val insertId = dbHelper.insertReminder(reminder)
                    reminderScheduler.scheduleReminder(reminder)
                    //
                    result.success(insertId)
                }

                "get-reminders" -> {
                    val reminders = dbHelper.getAllReminders()
                    result.success(reminders)
                }

                "delete-reminder" -> {
                    val id = call.arguments as String
                    val rowsDeleted = dbHelper.deleteReminder(id)
                    reminderScheduler.cancelReminder(id)
                    result.success(rowsDeleted)
                }

                "update-reminder" -> {
                    val data = call.arguments as Map<*, *>
                    val reminder = Reminder.fromMap(data)
                    //
                    val rowsUpdated = dbHelper.updateReminder(reminder)
                    reminderScheduler.cancelReminder(reminder.id)
                    reminderScheduler.scheduleReminder(reminder)
                    //
                    result.success(rowsUpdated)
                }
            }

        }
    }
}
