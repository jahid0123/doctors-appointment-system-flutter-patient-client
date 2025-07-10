import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/DoctorResponseDto.dart';
import '../services/doctor_service.dart';
import 'book_appointment_screen.dart';
import 'login_screen.dart';

class DoctorListScreen extends StatefulWidget {
  const DoctorListScreen({super.key});

  @override
  State<DoctorListScreen> createState() => _DoctorListScreenState();
}

class _DoctorListScreenState extends State<DoctorListScreen> {
  final DoctorService _doctorService = DoctorService();
  late Future<List<DoctorResponseDto>> _futureDoctors;

  List<DoctorResponseDto> _allDoctors = [];
  List<DoctorResponseDto> _filteredDoctors = [];
  String _searchText = "";
  String _selectedSpecialization = "All";

  List<String> _specializations = ["All"];

  final String baseImageUrl = "http://192.168.0.4:8080/images/"; // ✅ adjust as per backend

  @override
  void initState() {
    super.initState();
    _futureDoctors = _doctorService.fetchAllDoctors();
    _futureDoctors.then((doctors) {
      final uniqueSpecializations = doctors
          .map((d) => d.specialization)
          .toSet()
          .toList()
        ..sort();

      setState(() {
        _allDoctors = doctors;
        _filteredDoctors = doctors;
        _specializations.addAll(uniqueSpecializations);
      });
    });
  }

  void _filterDoctors() {
    setState(() {
      _filteredDoctors = _allDoctors.where((doctor) {
        final matchesSearch = doctor.name.toLowerCase().contains(_searchText) ||
            doctor.specialization.toLowerCase().contains(_searchText) ||
            doctor.hospitalName.toLowerCase().contains(_searchText);

        final matchesSpecialization = _selectedSpecialization == "All" ||
            doctor.specialization == _selectedSpecialization;

        return matchesSearch && matchesSpecialization;
      }).toList();
    });
  }

  void _logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
    if (!mounted) return;
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (_) => const LoginScreen()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            child: TextField(
              decoration: InputDecoration(
                hintText: "Search by name, specialization, or hospital",
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
              ),
              onChanged: (value) {
                _searchText = value.toLowerCase();
                _filterDoctors();
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: DropdownButtonFormField<String>(
              value: _selectedSpecialization,
              decoration: InputDecoration(
                labelText: "Filter by Specialization",
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
              ),
              items: _specializations
                  .map((spec) => DropdownMenuItem(
                value: spec,
                child: Text(spec),
              ))
                  .toList(),
              onChanged: (value) {
                _selectedSpecialization = value!;
                _filterDoctors();
              },
            ),
          ),
          const SizedBox(height: 10),
          Expanded(
            child: FutureBuilder<List<DoctorResponseDto>>(
              future: _futureDoctors,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting && _allDoctors.isEmpty) {
                  return const Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return const Center(child: Text("❌ Failed to load doctors"));
                } else if (_filteredDoctors.isEmpty) {
                  return const Center(child: Text("No matching doctors found"));
                }

                return ListView.builder(
                  itemCount: _filteredDoctors.length,
                  itemBuilder: (context, index) {
                    final doctor = _filteredDoctors[index];
                    final imageUrl = (doctor.image != null && doctor.image!.isNotEmpty)
                        ? "doctors image.jpg"
                        : "doctors image.jpg"; // ✅ placeholder

                    return Card(
                      elevation: 4,
                      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(12.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Center(
                              child: ClipRRect(
                                borderRadius: BorderRadius.circular(8),
                                child: Image.network(
                                  imageUrl,
                                  height: 140,
                                  width: double.infinity,
                                  fit: BoxFit.cover,
                                  errorBuilder: (context, error, stackTrace) {
                                    return const Icon(Icons.person, size: 80);
                                  },
                                ),
                              ),
                            ),
                            const SizedBox(height: 12),
                            Text(
                              doctor.name,
                              style: const TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text("Specialization: ${doctor.specialization}"),
                            Text("Qualification: ${doctor.qualification}"),
                            Text("Experience: ${doctor.experience} years"),
                            Text("Hospital: ${doctor.hospitalName}"),
                            Text("Phone: ${doctor.phone}"),
                            const SizedBox(height: 12),
                            Center(
                              child: ElevatedButton.icon(
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) => BookAppointmentScreen(
                                        doctorId: doctor.id,
                                        doctorName: doctor.name,
                                      ),
                                    ),
                                  );
                                },
                                icon: const Icon(Icons.calendar_month),
                                label: const Text("Book Appointment"),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
