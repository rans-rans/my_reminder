import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:remind/features/reminders/domain/entities/reminder.dart';
import 'package:remind/features/reminders/domain/entities/selected_time.dart';
import 'package:remind/features/reminders/presentation/state/reminder_provider.dart';
import 'package:remind/features/reminders/presentation/widgets/date_select_tile_widget.dart';
import 'package:remind/features/reminders/presentation/widgets/selected_days_widget.dart';

class NewReminderScreen extends ConsumerStatefulWidget {
  static const path = "/new-reminder";
  final Reminder? oldReminder;
  const NewReminderScreen({this.oldReminder, super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => _NewTaskScreenState();
}

class _NewTaskScreenState extends ConsumerState<NewReminderScreen> {
  final titleController = TextEditingController();
  final descriptionController = TextEditingController();
  final descriptionFocus = FocusNode();

  var selectedDays = ValueNotifier(<String>[]);
  var selectedDate = ValueNotifier(DateTime.now());
  var selectedTime = ValueNotifier(TimeOfDay.now());

  void saveReminder() {
    final reminder = Reminder(
      date: selectedDate.value,
      description: descriptionController.text,
      id: widget.oldReminder?.id ?? DateTime.now().toString(),
      timeOfDay: SelectedTime(
        hour: selectedTime.value.hour,
        minute: selectedTime.value.minute,
      ),
      title: titleController.text,
      selectedDays: selectedDays.value,
    );
    if (widget.oldReminder == null) {
      ref.read(reminderProvider.notifier).addReminder(reminder);
    } else {
      ref.read(reminderProvider.notifier).editReminder(reminder);
    }
    context.pop();
  }

  @override
  void initState() {
    super.initState();
    if (widget.oldReminder != null) {
      final dummyData = widget.oldReminder!;
      titleController.text = dummyData.title;
      descriptionController.text = dummyData.description;
      selectedDate = ValueNotifier(dummyData.date);
      selectedDays = ValueNotifier(dummyData.selectedDays);
      selectedTime = ValueNotifier(
        TimeOfDay(
          hour: dummyData.timeOfDay.hour,
          minute: dummyData.timeOfDay.minute,
        ),
      );
    }
  }

  @override
  void dispose() {
    super.dispose();
    titleController.dispose();
    descriptionController.dispose();
    descriptionFocus.dispose();
  }

  @override
  Widget build(context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("New Reminder"),
      ),
      body: Stack(
        alignment: Alignment.bottomCenter,
        children: [
          SizedBox.expand(
            child: SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    TextField(
                      controller: titleController,
                      decoration: const InputDecoration(
                        labelText: "Title",
                        border: OutlineInputBorder(),
                      ),
                    ),
                    SelectedDaysWidget(
                      days: selectedDays.value,
                      onDateChanged: (days) {
                        selectedDays.value = days;
                      },
                    ),
                    ValueListenableBuilder(
                      valueListenable: selectedDays,
                      builder: (context, days, child) {
                        if (days.isNotEmpty) {
                          return SingleChildScrollView(
                            scrollDirection: Axis.horizontal,
                            child: Text(
                              "Repeat every ${days.map(
                                    (e) => e.substring(0, 3).toUpperCase(),
                                  ).toList().join(" ,")}",
                              // style: const TextStyle(fontSize: 13),
                            ),
                          );
                        }
                        return DateSelectTileWidget(
                          selectedDate: selectedDate,
                          onDateChaged: (date) {
                            selectedDate.value = date;
                          },
                        );
                      },
                    ),
                    ValueListenableBuilder(
                      valueListenable: selectedTime,
                      builder: (context, value, child) {
                        return ListTile(
                          leading: const Icon(Icons.timelapse),
                          title: const Text("Time"),
                          trailing: Text(value.format(context)),
                          onTap: () async {
                            final time = await showTimePicker(
                              context: context,
                              initialTime: TimeOfDay.now(),
                            );
                            if (time == null) return;
                            selectedTime.value = time;
                          },
                        );
                      },
                    ),
                    SizedBox(
                      height: MediaQuery.sizeOf(context).height * 0.3,
                      child: TextField(
                        controller: descriptionController,
                        decoration: const InputDecoration(
                          hintText: "Details",
                          border: OutlineInputBorder(),
                        ),
                        expands: true,
                        focusNode: descriptionFocus,
                        maxLines: null,
                        onTapOutside: (event) {
                          descriptionFocus.unfocus();
                        },
                        textAlignVertical: TextAlignVertical.top,
                      ),
                    )
                  ],
                ),
              ),
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              TextButton(
                onPressed: () {
                  context.pop();
                },
                child: const Text(
                  "Cancel",
                  style: TextStyle(color: Colors.red),
                ),
              ),
              TextButton(
                onPressed: saveReminder,
                child: const Text("Save"),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
