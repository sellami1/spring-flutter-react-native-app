import 'dart:convert';
import 'dart:io';

import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

import '../models/student.dart';

class StudentService {
  static const String _emulatorBaseUrl = 'http://10.30.141.1:8080';
  static const String _androidPhysicalBaseUrl = String.fromEnvironment(
    'API_BASE_URL_ANDROID_PHYSICAL',
    defaultValue: 'http://192.168.1.100:8080',
  );
  static const String _iosBaseUrl =
      String.fromEnvironment('API_BASE_URL_IOS', defaultValue: 'http://localhost:8080');
  static const String _webBaseUrl =
      String.fromEnvironment('API_BASE_URL_WEB', defaultValue: 'http://localhost:8080');

  Future<String> _resolveBaseUrl() async {
    if (kIsWeb) {
      return _webBaseUrl;
    }

    if (Platform.isAndroid) {
      final androidInfo = await DeviceInfoPlugin().androidInfo;
      return androidInfo.isPhysicalDevice ? _androidPhysicalBaseUrl : _emulatorBaseUrl;
    }

    if (Platform.isIOS) {
      return _iosBaseUrl;
    }

    return _webBaseUrl;
  }

  Future<List<Student>> fetchStudents() async {
    final baseUrl = await _resolveBaseUrl();
    final response = await http.get(Uri.parse('$baseUrl/api/etudiants'));

    if (response.statusCode != 200) {
      throw Exception('Failed to load students (HTTP ${response.statusCode})');
    }

    final data = jsonDecode(response.body);
    if (data is! List) {
      throw Exception('Unexpected response format');
    }

    return data
        .map((item) => Student.fromJson(item as Map<String, dynamic>))
        .toList();
  }
}
