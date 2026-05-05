class Departement {
  final int id;
  final String nom;

  const Departement({
    required this.id,
    required this.nom,
  });

  factory Departement.fromJson(Map<String, dynamic> json) {
    return Departement(
      id: (json['id'] as num).toInt(),
      nom: json['nom'] as String,
    );
  }
}
