import 'package:doctors_appointment_flutter_app/services/doctor_service.dart';
import 'package:flutter/material.dart';

class BookAppointmentScreen extends StatefulWidget {
  final int doctorId;
  final String doctorName;

  const BookAppointmentScreen({
    super.key,
    required this.doctorId,
    required this.doctorName,
  });

  @override
  State<BookAppointmentScreen> createState() => _BookAppointmentScreenState();
}

class _BookAppointmentScreenState extends State<BookAppointmentScreen> {
  final DoctorService _appointmentService = DoctorService();
  DateTime? selectedDate;
  TimeOfDay? selectedTime;
  bool isLoading = false;

  Future<void> _selectDate() async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now().add(const Duration(days: 1)),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 30)),
    );
    if (picked != null) setState(() => selectedDate = picked);
  }

  Future<void> _selectTime() async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: const TimeOfDay(hour: 10, minute: 0),
    );
    if (picked != null) setState(() => selectedTime = picked);
  }

  Future<void> _submitAppointment() async {
    if (selectedDate == null || selectedTime == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Please select both date and time")),
      );
      return;
    }

    final String formattedTime = "${selectedTime!.hour.toString().padLeft(2, '0')}:${selectedTime!.minute.toString().padLeft(2, '0')}";

    setState(() => isLoading = true);
    final success = await _appointmentService.bookAppointment(
      doctorId: widget.doctorId,
      date: selectedDate!,
      time: formattedTime,
    );

    setState(() => isLoading = false);

    if (success) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("✅ Appointment booked successfully!")),
      );
      Navigator.pop(context);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("❌ Failed to book appointment")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Book with Dr. ${widget.doctorName}")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            ListTile(
              leading: const Icon(Icons.calendar_today),
              title: Text(selectedDate != null
                  ? "Date: ${selectedDate!.toLocal().toString().split(' ')[0]}"
                  : "Select Appointment Date"),
              onTap: _selectDate,
            ),
            ListTile(
              leading: const Icon(Icons.access_time),
              title: Text(selectedTime != null
                  ? "Time: ${selectedTime!.format(context)}"
                  : "Select Appointment Time"),
              onTap: _selectTime,
            ),
            const SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: isLoading ? null : _submitAppointment,
              icon: const Icon(Icons.check_circle),
              label: Text(isLoading ? "Booking..." : "Confirm Appointment"),
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
              ),
            )
          ],
        ),
      ),
    );
  }
}
