import 'dart:io';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:path_provider/path_provider.dart';
import 'package:http/http.dart' as http;

import '../models/AppointmentResponseDto.dart';
import '../services/appointment_service.dart';

class AppointmentHistoryScreen extends StatefulWidget {
  const AppointmentHistoryScreen({super.key});

  @override
  State<AppointmentHistoryScreen> createState() => _AppointmentHistoryScreenState();
}

class _AppointmentHistoryScreenState extends State<AppointmentHistoryScreen> {
  final AppointmentService _appointmentService = AppointmentService();
  late Future<List<AppointmentResponseDto>> _futureAppointments;

  List<AppointmentResponseDto> _allAppointments = [];
  List<AppointmentResponseDto> _filteredAppointments = [];
  String _selectedStatus = "ALL";

  final List<String> _statuses = ["ALL", "PENDING", "APPROVED", "REJECTED", "CONFIRMED"];

  @override
  void initState() {
    super.initState();
    _futureAppointments = _appointmentService.getAppointmentsByPatient();
    _futureAppointments.then((appointments) {
      setState(() {
        _allAppointments = appointments;
        _filteredAppointments = appointments;
      });
    });
  }

  void _filterByStatus(String status) {
    setState(() {
      _selectedStatus = status;
      if (status == "ALL") {
        _filteredAppointments = _allAppointments;
      } else {
        _filteredAppointments = _allAppointments
            .where((a) => a.appointmentStatus.toUpperCase() == status)
            .toList();
      }
    });
  }

  Color _getStatusColor(String status) {
    switch (status.toUpperCase()) {
      case 'PENDING':
        return Colors.orange;
      case 'APPROVED':
        return Colors.green;
      case 'REJECTED':
        return Colors.red;
      case 'CONFIRMED':
        return Colors.blue;
      default:
        return Colors.grey;
    }
  }

  Future<void> _downloadPrescription(int appointmentId) async {
    try {
      final url = Uri.parse("http://10.0.2.2:8081/api/prescriptions/$appointmentId/report");
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final Uint8List bytes = response.bodyBytes;
        final directory = await getExternalStorageDirectory(); // internal app folder

        final filePath = '${directory!.path}/prescription_$appointmentId.pdf';
        final file = File(filePath);
        await file.writeAsBytes(bytes);

        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("✅ Prescription saved at:\n$filePath")),
        );
      } else {
        throw Exception("Failed to download PDF");
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("❌ Download failed: $e")),
      );
    }
  }

  void _showAppointmentDetails(AppointmentResponseDto appt) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          title: const Text("Appointment Details"),
          content: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text("🩺 Doctor: ${appt.doctorName}"),
                Text("🎓 Qualification: ${appt.qualification}"),
                Text("📆 Date: ${DateFormat('dd/MM/yyyy').format(appt.appointmentDate)}"),
                Text("⏰ Time: ${appt.appointmentTime}"),
                Text("🧑 Patient: ${appt.patientName}"),
                const SizedBox(height: 12),
                Row(
                  children: [
                    const Text("Status: "),
                    Chip(
                      label: Text(appt.appointmentStatus),
                      backgroundColor: _getStatusColor(appt.appointmentStatus),
                      labelStyle: const TextStyle(color: Colors.white),
                    ),
                  ],
                ),
                const SizedBox(height: 20),
                if (appt.appointmentStatus == "APPROVED")
                  ElevatedButton.icon(
                    icon: const Icon(Icons.download),
                    label: const Text("Download Prescription"),
                    onPressed: () async {
                      Navigator.pop(context);
                      await _downloadPrescription(appt.appointmentId);
                    },
                  ),
                if (appt.appointmentStatus == "PENDING" ||
                    appt.appointmentStatus == "CONFIRMED")
                  TextButton.icon(
                    icon: const Icon(Icons.cancel, color: Colors.red),
                    label: const Text("Cancel Appointment",
                        style: TextStyle(color: Colors.red)),
                    onPressed: () {
                      Navigator.pop(context);
                      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                        content: Text("❌ Appointment cancelled (mock)."),
                      ));
                    },
                  ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text("Close"),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(

      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12.0),
            child: DropdownButtonFormField<String>(
              value: _selectedStatus,
              decoration: InputDecoration(
                labelText: "Filter by Status",
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
              ),
              items: _statuses
                  .map((status) => DropdownMenuItem(
                value: status,
                child: Text(status),
              ))
                  .toList(),
              onChanged: (value) => _filterByStatus(value!),
            ),
          ),
          Expanded(
            child: FutureBuilder<List<AppointmentResponseDto>>(
              future: _futureAppointments,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting &&
                    _allAppointments.isEmpty) {
                  return const Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return const Center(child: Text("❌ Failed to load appointments"));
                } else if (_filteredAppointments.isEmpty) {
                  return const Center(child: Text("No appointments found for this status"));
                }

                return ListView.builder(
                  itemCount: _filteredAppointments.length,
                  itemBuilder: (context, index) {
                    final appt = _filteredAppointments[index];
                    return Card(
                      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                      elevation: 4,
                      child: ListTile(
                        title: Text("Dr. ${appt.doctorName}"),
                        subtitle: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text("Date: ${DateFormat('dd/MM/yyyy').format(appt.appointmentDate)}"),
                            Text("Time: ${appt.appointmentTime}"),
                            Text("Patient: ${appt.patientName}"),
                            const SizedBox(height: 4),
                            Row(
                              children: [
                                const Text("Status: "),
                                Chip(
                                  label: Text(appt.appointmentStatus),
                                  backgroundColor: _getStatusColor(appt.appointmentStatus),
                                  labelStyle: const TextStyle(color: Colors.white),
                                ),
                              ],
                            ),
                          ],
                        ),
                        onTap: () => _showAppointmentDetails(appt),
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
