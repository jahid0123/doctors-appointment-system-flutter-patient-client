import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import '../models/AppointmentResponseDto.dart';

class AppointmentService {
  final String baseUrl = "http://10.0.2.2:8081/api/patient";

  Future<List<AppointmentResponseDto>> getAppointmentsByPatient() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString("token");
    final patientId = prefs.getInt("id");

    if (token == null || patientId == null) {
      throw Exception("Token or Patient ID not found");
    }

    final url = Uri.parse("$baseUrl/all/appointmentlist?id=$patientId");

    final response = await http.get(
      url,
      headers: {"Authorization": "Bearer $token"},
    );

    print("========${response.body}");
    print(response.statusCode);
    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((e) => AppointmentResponseDto.fromJson(e)).toList();
    } else {
      throw Exception("Failed to fetch appointments");
    }
  }
}
