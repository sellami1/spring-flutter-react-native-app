class Student {
  final int id;
  final String cin;
  final String nom;
  final String dateNaissance;

  const Student({
    required this.id,
    required this.cin,
    required this.nom,
    required this.dateNaissance,
  });

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      id: (json['id'] as num).toInt(),
      cin: json['cin'] as String,
      nom: json['nom'] as String,
      dateNaissance: json['dateNaissance'] as String,
    );
  }
}
