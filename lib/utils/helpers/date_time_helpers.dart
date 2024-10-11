final months = [
  "january",
  "february",
  "march",
  "april",
  "may",
  "june",
  "july",
  "august",
  "september",
  "october",
  "november",
  "december",
];

final daysOfWeek = [
  "monday",
  "tuesday",
  "wednesday",
  "thursday",
  "friday",
  "saturday",
  "sunday",
];

class DateTimeHelpers {
  static String getDay(DateTime date) {
    final day = daysOfWeek[date.weekday % 7];
    return day;
  }

  static bool isTheSameDay(DateTime d1, DateTime d2) {
    final data = d1.year == d2.year && d1.month == d2.month && d1.day == d2.day;
    return data;
  }

  static String getFormattedDate(DateTime date) {
    return "${months[date.month - 1]} ${date.day}, ${date.year}";
  }
}
