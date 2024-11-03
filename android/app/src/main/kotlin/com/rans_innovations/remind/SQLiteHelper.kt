package com.rans_innovations.remind

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reminders.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_REMINDERS = "reminders"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME_OF_DAY = "timeOfDay"
        private const val COLUMN_SELECTED_DAYS = "selectedDays"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_REMINDERS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME_OF_DAY TEXT,
                $COLUMN_SELECTED_DAYS TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDERS")
        onCreate(db)
    }

    // Insert a new reminder
    fun insertReminder(reminder: Reminder): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, reminder.id)
            put(COLUMN_TITLE, reminder.title)
            put(COLUMN_DESCRIPTION, reminder.description)
            put(COLUMN_DATE, reminder.date)
            put(COLUMN_TIME_OF_DAY, reminder.timeOfDay)
            put(COLUMN_SELECTED_DAYS, mapSelectedDaysToString(reminder.selectedDays))
        }
        return db.insert(TABLE_REMINDERS, null, values)
    }

    // Retrieve all reminders
    fun getAllReminders(): List<Map<String, Any?>> {
        val db = this.readableDatabase
        val cursor = db.query(
            true,
            TABLE_REMINDERS,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val reminders = mutableListOf<Map<String, Any?>>()

        while (cursor.moveToNext()) {
            val reminder = mapOf(
                "id" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                "title" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                "description" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                "date" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                "timeOfDay" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME_OF_DAY)),
                "selectedDays" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SELECTED_DAYS))
            )
            reminders.add(reminder)
        }
        cursor.close()
        return reminders
    }


    // Delete a reminder by ID
    fun deleteReminder(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_REMINDERS, "$COLUMN_ID = ?", arrayOf(id))
    }


    // Update an existing reminder
    fun updateReminder(reminder: Reminder): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, reminder.title)
            put(COLUMN_DESCRIPTION, reminder.description)
            put(COLUMN_DATE, reminder.date)
            put(COLUMN_TIME_OF_DAY, reminder.timeOfDay)
            put(COLUMN_SELECTED_DAYS, mapSelectedDaysToString(reminder.selectedDays))
        }
        return db.update(TABLE_REMINDERS, values, "$COLUMN_ID = ?", arrayOf(reminder.id))
    }
}
