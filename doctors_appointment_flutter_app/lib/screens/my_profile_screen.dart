import 'package:doctors_appointment_flutter_app/services/auth_service.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/Patient.dart';

class MyProfileScreen extends StatefulWidget {
  const MyProfileScreen({super.key});

  @override
  State<MyProfileScreen> createState() => _MyProfileScreenState();
}

class _MyProfileScreenState extends State<MyProfileScreen> {
  final AuthService _patientService = AuthService();
  Future<Patient>? _futurePatient;

  @override
  void initState() {
    super.initState();
    _loadPatient();
  }

  void _loadPatient() async {
    final prefs = await SharedPreferences.getInstance();
    final int? id = prefs.getInt("id");

    if (id != null) {
      setState(() {
        _futurePatient = _patientService.fetchProfile(id);
      });
    }
  }

  void _logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
    if (!mounted) return;
    Navigator.of(context).pushReplacementNamed("/login");
  }

  void _editProfile(Patient patient) {
    Navigator.of(context).pushNamed('/edit-profile', arguments: patient);
  }

  Widget _infoRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Row(
        children: [
          Icon(icon, color: Colors.blueGrey),
          const SizedBox(width: 12),
          Text(
            "$label:",
            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
          ),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              value,
              style: const TextStyle(fontSize: 16),
              overflow: TextOverflow.ellipsis,
            ),
          )
        ],
      ),
    );
  }

  String _getInitials(String name) {
    final parts = name.split(' ');
    String initials = '';
    for (var part in parts) {
      if (part.isNotEmpty) {
        initials += part[0].toUpperCase();
      }
    }
    return initials;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _futurePatient == null
          ? const Center(child: CircularProgressIndicator())
          : FutureBuilder<Patient>(
        future: _futurePatient,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return const Center(child: Text("❌ Failed to load profile"));
          } else if (snapshot.hasData) {
            final patient = snapshot.data!;
            final initials = _getInitials(patient.name);

            return SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Card(
                elevation: 8,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Padding(
                  padding: const EdgeInsets.symmetric(
                      vertical: 24, horizontal: 20),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      CircleAvatar(
                        radius: 48,
                        backgroundColor: Colors.blueAccent,
                        child: Text(
                          initials,
                          style: const TextStyle(
                              fontSize: 36,
                              fontWeight: FontWeight.bold,
                              color: Colors.white),
                        ),
                      ),
                      const SizedBox(height: 16),
                      Text(
                        patient.name,
                        style: const TextStyle(
                            fontSize: 24, fontWeight: FontWeight.bold),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 24),
                      _infoRow(Icons.email, "Email", patient.email),
                      _infoRow(Icons.phone, "Phone",
                          patient.phone ?? 'N/A'),
                      _infoRow(Icons.person, "Gender", patient.gender),
                      _infoRow(Icons.cake, "Date of Birth",
                          patient.dob.toString()), // Format if needed
                      _infoRow(Icons.home, "Address",
                          patient.address ?? 'N/A'),
                      const SizedBox(height: 30),
                      ElevatedButton.icon(
                        onPressed: () => _editProfile(patient),
                        icon: const Icon(Icons.edit),
                        label: const Text("Edit Profile"),
                        style: ElevatedButton.styleFrom(
                          minimumSize: const Size.fromHeight(48),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            );
          } else {
            return const Center(child: Text("No profile found"));
          }
        },
      ),
    );
  }
}
