import 'package:flutter/material.dart';

import 'models/student.dart';
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

  bool _loading = true;
  String? _error;
  List<Student> _students = const [];

  @override
  void initState() {
    super.initState();
    _loadStudents();
  }

  Future<void> _loadStudents() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final students = await _studentService.fetchStudents();
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
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_error != null) {
      return ListView(
        children: [
          const SizedBox(height: 120),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 24),
            child: Text(
              _error!,
              textAlign: TextAlign.center,
            ),
          ),
          const SizedBox(height: 16),
          Center(
            child: FilledButton(
              onPressed: _loadStudents,
              child: const Text('Retry'),
            ),
          ),
        ],
      );
    }

    if (_students.isEmpty) {
      return ListView(
        children: const [
          SizedBox(height: 120),
          Center(child: Text('No students found.')),
        ],
      );
    }

    return ListView.separated(
      padding: const EdgeInsets.all(12),
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
