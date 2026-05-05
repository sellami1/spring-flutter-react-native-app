import 'package:flutter/material.dart';

import 'models/departement.dart';
import 'models/student.dart';
import 'services/departement_service.dart';
import 'services/student_service.dart';

void main() {
  runApp(const StudentsApp());
}

class StudentsApp extends StatelessWidget {
  const StudentsApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Students',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
      home: const StudentsPage(),
    );
  }
}

class StudentsPage extends StatefulWidget {
  const StudentsPage({super.key});

  @override
  State<StudentsPage> createState() => _StudentsPageState();
}

class _StudentsPageState extends State<StudentsPage> {
  final StudentService _studentService = StudentService();
  final DepartementService _departementService = DepartementService();

  late Future<List<Departement>> _departementsFuture;
  int? _selectedDepartementId;

  bool _loading = true;
  String? _error;
  List<Student> _students = const [];

  @override
  void initState() {
    super.initState();
    _departementsFuture = _departementService.fetchDepartements();
    _loadStudents();
  }

  Future<void> _loadStudents() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final students = await _studentService.fetchStudents(
        departementId: _selectedDepartementId,
      );
      setState(() {
        _students = students;
      });
    } catch (error) {
      setState(() {
        _error = 'Unable to load students: $error';
      });
    } finally {
      setState(() {
        _loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Students'),
      ),
      body: RefreshIndicator(
        onRefresh: _loadStudents,
        child: _buildBody(),
      ),
    );
  }

  Widget _buildBody() {
    return FutureBuilder<List<Departement>>(
      future: _departementsFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        }

        if (snapshot.hasError) {
          return ListView(
            children: [
              const SizedBox(height: 120),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Text(
                  'Unable to load departments: ${snapshot.error}',
                  textAlign: TextAlign.center,
                ),
              ),
              const SizedBox(height: 16),
              Center(
                child: FilledButton(
                  onPressed: () {
                    setState(() {
                      _departementsFuture = _departementService.fetchDepartements();
                    });
                  },
                  child: const Text('Retry'),
                ),
              ),
            ],
          );
        }

        final departements = snapshot.data ?? [];

        return ListView(
          padding: const EdgeInsets.all(12),
          children: [
            Text(
              'Filter by department',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 8),
            DropdownButtonFormField<int?> (
              value: _selectedDepartementId,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: 'Select a department',
              ),
              items: [
                const DropdownMenuItem<int?>(
                  value: null,
                  child: Text('All departments'),
                ),
                ...departements.map(
                  (departement) => DropdownMenuItem<int?>(
                    value: departement.id,
                    child: Text(departement.nom),
                  ),
                ),
              ],
              onChanged: (value) {
                setState(() {
                  _selectedDepartementId = value;
                });
                _loadStudents();
              },
            ),
            const SizedBox(height: 16),
            _buildStudentsSection(),
          ],
        );
      },
    );
  }

  Widget _buildStudentsSection() {
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_error != null) {
      return Column(
        children: [
          Text(
            _error!,
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 12),
          FilledButton(
            onPressed: _loadStudents,
            child: const Text('Retry'),
          ),
        ],
      );
    }

    if (_students.isEmpty) {
      return const Padding(
        padding: EdgeInsets.symmetric(vertical: 24),
        child: Center(child: Text('No students found.')),
      );
    }

    return ListView.separated(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemCount: _students.length,
      separatorBuilder: (context, index) => const SizedBox(height: 8),
      itemBuilder: (_, index) {
        final student = _students[index];

        return Card(
          child: ListTile(
            title: Text(student.nom),
            subtitle: Text('CIN: ${student.cin}\nBorn: ${student.dateNaissance}'),
            isThreeLine: true,
          ),
        );
      },
    );
  }
}
