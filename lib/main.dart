import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:remind/config/app_router.dart';

void main() async {
  runApp(const ProviderScope(child: MyApp()));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: "Remind",
      routerConfig: routeConfig,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.brown,
        ),
      ),
    );
  }
}
