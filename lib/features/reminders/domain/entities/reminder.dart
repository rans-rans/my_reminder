import 'dart:convert' show json;

import 'package:flutter/foundation.dart' show listEquals;

import 'selected_time.dart';

class Reminder {
  final String description;
  final String id;
  final SelectedTime timeOfDay;
  final String title;
  final DateTime date;
  final List<String> selectedDays;
  Reminder({
    required this.description,
    required this.id,
    required this.timeOfDay,
    required this.title,
    required this.date,
    required this.selectedDays,
  });

  Reminder copyWith({
    String? description,
    String? id,
    SelectedTime? timeOfDay,
    String? title,
    DateTime? date,
    List<String>? selectedDays,
  }) {
    return Reminder(
      description: description ?? this.description,
      id: id ?? this.id,
      timeOfDay: timeOfDay ?? this.timeOfDay,
      title: title ?? this.title,
      date: date ?? this.date,
      selectedDays: selectedDays ?? this.selectedDays,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'date': date.toIso8601String(),
      'description': description,
      'id': id,
      'timeOfDay': json.encode(timeOfDay.toMap()),
      'title': title,
      'selectedDays': json.encode(selectedDays),
    };
  }

  factory Reminder.fromMap(Map<String, dynamic> map) {
    try {
      return Reminder(
        date: DateTime.parse(map['date']),
        description: map['description'],
        id: map['id'],
        timeOfDay: SelectedTime.fromMap(json.decode(map['timeOfDay'])),
        title: map['title'],
        selectedDays: (json.decode(map['selectedDays']) as List<dynamic>)
            .map((e) => e.toString())
            .toList(),
      );
    } catch (e) {
      rethrow;
    }
  }
  @override
  String toString() {
    return 'Reminder(description: $description, id: $id, timeOfDay: $timeOfDay, title: $title, date: $date, selectedDays: $selectedDays)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is Reminder &&
        other.description == description &&
        other.id == id &&
        other.timeOfDay == timeOfDay &&
        other.title == title &&
        other.date == date &&
        listEquals(other.selectedDays, selectedDays);
  }

  @override
  int get hashCode {
    return description.hashCode ^
        id.hashCode ^
        timeOfDay.hashCode ^
        title.hashCode ^
        date.hashCode ^
        selectedDays.hashCode;
  }
}
