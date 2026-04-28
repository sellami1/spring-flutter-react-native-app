import { useCallback, useEffect, useState } from 'react';

import { getStudents } from '@/services/student-api-client';
import type { Student } from '@/types/student';

type UseStudentsResult = {
  students: Student[];
  loading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
};

export function useStudents(): UseStudentsResult {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const result = await getStudents();
      setStudents(result);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unable to load students';
      setError(message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  return { students, loading, error, refresh };
}
