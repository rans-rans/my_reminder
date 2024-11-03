import 'package:flutter/services.dart';
import 'package:remind/constants/object_constants.dart';

class SqlLiteConfig {
  // CRUD operations: Create, Read, Update, Delete
  Future<void> deleteReminder(String id) async {
    try {
      await methodChannel.invokeMethod('delete-reminder', id);
    } on PlatformException {
      rethrow;
    }
  }

  Future<int> insertReminder(Map<String, dynamic> reminderData) async {
    final value = await methodChannel.invokeMethod(
      'insert-reminder',
      reminderData,
    ) as int;
    return value;
  }

  Future<List<Object?>> getReminders() async {
    try {
      final List<Object?> result = await methodChannel.invokeMethod('get-reminders');
      return result;
    } catch (e) {
      rethrow;
    }
  }

  Future<void> updateReminder(Map<String, dynamic> reminderData) async {
    try {
      await methodChannel.invokeMethod('update-reminder', reminderData);
    } on PlatformException {
      rethrow;
    }
  }
}
