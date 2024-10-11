import 'package:remind/features/reminders/domain/entities/reminder.dart';
import 'package:remind/utils/helpers/date_time_helpers.dart';

typedef Dth = DateTimeHelpers;
typedef RList = List<Reminder>;

class ReminderHelpers {
  static RList getDateReminders(DateTime date, RList reminders) {
    return reminders.where((reminder) {
      return Dth.isTheSameDay(date, reminder.date) ||
          reminder.selectedDays.contains(Dth.getDay(date));
    }).toList();
  }
}
