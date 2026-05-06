import type { Student } from './types';
import type { Departement } from './types';

const rawBaseUrl = process.env.API_BASE_URL ?? 'http://localhost:8080';
const apiBaseUrl = rawBaseUrl.endsWith('/') ? rawBaseUrl.slice(0, -1) : rawBaseUrl;

export async function fetchJson<T>(path: string, init: RequestInit = {}): Promise<T> {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  const response = await fetch(`${apiBaseUrl}${normalizedPath}`, {
    ...init,
    cache: 'no-store',
  });

  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }

  return (await response.json()) as T;
}

export function getAllStudents(departementId?: string): Promise<Student[]> {
  const query = departementId ? `?departementId=${encodeURIComponent(departementId)}` : '';
  return fetchJson<Student[]>(`/api/etudiants${query}`);
}

export function getStudentById(id: string): Promise<Student> {
  return fetchJson<Student>(`/api/etudiants/${id}`);
}

export function getAllDepartements(): Promise<Departement[]> {
  return fetchJson<Departement[]>('/api/departements');
}
