import { ActivityIndicator, FlatList, Pressable, StyleSheet } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Colors } from '@/constants/theme';
import { useStudents } from '@/hooks/use-students';
import { useColorScheme } from '@/hooks/use-color-scheme';

export default function StudentsScreen() {
  const colorScheme = useColorScheme() ?? 'light';
  const palette = Colors[colorScheme];
  const { students, loading, error, refresh } = useStudents();

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
  buttonText: {
    color: '#FFFFFF',
    fontWeight: '700',
  },
});
