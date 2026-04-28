import { API_BASE_URL } from '@/services/config';
import type { Student } from '@/types/student';

export async function getStudents(signal?: AbortSignal): Promise<Student[]> {
  const response = await fetch(`${API_BASE_URL}/api/etudiants`, { signal });

  if (!response.ok) {
    throw new Error(`Unable to fetch students: HTTP ${response.status}`);
  }

  const data = (await response.json()) as Student[];

  if (!Array.isArray(data)) {
    throw new Error('Unexpected API response while loading students');
  }

  return data;
}
