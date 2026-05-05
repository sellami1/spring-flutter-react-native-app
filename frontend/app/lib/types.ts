export type Student = {
  id: number;
  cin: string;
  nom: string;
  dateNaissance: string;
  anneePremiereInscription: number;
  age?: number;
};

export type Departement = {
  id: number;
  nom: string;
};
