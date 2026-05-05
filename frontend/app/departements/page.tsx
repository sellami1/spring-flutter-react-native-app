import DepartementForm from '../components/DepartementForm';
import { fetchJson } from '../lib/api';
import type { Departement } from '../lib/types';

export default async function DepartementsPage() {
  const departements = await fetchJson<Departement[]>('/api/departements');

  return (
    <section className="space-y-6">
      <div className="rounded-3xl border border-white/60 bg-white/70 p-6 shadow-soft backdrop-blur">
        <h2 className="font-serif text-2xl">Departments</h2>
        <p className="mt-2 text-sm text-midnight/70">
          Maintain the department catalog used by the mobile and grading services.
        </p>
      </div>
      <DepartementForm departements={departements} />
    </section>
  );
}
