package com.rans_innovations.remind

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


fun findInitialDelay(reminder: Reminder): Long {
    val timeData = parseJsonMap(reminder.timeOfDay)
    val targetTime = LocalTime.of(timeData["hour"] as Int, timeData["minute"] as Int)
    val targetDate = LocalDateTime.parse(reminder.date, DateTimeFormatter.ISO_DATE_TIME)!!

    val nowDateTime = LocalDateTime.now()
    val selectedDays = parseJsonList(reminder.selectedDays).map {
        mapToDay(it)
    }.sorted()

    val targetDay = if (selectedDays.isEmpty()) {
        targetDate.dayOfWeek!!
    } else {
        if ((nowDateTime.dayOfWeek == DayOfWeek.SUNDAY) && !selectedDays.contains(DayOfWeek.SUNDAY)) {
            DayOfWeek.MONDAY
        } else selectedDays.first { it.value >= nowDateTime.dayOfWeek.value }
    }
    println(targetDay)

    var daysUntilTarget = targetDay.value - nowDateTime.dayOfWeek.value

    if (daysUntilTarget < 0 ||
        (daysUntilTarget == 0 && nowDateTime.toLocalTime().isAfter(targetTime))
    ) daysUntilTarget += 7

    val targetDateTime =
        nowDateTime.toLocalDate().plusDays(daysUntilTarget.toLong()).atTime(targetTime)
    return ChronoUnit.MILLIS.between(nowDateTime, targetDateTime)

}

fun mapToDay(day: String): DayOfWeek {
    val correctedDay = day.substring(1, day.length - 1)
    return DayOfWeek.values().firstOrNull {
        it.name.lowercase().trim() == correctedDay
    } ?: DayOfWeek.SUNDAY
}

fun mapToJson(map: Map<*, *>): String {
    return map.entries.joinToString(
        prefix = "{",
        postfix = "}",
        separator = ","
    ) { (key, value) ->
        val formattedValue = when (value) {
            is String -> "\"$value\""
            else -> value.toString()
        }
        "\"$key\":$formattedValue"
    }
}

fun parseJsonList(jsonString: String): List<String> {
    val trimmed = jsonString.trim()
    if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
        throw IllegalArgumentException("Invalid JSON string")
    }

    val content = trimmed.substring(1, trimmed.length - 1)
    return content.split(",").map { it.trim() }.filter { it.isNotEmpty() }.map {
        if (it.startsWith("\\") && it.endsWith("\\")) {
            it.substring(1, it.length - 1)
        } else {
            it
        }
    }
}

fun parseJsonMap(jsonString: String): Map<String, Any> {
    val trimmed = jsonString.trim()
    if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
        throw IllegalArgumentException("Invalid JSON string")
    }

    val result = mutableMapOf<String, Any>()
    val content = trimmed.substring(1, trimmed.length - 1)
    var key = ""
    var value = ""
    var inQuotes = false
    var inKey = true

    for (char in content) {
        when {
            char == '"' -> inQuotes = !inQuotes
            char == ':' && !inQuotes -> {
                inKey = false
                continue
            }

            char == ',' && !inQuotes -> {
                result[key.trim().removeSurrounding("\"")] = parseValue(value.trim())
                key = ""
                value = ""
                inKey = true
                continue
            }

            else -> {
                if (inKey) key += char else value += char
            }
        }
    }

    if (key.isNotEmpty() && value.isNotEmpty()) {
        result[key.trim().removeSurrounding("\"")] = parseValue(value.trim())
    }

    return result
}

fun parseValue(value: String): Any {
    return when {
        value == "null" -> "null"
        value == "true" -> true
        value == "false" -> false
        value.toIntOrNull() != null -> value.toInt()
        value.toDoubleOrNull() != null -> value.toDouble()
        value.startsWith("\"") && value.endsWith("\"") -> value.removeSurrounding("\"")
        else -> value
    }
}
