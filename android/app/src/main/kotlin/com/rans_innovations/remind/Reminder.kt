package com.rans_innovations.remind

import java.time.DayOfWeek

class Reminder(
    val id: String,
    val date: String,
    val title: String,
    val description: String,
    val timeOfDay: String,
    val selectedDays: List<DayOfWeek>
) {

    companion object {

        private fun mapToDay(day: String): DayOfWeek {
            val correctedDay = day.substring(1, day.length - 1)
            return DayOfWeek.values().firstOrNull {
                it.name.lowercase().trim() == correctedDay
            } ?: DayOfWeek.SUNDAY
        }

        fun fromMap(data: Map<*, *>): Reminder {
            val id = data["id"] as String
            val title = data["title"] as String
            val description = data["description"] as String
            val date = data["date"] as String
            val timeOfDay = data["timeOfDay"] as String
            val selectedDaysData = data["selectedDays"] as String

            val selectedDays = parseJsonList(selectedDaysData).map {
                mapToDay(it)
            }.sorted()

            return Reminder(
                id, date, title, description, timeOfDay, selectedDays
            )
        }
    }


    fun toMap(): Map<*, *> {
        return mapOf(
            "id" to id,
            "title" to title,
            "date" to date,
            "description" to description,
            "timeOfDay" to timeOfDay,
            "selectedDays" to mapSelectedDaysToString(selectedDays)
        )
    }
}
