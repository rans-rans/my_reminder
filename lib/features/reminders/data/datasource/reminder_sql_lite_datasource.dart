import 'package:remind/config/sql_lite_config.dart';

import '../../domain/entities/reminder.dart';

class ReminderSqlLiteDatasource {
  final datasource = SqlLiteConfig();

  Future<void> deleteReminder(String id) async {
    try {
      await datasource.deleteReminder(id);
    } catch (e) {
      rethrow;
    }
  }

  Future<bool> insertReminder(Reminder reminder) async {
    final value = await datasource.insertReminder(reminder.toMap());
    return value == 1;
  }

  Future<List<Map<String, dynamic>>> getReminders() async {
    try {
      final result = await datasource.getReminders();

      final reminderData = result.map((r) {
        final temp = <String, dynamic>{};
        final data = r as Map<Object?, Object?>;
        data.forEach((key, value) {
          temp[key.toString()] = value as dynamic;
        });
        return temp;
      }).toList();

      return reminderData;
    } catch (e) {
      rethrow;
    }
  }

  Future<void> updateReminder(Reminder reminder) async {
    try {
      await datasource.updateReminder(reminder.toMap());
    } catch (e) {
      rethrow;
    }
  }
}
