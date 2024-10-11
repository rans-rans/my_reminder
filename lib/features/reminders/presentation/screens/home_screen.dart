import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:remind/features/reminders/presentation/screens/new_reminder_screen.dart';
import 'package:remind/features/reminders/presentation/screens/reminders_screen.dart';
import 'package:remind/features/reminders/presentation/screens/timer_screen.dart';
import 'package:remind/features/reminders/presentation/screens/today_screen.dart';

class HomeScreen extends ConsumerStatefulWidget {
  const HomeScreen({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => _HomeScreenState();
}

class _HomeScreenState extends ConsumerState<HomeScreen> {
  int selectedIndex = 0;
  final navigationDestinations = [
    {
      "icon": const Icon(Icons.calendar_month),
      "label": "Calender",
    },
    {
      "icon": const Icon(Icons.task),
      "label": "Task",
    },
    {
      "icon": const Icon(Icons.timer),
      "label": "Clock",
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () {
          context.push(NewReminderScreen.path);
        },
      ),
      body: IndexedStack(
        index: selectedIndex,
        children: const [
          TodayScreen(),
          RemindersScreen(),
          TimerScreen(),
        ],
      ),
      bottomNavigationBar: NavigationBar(
        selectedIndex: selectedIndex,
        onDestinationSelected: (value) {
          setState(() => selectedIndex = value);
        },
        destinations: [
          ...navigationDestinations.map(
            (e) => NavigationDestination(
              icon: e["icon"] as Icon,
              label: e["label"] as String,
            ),
          ),
        ],
      ),
    );
  }
}
