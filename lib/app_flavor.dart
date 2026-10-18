import 'package:flutter/services.dart' show appFlavor;

const _kAppFlavor = appFlavor ?? 'staging';

enum AppFlavor {
  staging._(),
  production._();

  const AppFlavor._();

  static AppFlavor get current => isProduction ? production : staging;

  static bool get isStaging => _kAppFlavor == 'staging';

  static bool get isProduction => _kAppFlavor == 'production';
}
