import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:remind/features/reminders/presentation/state/reminder_provider.dart';
import 'package:remind/features/reminders/presentation/widgets/reminder_tile.dart';

class RemindersScreen extends ConsumerWidget {
  const RemindersScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final reminders = ref.watch(reminderProvider);
    if ((reminders.value ?? []).isEmpty) {
      return const Center(
        child: Text(
          "No reminders available",
        ),
      );
    }
    return ListView.builder(
      itemCount: reminders.value?.length ?? 0,
      itemBuilder: (ctx, index) {
        final reminder = reminders.value![index];
        return ReminderTile(reminder: reminder);
      },
    );
  }
}
