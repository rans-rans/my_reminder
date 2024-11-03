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

    val targetDay = if (reminder.selectedDays.isEmpty()) {
        targetDate.dayOfWeek!!
    } else {
        if ((nowDateTime.dayOfWeek == DayOfWeek.SUNDAY) && !reminder.selectedDays.contains(DayOfWeek.SUNDAY)) {
            DayOfWeek.MONDAY
        } else reminder.selectedDays.first { it.value >= nowDateTime.dayOfWeek.value }
    }

    var daysUntilTarget = targetDay.value - nowDateTime.dayOfWeek.value

    if (daysUntilTarget < 0 ||
        (daysUntilTarget == 0 && nowDateTime.toLocalTime().isAfter(targetTime))
    ) daysUntilTarget += 7

    val targetDateTime =
        nowDateTime.toLocalDate().plusDays(daysUntilTarget.toLong()).atTime(targetTime)
    return ChronoUnit.MILLIS.between(nowDateTime, targetDateTime)
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

fun mapSelectedDaysToString(selectedDays: List<DayOfWeek>): String {
    val mappedDays = selectedDays.map {
        when (it) {
            DayOfWeek.MONDAY -> "monday"
            DayOfWeek.TUESDAY -> "tuesday"
            DayOfWeek.WEDNESDAY -> "wednesday"
            DayOfWeek.THURSDAY -> "thursday"
            DayOfWeek.FRIDAY -> "friday"
            DayOfWeek.SATURDAY -> "saturday"
            DayOfWeek.SUNDAY -> "sunday"

        }
    }
    return mappedDays
        .joinToString(
            prefix = "[",
            postfix = "]",
            separator = ","
        ) { "\"$it\"" }
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
