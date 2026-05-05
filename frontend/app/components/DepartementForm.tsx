'use client';

import { useEffect, useState, useTransition, type FormEvent } from 'react';
import { useRouter } from 'next/navigation';

import type { Departement } from '../lib/types';

type DepartementFormProps = {
  departements: Departement[];
};

const clientBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? 'http://localhost:8080';

export default function DepartementForm({ departements }: DepartementFormProps) {
  const router = useRouter();
  const [isPending, startTransition] = useTransition();
  const [selected, setSelected] = useState<Departement | null>(null);
  const [nom, setNom] = useState('');
  const [status, setStatus] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setNom(selected?.nom ?? '');
  }, [selected]);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setStatus(null);
    setError(null);

    const isEditing = Boolean(selected?.id);
    const url = isEditing
      ? `${clientBaseUrl}/api/departements/${selected?.id}`
      : `${clientBaseUrl}/api/departements`;

    try {
      const response = await fetch(url, {
        method: isEditing ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nom }),
      });

      if (!response.ok) {
        throw new Error(`Request failed: ${response.status}`);
      }

      setStatus(isEditing ? 'Department updated.' : 'Department created.');
      setSelected(null);
      setNom('');

      startTransition(() => {
        router.refresh();
      });
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Request failed';
      setError(message);
    }
  };

  return (
    <div className="grid gap-8 lg:grid-cols-[1.1fr_0.9fr]">
      <div className="rounded-3xl border border-white/60 bg-white/80 p-6 shadow-soft backdrop-blur">
        <h3 className="font-serif text-2xl">Departments</h3>
        <p className="mt-2 text-sm text-midnight/70">
          Select a department to edit or create a new one.
        </p>
        <div className="mt-6 space-y-3">
          {departements.map((departement) => (
            <button
              key={departement.id}
              type="button"
              onClick={() => setSelected(departement)}
              className={`flex w-full items-center justify-between rounded-2xl border px-4 py-3 text-left transition ${
                selected?.id === departement.id
                  ? 'border-sage bg-sage/10'
                  : 'border-midnight/10 hover:border-sage/50'
              }`}
            >
              <span className="text-sm font-semibold">{departement.nom}</span>
              <span className="text-xs uppercase tracking-[0.2em] text-midnight/50">Edit</span>
            </button>
          ))}
          {departements.length === 0 ? (
            <p className="text-sm text-midnight/60">No departments available yet.</p>
          ) : null}
        </div>
      </div>

      <form
        onSubmit={handleSubmit}
        className="rounded-3xl border border-white/60 bg-white/80 p-6 shadow-soft backdrop-blur"
      >
        <h3 className="font-serif text-2xl">
          {selected ? 'Edit department' : 'Add a new department'}
        </h3>
        <label className="mt-6 grid gap-2 text-sm font-semibold">
          Department name
          <input
            className="rounded-xl border border-midnight/10 bg-white px-4 py-2"
            value={nom}
            onChange={(event) => setNom(event.target.value)}
            required
          />
        </label>
        <div className="mt-6 flex flex-wrap items-center gap-3">
          <button
            type="submit"
            disabled={isPending}
            className="rounded-full bg-clay px-6 py-2 text-sm font-semibold text-white disabled:opacity-60"
          >
            {isPending ? 'Saving...' : selected ? 'Save changes' : 'Create department'}
          </button>
          {selected ? (
            <button
              type="button"
              onClick={() => setSelected(null)}
              className="rounded-full border border-midnight/20 px-5 py-2 text-sm font-semibold"
            >
              Clear
            </button>
          ) : null}
          {status ? <span className="text-sm text-sage">{status}</span> : null}
          {error ? <span className="text-sm text-clay">{error}</span> : null}
        </div>
      </form>
    </div>
  );
}
