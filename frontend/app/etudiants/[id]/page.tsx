import Link from 'next/link';
import { notFound } from 'next/navigation';

import StudentForm from '../../components/StudentForm';
import { fetchJson } from '../../lib/api';
import type { Student } from '../../lib/types';

type PageProps = {
  params: { id: string };
};

export default async function EtudiantDetailPage({ params }: PageProps) {
  let student: Student;

  try {
    student = await fetchJson<Student>(`/api/etudiants/${params.id}`);
  } catch {
    notFound();
  }

  return (
    <section className="space-y-6">
      <Link
        href="/etudiants"
        className="inline-flex items-center gap-2 text-sm font-semibold text-sage"
      >
        ← Back to students
      </Link>
      <StudentForm mode="edit" initialStudent={student} />
    </section>
  );
}
