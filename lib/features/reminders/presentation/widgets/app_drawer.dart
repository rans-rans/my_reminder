import 'package:flutter/material.dart';

class AppDrawer extends StatelessWidget {
  final Function(int index) onSelected;
  const AppDrawer({
    super.key,
    required this.onSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            children: [
              ListTile(
                leading: const Icon(Icons.calendar_month),
                title: const Text("Calender"),
                onTap: () {
                  onSelected(0);
                },
              ),
              ListTile(
                leading: const Icon(Icons.task),
                title: const Text("Reminders"),
                onTap: () {
                  onSelected(1);
                },
              ),
              ListTile(
                leading: const Icon(Icons.timer),
                title: const Text("Clock"),
                onTap: () {
                  onSelected(2);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
