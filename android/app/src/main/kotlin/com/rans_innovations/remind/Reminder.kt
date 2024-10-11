package com.rans_innovations.remind

class Reminder(
    val id: String,
    val date: String,
    val title: String,
    val description: String,
    val timeOfDay: String,
    val selectedDays: String
) {

    companion object {
        fun fromMap(data: Map<*, *>): Reminder {
            val id = data["id"] as String
            val title = data["title"] as String
            val description = data["description"] as String
            val date = data["date"] as String
            val timeOfDay = data["timeOfDay"] as String
            val selectedDays = data["selectedDays"] as String

            return Reminder(
                id, date, title, description, timeOfDay, selectedDays
            )
        }
    }
//    fun toMap(): Map<*, *> {
//        return mapOf(
//            "id" to id,
//            "title" to title,
//            "date" to date,
//            "description" to description,
//            "timeOfDay" to timeOfDay,
//            "selectedDays" to selectedDays
//        )
//    }
}
