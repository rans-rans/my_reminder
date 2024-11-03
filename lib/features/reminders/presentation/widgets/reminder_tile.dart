import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:remind/features/reminders/domain/entities/reminder.dart';
import 'package:remind/features/reminders/presentation/screens/new_reminder_screen.dart';
import 'package:remind/features/reminders/presentation/state/reminder_provider.dart';

class ReminderTile extends ConsumerStatefulWidget {
  final Reminder reminder;
  const ReminderTile({super.key, required this.reminder});

  @override
  ConsumerState<ReminderTile> createState() => _ReminderTileState();
}

class _ReminderTileState extends ConsumerState<ReminderTile> {
  late GlobalKey widgetKey;

  @override
  void initState() {
    super.initState();
    widgetKey = GlobalKey();
  }

  RelativeRect get _getWidgetPosition {
    try {
      final renderBox = widgetKey.currentContext?.findRenderObject() as RenderBox;
      final position = renderBox.localToGlobal(Offset.zero);
      final size = renderBox.size;
      return RelativeRect.fromLTRB(
        position.dx + (MediaQuery.sizeOf(context).width * 0.45),
        position.dy,
        (position.dx + size.width),
        position.dy + size.height,
      );
    } catch (e) {
      return RelativeRect.fromSize(Rect.zero, const Size(100, 100));
    }
  }

  void _showMenu() {
    showMenu(
      context: context,
      position: _getWidgetPosition,
      popUpAnimationStyle: AnimationStyle(
        curve: Curves.bounceInOut,
        reverseCurve: Curves.easeInOutQuad,
        duration: const Duration(milliseconds: 450),
      ),
      items: [
        PopupMenuItem(
          padding: const EdgeInsets.all(0),
          child: ListTile(
            title: const Text(
              "Delete Item",
              style: TextStyle(
                color: Colors.red,
              ),
            ),
            onTap: () {
              Navigator.pop(context);
              showModalBottomSheet(
                context: context,
                builder: (context) {
                  return SizedBox(
                    height: 180,
                    width: double.infinity,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const Text(
                          'Delete Item',
                          style: TextStyle(fontWeight: FontWeight.bold),
                        ),
                        const Spacer(),
                        InkWell(
                          onTap: () {
                            ref
                                .read(reminderProvider.notifier)
                                .remmoveReminder(widget.reminder.id);
                            Navigator.pop(context);
                          },
                          child: Container(
                            alignment: Alignment.center,
                            height: 80,
                            child: const Text("YES"),
                          ),
                        ),
                        InkWell(
                          onTap: () => Navigator.pop(context),
                          child: Container(
                            alignment: Alignment.center,
                            height: 80,
                            child: const Text("NO"),
                          ),
                        ),
                      ],
                    ),
                  );
                },
              );
            },
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      key: widgetKey,
      title: Text(widget.reminder.title),
      onLongPress: _showMenu,
      onTap: () {
        context.push(
          NewReminderScreen.path,
          extra: widget.reminder,
        );
      },
    );
  }
}
