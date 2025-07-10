import 'package:doctors_appointment_flutter_app/screens/doctor_list_screen.dart';
import 'package:doctors_appointment_flutter_app/screens/login_screen.dart';
import 'package:doctors_appointment_flutter_app/screens/main_bottom_nav_screen.dart';
import 'package:doctors_appointment_flutter_app/screens/patient_register_screen.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Doctors Appointment System',
      debugShowCheckedModeBanner: false,
      initialRoute: '/login',
      routes: {
        '/login': (context) => const LoginScreen(),
        '/register': (context) => const PatientRegisterScreen(),
        // Updated here: user home route now points to MainBottomNavScreen
        '/user_home': (context) => const MainBottomNavScreen(),
      },
    );
  }
}

