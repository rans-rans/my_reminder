import 'package:go_router/go_router.dart';
import 'package:remind/features/reminders/domain/entities/reminder.dart';
import 'package:remind/features/reminders/presentation/screens/home_screen.dart';
import 'package:remind/features/reminders/presentation/screens/new_reminder_screen.dart';

final routeConfig = GoRouter(
  initialLocation: "/",
  routes: [
    GoRoute(
      path: '/',
      builder: (_, __) => const HomeScreen(),
    ),
    GoRoute(
      path: NewReminderScreen.path,
      builder: (context, state) {
        final oldReminder = state.extra as Reminder?;
        return NewReminderScreen(oldReminder: oldReminder);
      },
    ),
  ],
);
