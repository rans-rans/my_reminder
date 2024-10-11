import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:remind/features/reminders/data/repository/sql_lite_repo_impl.dart';
import 'package:remind/features/reminders/domain/entities/reminder.dart';
import 'package:remind/features/reminders/domain/repository/reminder_repo.dart';

class ReminderProvider extends AsyncNotifier<List<Reminder>> {
  final ReminderRepo _reminderRepo;

  ReminderProvider({required ReminderRepo reminderRepo}) : _reminderRepo = reminderRepo;

  @override
  FutureOr<List<Reminder>> build() async {
    final data = await _reminderRepo.getReminders();
    return data;
  }

  void addReminder(Reminder reminder) async {
    await _reminderRepo.addRemainder(reminder);
    state.value!.add(reminder);
    state = AsyncValue.data(state.value!);
  }

  void remmoveReminder(String id) async {
    await _reminderRepo.deleteRemainder(id);
    state.value?.removeWhere((r) => r.id == id);
    state = AsyncValue.data(state.value!);
  }

  void editReminder(Reminder reminder) async {
    await _reminderRepo.updateRemainder(reminder);
    final reminderIndex = state.value!.indexWhere((e) => e.id == reminder.id);
    state.value![reminderIndex] = reminder;
    state = AsyncValue.data(state.value!);
  }
}

final reminderProvider = AsyncNotifierProvider<ReminderProvider, List<Reminder>>(() {
  return ReminderProvider(
    reminderRepo: SqlLiteRepoImpl(),
  );
});
