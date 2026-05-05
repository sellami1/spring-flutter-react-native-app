import { useState } from 'react';
import { ActivityIndicator, FlatList, Pressable, StyleSheet, View } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Colors } from '@/constants/theme';
import { useDepartements } from '@/hooks/use-departements';
import { useStudents } from '@/hooks/use-students';
import { useColorScheme } from '@/hooks/use-color-scheme';
import type { Departement } from '@/types/departement';

export default function StudentsScreen() {
  const colorScheme = useColorScheme() ?? 'light';
  const palette = Colors[colorScheme];
  const { departements, loading: deptLoading, error: deptError, refresh: refreshDepts } =
    useDepartements();
  const [selectedDepartementId, setSelectedDepartementId] = useState<number | null>(null);
  const { students, loading, error, refresh } = useStudents(
    selectedDepartementId ?? undefined,
  );

  const departmentOptions: Array<Departement | { id: null; nom: string }> = [
    { id: null, nom: 'All departments' },
    ...departements,
  ];

  if (loading) {
    return (
      <ThemedView style={styles.centerStateContainer}>
        <ActivityIndicator size="large" color={palette.tint} />
        <ThemedText style={styles.stateText}>Loading students...</ThemedText>
      </ThemedView>
    );
  }

  if (error) {
    return (
      <ThemedView style={styles.centerStateContainer}>
        <ThemedText type="subtitle">Unable to load students</ThemedText>
        <ThemedText style={styles.stateText}>{error}</ThemedText>
        <Pressable style={[styles.button, { backgroundColor: palette.tint }]} onPress={refresh}>
          <ThemedText style={styles.buttonText}>Retry</ThemedText>
        </Pressable>
      </ThemedView>
    );
  }

  return (
    <ThemedView style={styles.screen}>
      <ThemedView style={styles.header}>
        <ThemedText type="title">Students</ThemedText>
        <ThemedText>Spring API endpoint: /api/etudiants</ThemedText>
      </ThemedView>

      <ThemedView style={styles.departementsSection}>
        <ThemedText type="subtitle">Departments</ThemedText>
        {deptLoading ? (
          <View style={styles.deptLoadingRow}>
            <ActivityIndicator size="small" color={palette.tint} />
            <ThemedText>Loading departments...</ThemedText>
          </View>
        ) : deptError ? (
          <View style={styles.deptErrorRow}>
            <ThemedText style={styles.stateText}>{deptError}</ThemedText>
            <Pressable
              style={[styles.buttonSmall, { backgroundColor: palette.tint }]}
              onPress={refreshDepts}
            >
              <ThemedText style={styles.buttonText}>Retry</ThemedText>
            </Pressable>
          </View>
        ) : (
          <FlatList
            data={departmentOptions}
            keyExtractor={(item) => String(item.id ?? 'all')}
            horizontal
            showsHorizontalScrollIndicator={false}
            contentContainerStyle={styles.departementsList}
            renderItem={({ item }) => {
              const isSelected = item.id === selectedDepartementId;
              return (
                <Pressable
                  onPress={() => setSelectedDepartementId(item.id)}
                  style={[
                    styles.departementChip,
                    {
                      backgroundColor: isSelected ? palette.tint : 'transparent',
                      borderColor: palette.tint,
                    },
                  ]}
                >
                  <ThemedText
                    style={[
                      styles.departementChipText,
                      { color: isSelected ? '#FFFFFF' : palette.tint },
                    ]}
                  >
                    {item.nom}
                  </ThemedText>
                </Pressable>
              );
            }}
          />
        )}
      </ThemedView>

      {students.length === 0 ? (
        <ThemedView style={styles.centerStateContainer}>
          <ThemedText>No students found.</ThemedText>
          <Pressable style={[styles.button, { backgroundColor: palette.tint }]} onPress={refresh}>
            <ThemedText style={styles.buttonText}>Refresh</ThemedText>
          </Pressable>
        </ThemedView>
      ) : (
        <FlatList
          data={students}
          keyExtractor={(item) => String(item.id)}
          contentContainerStyle={styles.listContent}
          renderItem={({ item }) => (
            <ThemedView style={styles.card}>
              <ThemedText type="subtitle">{item.nom}</ThemedText>
              <ThemedText>CIN: {item.cin}</ThemedText>
              <ThemedText>Born: {item.dateNaissance}</ThemedText>
            </ThemedView>
          )}
          onRefresh={refresh}
          refreshing={loading}
        />
      )}
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    paddingHorizontal: 16,
    paddingTop: 24,
  },
  header: {
    marginBottom: 16,
    gap: 6,
  },
  departementsSection: {
    gap: 10,
    marginBottom: 16,
  },
  departementsList: {
    gap: 10,
    paddingVertical: 4,
  },
  departementChip: {
    borderRadius: 999,
    borderWidth: 1,
    paddingHorizontal: 14,
    paddingVertical: 6,
  },
  departementChipText: {
    fontWeight: '600',
  },
  deptLoadingRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  deptErrorRow: {
    gap: 8,
  },
  listContent: {
    gap: 12,
    paddingBottom: 24,
  },
  card: {
    borderRadius: 12,
    padding: 14,
    borderWidth: 1,
    borderColor: '#D0D7DE',
    gap: 4,
  },
  centerStateContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 20,
    gap: 12,
  },
  stateText: {
    textAlign: 'center',
  },
  button: {
    borderRadius: 8,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  buttonSmall: {
    alignSelf: 'flex-start',
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 6,
  },
  buttonText: {
    color: '#FFFFFF',
    fontWeight: '700',
  },
});
