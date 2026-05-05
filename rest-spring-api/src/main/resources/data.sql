-- Initial data for departments table
INSERT INTO departements (nom) VALUES ('Informatique');
INSERT INTO departements (nom) VALUES ('Mathematiques');

-- Initial data for students table
INSERT INTO etudiants (cin, nom, date_naissance, annee_premiere_inscription, departement_id) VALUES
('07123456', 'Ahmed Safouri', '2005-03-15', 2023, 1);

INSERT INTO etudiants (cin, nom, date_naissance, annee_premiere_inscription, departement_id) VALUES
('08234567', 'Fatima Bensalem', '2004-07-22', 2022, 1);

INSERT INTO etudiants (cin, nom, date_naissance, annee_premiere_inscription, departement_id) VALUES
('09345678', 'Mohamed Karim', '2005-11-08', 2023, 2);

INSERT INTO etudiants (cin, nom, date_naissance, annee_premiere_inscription, departement_id) VALUES
('10456789', 'Leila Aissaoui', '2004-05-30', 2021, 2);

INSERT INTO etudiants (cin, nom, date_naissance, annee_premiere_inscription, departement_id) VALUES
('11567890', 'Salim Turki', '2005-09-12', 2024, 1);
