import { API_BASE_URL } from '@/services/config';
import type { Departement } from '@/types/departement';

export async function getDepartements(signal?: AbortSignal): Promise<Departement[]> {
  const response = await fetch(`${API_BASE_URL}/api/departements`, { signal });

  if (!response.ok) {
    throw new Error(`Unable to fetch departments: HTTP ${response.status}`);
  }

  const data = (await response.json()) as Departement[];

  if (!Array.isArray(data)) {
    throw new Error('Unexpected API response while loading departments');
  }

  return data;
}
