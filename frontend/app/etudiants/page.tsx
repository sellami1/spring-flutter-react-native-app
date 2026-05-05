import EtudiantCard from '../components/EtudiantCard';
import StudentForm from '../components/StudentForm';
import { fetchJson } from '../lib/api';
import type { Student } from '../lib/types';

export default async function EtudiantsPage() {
  const students = await fetchJson<Student[]>('/api/etudiants');

  return (
    <section className="grid gap-8 lg:grid-cols-[0.95fr_1.05fr]">
      <StudentForm mode="create" />
      <div className="space-y-5">
        <div className="rounded-3xl border border-white/60 bg-white/70 p-6 shadow-soft backdrop-blur">
          <h2 className="font-serif text-2xl">Students</h2>
          <p className="mt-2 text-sm text-midnight/70">
            Click a student to edit details and keep data aligned with the gateway.
          </p>
        </div>
        <div className="grid gap-4">
          {students.map((student) => (
            <EtudiantCard key={student.id} student={student} />
          ))}
          {students.length === 0 ? (
            <div className="rounded-3xl border border-white/60 bg-white/70 p-6 text-sm text-midnight/70">
              No students found. Add one using the form.
            </div>
          ) : null}
        </div>
      </div>
    </section>
  );
}
