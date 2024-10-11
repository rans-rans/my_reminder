package com.rans_innovations.remind

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

fun findDifferenceBetweenNextOccurrence(days: List<DayOfWeek>, hour: Int, minute: Int): Long {
    val setTime = LocalTime.of(hour, minute)
    val currentDateTime = LocalDateTime.now()
    val currentTime = currentDateTime.toLocalTime()
    val currentDayOfWeek = LocalDateTime.now().dayOfWeek

    val todayIsInList = days.find {
        it.value == currentDayOfWeek.value
    } == null
    val timeHasNotPast = setTime.isAfter(currentTime)

    if (todayIsInList && timeHasNotPast) {
        return ChronoUnit.MILLIS.between(currentTime, setTime)
    }

    val nextDay = days.firstOrNull { it.value > currentDayOfWeek.value } ?: days.first()

    val daysUntilNext = if (nextDay.value > currentDayOfWeek.value) {
        nextDay.value - currentDayOfWeek.value
    } else {
        7 - currentDayOfWeek.value + nextDay.value
    }

    val targetDateTime = currentDateTime.plusDays(daysUntilNext.toLong()).with(setTime)
    val duration = ChronoUnit.MILLIS.between(currentDateTime, targetDateTime)

    return abs(duration)
}

fun mapToDay(day: String): DayOfWeek {
    val correctedDay=day.substring(1,day.length-1)
    return DayOfWeek.values().firstOrNull{
        it.name.lowercase().trim() ==correctedDay
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
