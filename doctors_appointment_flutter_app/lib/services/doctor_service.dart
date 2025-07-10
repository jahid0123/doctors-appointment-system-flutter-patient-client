import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import '../models/DoctorResponseDto.dart';


class DoctorService {
  final String baseUrl = "http://10.0.2.2:8081/api/patient";

  Future<List<DoctorResponseDto>> fetchAllDoctors() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString("token");

    if (token == null) {
      throw Exception("Authorization token not found");
    }

    final url = Uri.parse("$baseUrl/get/doctors");

    final response = await http.get(
      url,
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    print("📡 Status Code: ${response.statusCode}");
    print("📥 Response Body: ${response.body}");

    if (response.statusCode == 200) {
      try {
        final List<dynamic> jsonList = jsonDecode(response.body);
        print("✅ JSON parsed. Doctors count: ${jsonList.length}");

        final doctors = jsonList.map((e) => DoctorResponseDto.fromJson(e)).toList();
        return doctors;
      } catch (e) {
        print("❌ JSON parsing error: $e");
        throw Exception("Failed to parse doctor list");
      }
    } else {
      print("❌ Error ${response.statusCode}: ${response.body}");
      throw Exception("Failed to load doctors");
    }
  }

  Future<bool> bookAppointment({
    required int doctorId,
    required DateTime date,
    required String time, // in "HH:mm" format
  }) async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString("token");
    final patientId = prefs.getInt("id");

    if (token == null || patientId == null) {
      throw Exception("Missing token or patient ID");
    }

    final url = Uri.parse("$baseUrl/book/appointment/by");
    final body = jsonEncode({
      "doctorId": doctorId,
      "patientId": patientId,
      "appointmentDate": date.toIso8601String().split("T")[0], // yyyy-MM-dd
      "appointmentTime": time, // should be like "14:00"
    });

    final response = await http.post(
      url,
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: body,
    );

    if (response.statusCode == 201) {
      return true;
    } else {
      print("❌ Error booking: ${response.statusCode} ${response.body}");
      return false;
    }
  }
}
