import { useCallback, useEffect, useState } from 'react';

import { getDepartements } from '@/services/departement-api-client';
import type { Departement } from '@/types/departement';

type UseDepartementsResult = {
  departements: Departement[];
  loading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
};

export function useDepartements(): UseDepartementsResult {
  const [departements, setDepartements] = useState<Departement[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const result = await getDepartements();
      setDepartements(result);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unable to load departments';
      setError(message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  return { departements, loading, error, refresh };
}
