import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:open_file/open_file.dart';

class PrescriptionService {
  final String baseUrl = "http://10.0.2.2:8081/api/prescriptions"; // change as needed

  Future<void> downloadPrescriptionPdf(int prescriptionId) async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString("token");

    if (token == null) {
      throw Exception("Token not found");
    }

    final url = Uri.parse("$baseUrl/$prescriptionId/report");

    final response = await http.get(
      url,
      headers: {
        "Authorization": "Bearer $token",
        "Accept": "application/pdf",
      },
    );

    if (response.statusCode == 200) {
      final dir = await getApplicationDocumentsDirectory();
      final filePath = "${dir.path}/prescription_$prescriptionId.pdf";
      final file = File(filePath);
      await file.writeAsBytes(response.bodyBytes);

      await OpenFile.open(filePath);
    } else {
      throw Exception("Failed to download PDF: ${response.statusCode}");
    }
  }
}
