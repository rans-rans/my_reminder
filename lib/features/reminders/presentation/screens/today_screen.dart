import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:remind/features/reminders/presentation/screens/new_reminder_screen.dart';
import 'package:remind/features/reminders/presentation/state/reminder_provider.dart';
import 'package:remind/utils/helpers/date_time_helpers.dart';
import 'package:remind/utils/helpers/reminder_helpers.dart';

class TodayScreen extends ConsumerWidget {
  const TodayScreen({super.key});

  @override
  Widget build(context, ref) {
    final selectedDate = ValueNotifier(DateTime.now());
    final reminderProv = ref.watch(reminderProvider);
    return reminderProv.when(
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (err, stack) => const Center(child: Text("Error occurred")),
      data: (reminders) => switch (MediaQuery.sizeOf(context).height <= 445) {
        true => CalendarDatePicker(
            initialDate: DateTime.now(),
            firstDate: DateTime.now(),
            currentDate: DateTime.now(),
            lastDate: DateTime(2099),
            onDateChanged: (date) {
              selectedDate.value = date;
            },
          ),
        false => Column(
            children: [
              SizedBox(
                height: MediaQuery.sizeOf(context).height * 0.3,
                child: CalendarDatePicker(
                  initialDate: DateTime.now(),
                  firstDate: DateTime.now(),
                  currentDate: DateTime.now(),
                  lastDate: DateTime(2099),
                  onDateChanged: (date) {
                    selectedDate.value = date;
                  },
                ),
              ),
              ValueListenableBuilder(
                valueListenable: selectedDate,
                builder: (context, thisDate, child) {
                  final selectedReminders = ReminderHelpers.getDateReminders(
                    selectedDate.value,
                    reminders,
                  );

                  return Expanded(
                    child: switch (selectedReminders.isEmpty) {
                      true => Center(
                          child: Text(
                            "No reminders for ${DateTimeHelpers.getFormattedDate(selectedDate.value)}",
                          ),
                        ),
                      false => ListView.builder(
                          itemCount: selectedReminders.length,
                          itemBuilder: (ctx, index) {
                            final reminder = selectedReminders[index];
                            return ListTile(
                              title: Text(reminder.title),
                              onTap: () {
                                context.push(
                                  NewReminderScreen.path,
                                  extra: reminder,
                                );
                              },
                            );
                          },
                        )
                    },
                  );
                },
              )
            ],
          ),
      },
    );
  }
}
