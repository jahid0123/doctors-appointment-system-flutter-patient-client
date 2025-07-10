class DoctorResponseDto {
  final int id;
  final String name;
  final String specialization;
  final String qualification;
  final int experience;
  final String hospitalName;
  final String phone;
  final String? image; // 👈 nullable

  DoctorResponseDto({
    required this.id,
    required this.name,
    required this.specialization,
    required this.qualification,
    required this.experience,
    required this.hospitalName,
    required this.phone,
    this.image,
  });

  factory DoctorResponseDto.fromJson(Map<String, dynamic> json) {
    return DoctorResponseDto(
      id: json['id'],
      name: json['name'],
      specialization: json['specialization'],
      qualification: json['qualification'],
      experience: json['experience'] ?? 0,
      hospitalName: json['hospitalName'],
      phone: json['phone'],
      image: json['image'], // allow null
    );
  }
}
