package com.rans_innovations.remind

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun findDifferenceBetweenNextOccurrence(
    allowedDays: List<DayOfWeek>,
    hour: Int,
    minute: Int
): Long {
    val targetTime = LocalTime.of(hour, minute)

    // Get current system time
    val currentDateTime = LocalDateTime.now()

    // Get current day and time
    val currentDay = currentDateTime.dayOfWeek
    val currentTime = currentDateTime.toLocalTime()

    // Check if current day is in allowed days
    if (currentDay in allowedDays) {
        // If current time is before target time on the same day
        if (currentTime.isBefore(targetTime)) {
            return ChronoUnit.MILLIS.between(
                currentDateTime,
                currentDateTime.with(targetTime)
            )
        }
    }

    // Find next allowed day
    var nextDay = allowedDays.find { it > currentDay }

    // If no next day found in current week, take first day from next week
    if (nextDay == null) {
        nextDay = allowedDays.first()
        // Calculate days until next occurrence
        val daysUntilNext = 7 - currentDay.value + nextDay.value

        val nextDateTime = currentDateTime
            .plusDays(daysUntilNext.toLong())
            .with(targetTime)

        return ChronoUnit.MILLIS.between(currentDateTime, nextDateTime)
    }

    // Calculate days until next allowed day in current week
    val daysUntilNext = nextDay.value - currentDay.value

    val nextDateTime = currentDateTime
        .plusDays(daysUntilNext.toLong())
        .with(targetTime)

    return ChronoUnit.MILLIS.between(currentDateTime, nextDateTime)
}

fun mapToDay(day: String): DayOfWeek {
    val correctedDay = day.substring(1, day.length - 1)
    return DayOfWeek.values().firstOrNull {
        it.name.lowercase().trim() == correctedDay
    } ?: DayOfWeek.SUNDAY
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
