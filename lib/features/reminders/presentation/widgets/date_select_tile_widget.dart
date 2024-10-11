import 'package:flutter/material.dart';
import 'package:remind/utils/helpers/date_time_helpers.dart';

class DateSelectTileWidget extends StatelessWidget {
  final ValueNotifier<DateTime> selectedDate;
  final Function(DateTime) onDateChaged;
  const DateSelectTileWidget(
      {required this.selectedDate, required this.onDateChaged, super.key});

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder(
      valueListenable: selectedDate,
      builder: (context, value, child) {
        return ListTile(
          leading: const Icon(Icons.calendar_today),
          title: const Text("Date"),
          trailing: Text(
            DateTimeHelpers.getFormattedDate(value),
            style: const TextStyle(fontSize: 16),
          ),
          onTap: () async {
            final date = await showDatePicker(
              firstDate: DateTime.now(),
              lastDate: DateTime(2099),
              initialDate: DateTime.now(),
              context: context,
            );
            if (date == null) return;
            selectedDate.value = date;
            onDateChaged(date);
          },
        );
      },
    );
  }
}
