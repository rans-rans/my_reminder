class SelectedTime {
  final int hour;
  final int minute;
  SelectedTime({
    required this.hour,
    required this.minute,
  });

  SelectedTime copyWith({
    int? hour,
    int? minute,
  }) {
    return SelectedTime(
      hour: hour ?? this.hour,
      minute: minute ?? this.minute,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'hour': hour,
      'minute': minute,
    };
  }

  factory SelectedTime.fromMap(Map<String, dynamic> map) {
    return SelectedTime(
      hour: map['hour'],
      minute: map['minute'],
    );
  }

  @override
  String toString() => 'SelectedTime(hour: $hour, minute: $minute)';

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is SelectedTime && other.hour == hour && other.minute == minute;
  }

  @override
  int get hashCode => hour.hashCode ^ minute.hashCode;
}
