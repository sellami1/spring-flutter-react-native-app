import Link from 'next/link';

import type { Student } from '../lib/types';

type EtudiantCardProps = {
  student: Student;
};

export default function EtudiantCard({ student }: EtudiantCardProps) {
  return (
    <article className="rounded-2xl border border-white/60 bg-white/70 p-5 shadow-soft backdrop-blur">
      <div className="flex items-start justify-between gap-4">
        <div>
          <h3 className="text-lg font-semibold">{student.nom}</h3>
          <p className="text-xs uppercase tracking-[0.2em] text-midnight/60">CIN {student.cin}</p>
        </div>
        <Link
          href={`/etudiants/${student.id}`}
          className="rounded-full border border-sage/50 px-4 py-1 text-xs font-semibold text-sage"
        >
          Edit
        </Link>
      </div>
      <div className="mt-4 text-sm text-midnight/70">
        <p>Birth date: {student.dateNaissance}</p>
        <p>First inscription: {student.anneePremiereInscription}</p>
        {student.age !== undefined ? <p>Age: {student.age}</p> : null}
      </div>
    </article>
  );
}
