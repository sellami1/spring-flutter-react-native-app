'use client';

import { useRouter } from 'next/navigation';
import { useState, useTransition, type FormEvent } from 'react';

import type { Student } from '../lib/types';

type StudentFormProps = {
  mode?: 'create' | 'edit';
  initialStudent?: Student;
};

const clientBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? 'http://localhost:8080';

export default function StudentForm({ mode = 'create', initialStudent }: StudentFormProps) {
  const router = useRouter();
  const [isPending, startTransition] = useTransition();

  const [cin, setCin] = useState(initialStudent?.cin ?? '');
  const [nom, setNom] = useState(initialStudent?.nom ?? '');
  const [dateNaissance, setDateNaissance] = useState(initialStudent?.dateNaissance ?? '');
  const [anneePremiereInscription, setAnneePremiereInscription] = useState(
    initialStudent?.anneePremiereInscription?.toString() ?? '',
  );
  const [status, setStatus] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setStatus(null);
    setError(null);

    if (mode === 'edit' && !initialStudent?.id) {
      setError('Missing student id for update.');
      return;
    }

    const payload = {
      cin,
      nom,
      dateNaissance,
      anneePremiereInscription: Number(anneePremiereInscription),
    };

    const url =
      mode === 'edit'
        ? `${clientBaseUrl}/api/etudiants/${initialStudent?.id}`
        : `${clientBaseUrl}/api/etudiants`;

    try {
      const response = await fetch(url, {
        method: mode === 'edit' ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`Request failed: ${response.status}`);
      }

      setStatus(mode === 'edit' ? 'Student updated.' : 'Student created.');

      if (mode === 'create') {
        setCin('');
        setNom('');
        setDateNaissance('');
        setAnneePremiereInscription('');
      }

      startTransition(() => {
        router.refresh();
      });
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Request failed';
      setError(message);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="rounded-3xl border border-white/60 bg-white/80 p-6 shadow-soft backdrop-blur"
    >
      <h3 className="font-serif text-2xl">
        {mode === 'edit' ? 'Edit student' : 'Add a new student'}
      </h3>
      <div className="mt-6 grid gap-4">
        <label className="grid gap-2 text-sm font-semibold">
          CIN
          <input
            className="rounded-xl border border-midnight/10 bg-white px-4 py-2"
            value={cin}
            onChange={(event) => setCin(event.target.value)}
            required
          />
        </label>
        <label className="grid gap-2 text-sm font-semibold">
          Name
          <input
            className="rounded-xl border border-midnight/10 bg-white px-4 py-2"
            value={nom}
            onChange={(event) => setNom(event.target.value)}
            required
          />
        </label>
        <label className="grid gap-2 text-sm font-semibold">
          Birth date
          <input
            className="rounded-xl border border-midnight/10 bg-white px-4 py-2"
            type="date"
            value={dateNaissance}
            onChange={(event) => setDateNaissance(event.target.value)}
            required
          />
        </label>
        <label className="grid gap-2 text-sm font-semibold">
          First inscription year
          <input
            className="rounded-xl border border-midnight/10 bg-white px-4 py-2"
            type="number"
            value={anneePremiereInscription}
            onChange={(event) => setAnneePremiereInscription(event.target.value)}
            required
          />
        </label>
      </div>
      <div className="mt-6 flex flex-wrap items-center gap-3">
        <button
          type="submit"
          disabled={isPending}
          className="rounded-full bg-sage px-6 py-2 text-sm font-semibold text-white disabled:opacity-60"
        >
          {isPending ? 'Saving...' : mode === 'edit' ? 'Save changes' : 'Create student'}
        </button>
        {status ? <span className="text-sm text-sage">{status}</span> : null}
        {error ? <span className="text-sm text-clay">{error}</span> : null}
      </div>
    </form>
  );
}
