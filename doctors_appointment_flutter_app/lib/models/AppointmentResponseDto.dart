class AppointmentResponseDto {
  final int doctorId;
  final String doctorName;
  final String qualification;
  final int patientId;
  final String patientName;
  final DateTime patientDob;
  final int appointmentId;
  final DateTime appointmentDate;
  final String appointmentTime;
  final String appointmentStatus;

  AppointmentResponseDto({
    required this.doctorId,
    required this.doctorName,
    required this.qualification,
    required this.patientId,
    required this.patientName,
    required this.patientDob,
    required this.appointmentId,
    required this.appointmentDate,
    required this.appointmentTime,
    required this.appointmentStatus,
  });

  factory AppointmentResponseDto.fromJson(Map<String, dynamic> json) {
    return AppointmentResponseDto(
      doctorId: json['doctorId'],
      doctorName: json['doctorName'],
      qualification: json['qualification'],
      patientId: json['patientId'],
      patientName: json['patientName'],
      patientDob: DateTime.parse(json['patientDob']),
      appointmentId: json['appointmentId'],
      appointmentDate: DateTime.parse(json['appointmentDate']),
      appointmentTime: json['appointmentTime'],
      appointmentStatus: json['appointmentStatus'],
    );
  }
}
