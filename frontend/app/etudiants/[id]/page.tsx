import Link from 'next/link';
import { notFound } from 'next/navigation';

import StudentForm from '../../components/StudentForm';
import { getStudentById } from '../../lib/api';
import type { Student } from '../../lib/types';

type PageProps = {
  params: Promise<{ id: string }>;
};

export default async function EtudiantDetailPage({ params }: PageProps) {
  let student: Student;
  const { id } = await params;

  try {
    student = await getStudentById(id);
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
