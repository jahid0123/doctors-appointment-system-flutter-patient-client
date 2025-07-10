import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import '../models/Patient.dart';


class AuthService {
  final String baseUrl = "http://10.0.2.2:8081/api/auth";

  Future<bool> login(String email, String password) async {
    final response = await http.post(
      Uri.parse("$baseUrl/login"),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({"email": email, "password": password}),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setString("token", data["token"]);
      await prefs.setInt("id", data["id"]);
      return true;
    } else {
      return false;
    }
  }

  Future<void> logout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.clear();
  }

  Future<String?> getToken() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString("token");
  }

  Future<bool> register(String name, String email, String password) async {
    final response = await http.post(
      Uri.parse("$baseUrl/register"),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "name": name,
        "email": email,
        "password": password,
      }),
    );

    return response.statusCode == 200 || response.statusCode == 201;
  }

  Future<bool> isLoggedIn() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString("token");
    return token != null && token.isNotEmpty;
  }

  // Future<User?> fetchUserInfo() async {
  //   final prefs = await SharedPreferences.getInstance();
  //   final token = prefs.getString("token");
  //   final userId = prefs.getInt("id"); // Make sure to store userId after login
  //
  //   if (token == null || userId == null) return null;
  //
  //   final response = await http.get(
  //     Uri.parse("http://10.0.2.2:8080/api/user/info?userId=$userId"),
  //     headers: {
  //       "Authorization": "Bearer $token",
  //     },
  //   );
  //
  //   if (response.statusCode == 200) {
  //     final json = jsonDecode(response.body);
  //     return User.fromJson(json);
  //   } else {
  //     print("❌ Failed to load user info: ${response.body}");
  //     return null;
  //   }
  // }


  // Future<bool> changePassword(ChangePasswordDto dto) async {
  //   final prefs = await SharedPreferences.getInstance();
  //   final token = prefs.getString("token");
  //
  //   if (token == null) return false;
  //
  //   final response = await http.put(
  //     Uri.parse("http://10.0.2.2:8080/api/user/change-password"),
  //     headers: {
  //       "Content-Type": "application/json",
  //       "Authorization": "Bearer $token",
  //     },
  //     body: jsonEncode(dto.toJson()),
  //   );
  //
  //   return response.statusCode == 200;
  // }

  Future<bool> registerPatient({
    required String name,
    required String email,
    required String password,
    required String gender,
    required DateTime dob,
  }) async {
    final url = Uri.parse("$baseUrl/patient/register");

    final response = await http.post(
      url,
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({
        "name": name,
        "email": email,
        "password": password,
        "gender": gender,
        "dob": dob.toIso8601String().split('T')[0], // format as yyyy-MM-dd
      }),
    );

    if (response.statusCode == 201) {
      return true;
    } else {
      print("❌ Registration failed: ${response.body}");
      return false;
    }
  }

  Future<Patient> fetchProfile(int id) async {

    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString("token");

    if (token == null) {
      throw Exception("Authorization token not found");
    }

    final response = await http.get(Uri.parse("http://10.0.2.2:8081/api/patient/me?id=$id"),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },);
    if (response.statusCode == 200) {
      return Patient.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to load profile");
    }
  }

}
