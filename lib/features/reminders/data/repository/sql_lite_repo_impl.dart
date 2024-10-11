import 'package:remind/features/reminders/data/datasource/reminder_sql_lite_datasource.dart';
import 'package:remind/features/reminders/domain/entities/reminder.dart';
import 'package:remind/features/reminders/domain/repository/reminder_repo.dart';

class SqlLiteRepoImpl implements ReminderRepo {
  final datasource = ReminderSqlLiteDatasource();

  @override
  Future<void> addRemainder(reminder) async {
    try {
      await datasource.insertReminder(reminder);
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<void> deleteRemainder(id) async {
    try {
      await datasource.deleteReminder(id);
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<List<Reminder>> getReminders() async {
    try {
      final data = await datasource.getReminders();
      return data.map(Reminder.fromMap).toList();
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<void> updateRemainder(reminder) async {
    try {
      await datasource.updateReminder(reminder);
    } catch (e) {
      rethrow;
    }
  }
}
