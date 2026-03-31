import 'package:flutter_test/flutter_test.dart';
import 'package:mobile_flutter/main.dart';

void main() {
  testWidgets('students app builds', (tester) async {
    await tester.pumpWidget(const StudentsApp());

    expect(find.text('Students'), findsOneWidget);
  });
}
