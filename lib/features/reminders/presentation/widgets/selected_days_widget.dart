import 'package:flutter/material.dart';
import 'package:remind/utils/helpers/date_time_helpers.dart';

class SelectedDaysWidget extends StatelessWidget {
  final List<String> days;
  final Function(List<String>) onDateChanged;
  const SelectedDaysWidget({required this.days, required this.onDateChanged, super.key});

  @override
  Widget build(BuildContext context) {
    final selectedDays = ValueNotifier(days);
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: [
        ...daysOfWeek.map(
          (day) => InkWell(
            borderRadius: BorderRadius.circular(45),
            splashFactory: InkRipple.splashFactory,
            onTap: () {
              if (days.contains(day)) {
                days.remove(day);
              } else {
                days.add(day);
              }
              selectedDays.value = [...days];
              onDateChanged(selectedDays.value);
            },
            child: ValueListenableBuilder(
              valueListenable: selectedDays,
              builder: (context, value, child) {
                return Container(
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    border: switch (selectedDays.value.contains(day)) {
                      false => null,
                      true => Border.all(width: 2),
                    },
                  ),
                  child: Text(
                    day.substring(0, 3).toUpperCase(),
                    // style: const TextStyle(fontSize: 14),
                  ),
                );
              },
            ),
          ),
        )
      ],
    );
  }
}
