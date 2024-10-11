import 'package:remind/features/reminders/domain/entities/reminder.dart';

abstract class ReminderRepo {
  Future<void> addRemainder(Reminder reminder);
  Future<void> deleteRemainder(String id);
  Future<List<Reminder>> getReminders();
  Future<void> updateRemainder(Reminder reminder);
}
