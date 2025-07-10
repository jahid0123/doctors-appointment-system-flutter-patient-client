class Patient {
  final int id;
  final String name;
  final String email;
  final String gender;
  final String? phone;
  final DateTime dob;
  final String? address;
  final String role;

  Patient({
    required this.id,
    required this.name,
    required this.email,
    required this.gender,
    this.phone,
    required this.dob,
    this.address,
    required this.role,
  });

  factory Patient.fromJson(Map<String, dynamic> json) {
    return Patient(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      gender: json['gender'],
      phone: json['phone'],
      dob: DateTime.parse(json['dob']),
      address: json['address'],
      role: json['role'],
    );
  }
}
