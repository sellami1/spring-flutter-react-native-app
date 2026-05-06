import EtudiantCard from '../components/EtudiantCard';
import StudentForm from '../components/StudentForm';
import { getAllDepartements, getAllStudents } from '../lib/api';
import type { Departement, Student } from '../lib/types';

type EtudiantsPageProps = {
  searchParams?: Promise<{
    departementId?: string | string[];
  }>;
};

function resolveDepartementId(value?: string | string[]) {
  if (Array.isArray(value)) {
    return value[0];
  }

  return value;
}

export default async function EtudiantsPage({ searchParams }: EtudiantsPageProps) {
  const resolvedSearchParams = (await searchParams) ?? {};
  const selectedDepartementId = resolveDepartementId(resolvedSearchParams.departementId);

  const [students, departements] = await Promise.all([
    getAllStudents(selectedDepartementId),
    getAllDepartements(),
  ]);

  const selectedDepartement = departements.find(
    (departement) => String(departement.id) === selectedDepartementId,
  );

  return (
    <section className="grid gap-8 lg:grid-cols-[0.95fr_1.05fr]">
      <StudentForm mode="create" />
      <div className="space-y-5">
        <div className="rounded-3xl border border-white/60 bg-white/70 p-6 shadow-soft backdrop-blur">
          <h2 className="font-serif text-2xl">Students</h2>
          <p className="mt-2 text-sm text-midnight/70">
            Click a student to edit details and keep data aligned with the gateway.
          </p>
          <form className="mt-5 flex flex-wrap items-end gap-3" action="/etudiants" method="get">
            <label className="grid gap-2 text-sm font-semibold text-midnight">
              Filter by department
              <select
                name="departementId"
                defaultValue={selectedDepartementId ?? ''}
                className="min-w-[220px] rounded-xl border border-midnight/10 bg-white px-4 py-2"
              >
                <option value="">All departments</option>
                {departements.map((departement: Departement) => (
                  <option key={departement.id} value={departement.id}>
                    {departement.nom}
                  </option>
                ))}
              </select>
            </label>
            <button
              type="submit"
              className="rounded-full bg-sage px-5 py-2 text-sm font-semibold text-white"
            >
              Apply filter
            </button>
            {selectedDepartement ? (
              <span className="text-sm text-midnight/70">
                Showing students from {selectedDepartement.nom}
              </span>
            ) : null}
          </form>
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
