# Contact Unifier

A production-ready Android application for intelligently detecting and merging duplicate contacts from multiple sources (SIM, Google accounts, device storage).

**Persian Name:** یکپارچه‌ساز مخاطبین

## Overview

Contact Unifier scans all contacts on your device across all accounts, detects duplicates using smart matching algorithms, and allows you to merge them into a single contact stored in your chosen location. The app supports full Persian (Farsi) and English localization with RTL layout support.

### Key Features

- **Multi-source scanning**: Reads contacts from SIM cards, Google accounts, and device storage
- **Intelligent duplicate detection**: Uses phone number matching, email matching, and fuzzy name matching
- **Batch operations**: Select and merge multiple duplicate groups at once
- **Master location selection**: Choose where the merged contact is saved (SIM/Google/Device)
- **Safe deletion**: Confirmation dialogs before removing duplicate contacts
- **Undo support**: Revert merges during your session
- **Full localization**: Persian and English with RTL support when Persian is selected
- **Material 3 UI**: Modern, polished interface with smooth animations
- **Hybrid animations**: Jetpack Compose transitions + Lottie for success feedback
- **Offline-first**: All operations are local; no internet required

## Requirements

### System Requirements

- **Java JDK 17** or later (Temurin/Eclipse Adoptium recommended)
- **Android SDK**: API 26+ (min), API 35 (target)
- **Gradle 8.10+**
- **Android Build Tools 35.0.0**
- **Kotlin 1.9.22+**

### Device Requirements

- Android 8.0+ (API 26+)
- At least 50MB free storage
- Contacts permission granted

## Installation & Build

### 1. Environment Setup

#### Windows (PowerShell)

```powershell
# Set environment variables
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17"
$env:ANDROID_HOME = "C:\Android\sdk"
$env:PATH = "$env:JAVA_HOME\bin;$env:ANDROID_HOME\cmdline-tools\latest\bin;$env:PATH"

# Verify installations
java -version
gradle -v
sdkmanager --list
```

#### macOS/Linux

```bash
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=$HOME/Android/sdk
export PATH=$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH

# Verify installations
java -version
gradle -v
sdkmanager --list
```

### 2. Install Android SDK Components

```bash
# Platform tools and build tools
sdkmanager "platform-tools"
sdkmanager "build-tools;35.0.0"

# Android SDK platforms
sdkmanager "platforms;android-34"
sdkmanager "platforms;android-35"

# Emulator and system images (optional, for testing)
sdkmanager "emulator"
sdkmanager "system-images;android-35;default;arm64-v8a"
```

### 3. Build the App

```bash
# Navigate to project directory
cd ContactUnifier

# Clean and build
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build debug and run on emulator
./gradlew installDebugAndRunTests
```

### 4. Output

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK** (if built): `app/build/outputs/apk/release/app-release.apk`

## Testing

### On Android Emulator

```bash
# Create an Android Virtual Device (AVD) if not exists
# Use API 35, arm64-v8a architecture, ~4GB RAM

# Launch emulator
emulator -avd ContactUnifier_API35 &

# Wait for boot, then install and run
./gradlew installDebug
adb shell am start -n com.contactunifier/.MainActivity

# View logs
adb logcat -s "ContactUnifier"
```

### On Physical Device

```bash
# Enable developer mode on device
# Enable USB debugging

# Connect device and verify
adb devices

# Install and run
./gradlew installDebug
adb shell am start -n com.contactunifier/.MainActivity
```

### Manual Testing Steps

1. **Launch app** → Splash screen appears with animated logo
2. **Permissions** → Grant READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS
3. **Main Screen** → Displays list of duplicate contact groups (if any exist)
4. **Select duplicates** → Check groups to merge
5. **Merge** → Click merge button, select master location
6. **Confirm** → Verify deletion details in confirmation dialog
7. **Result** → Success animation + snackbar with undo option

### Testing with Contacts

Create test contacts with duplicates:

```bash
# Add test contacts via adb (or use Android Contacts app)
adb shell content insert --uri content://com.android.contacts/raw_contacts \
  --bind account_name:s:test@gmail.com \
  --bind account_type:s:com.google
```

## Project Structure

```
ContactUnifier/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/contactunifier/
│   │   │   │   ├── di/                    # Hilt DI modules
│   │   │   │   ├── presentation/          # UI Layer (Compose)
│   │   │   │   │   ├── main/             # Main screen & ViewModel
│   │   │   │   │   ├── detail/           # Detail/merge screen
│   │   │   │   │   ├── settings/         # Settings screen
│   │   │   │   │   ├── components/       # Reusable Compose components
│   │   │   │   │   ├── splash/           # Splash screen
│   │   │   │   │   ├── ui/               # Theme, colors, typography
│   │   │   │   │   └── navigation/       # NavGraph setup
│   │   │   │   ├── domain/                # Business logic
│   │   │   │   │   ├── usecase/          # Use cases (GetContacts, etc.)
│   │   │   │   │   └── repository/       # Repository interfaces
│   │   │   │   ├── data/                  # Data layer
│   │   │   │   │   ├── model/            # Contact, DuplicateGroup, etc.
│   │   │   │   │   └── repository/       # Repository implementations
│   │   │   │   ├── utils/                 # Utilities, extensions, helpers
│   │   │   │   ├── MainActivity.kt        # Entry point Activity
│   │   │   │   └── ContactUnifierApp.kt   # Application class (Hilt)
│   │   │   ├── res/
│   │   │   │   ├── values/strings.xml     # English strings
│   │   │   │   ├── values-fa/strings.xml  # Persian strings
│   │   │   │   ├── drawable/              # Icons and shapes
│   │   │   │   └── mipmap/                # App icons
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                          # Unit tests
│   │   └── androidTest/                   # Integration tests
│   ├── build.gradle.kts                   # App-level build config
│   └── proguard-rules.pro                 # ProGuard/R8 rules
│
├── build.gradle.kts                       # Root build config
├── settings.gradle.kts                    # Gradle settings
└── README.md                              # This file
```

## Architecture

### Clean Architecture Layers

- **Presentation**: Jetpack Compose UI, ViewModels, state management
- **Domain**: Business logic, use cases, repository interfaces
- **Data**: Contact access via ContactsContract, account management

### Key Technologies

- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Async**: Kotlin Coroutines + Flow
- **Database**: Android ContactsContract (system contacts)
- **Animations**: Compose + Lottie
- **Phone Matching**: libphonenumber library

## Localization

### Supported Languages

- **English** (en) - Default
- **Persian/Farsi** (fa) - Full RTL support

### Strings Location

- `res/values/strings.xml` - English strings
- `res/values-fa/strings.xml` - Persian strings

### RTL Support

Jetpack Compose automatically handles RTL layout when the system locale is Persian. No additional configuration needed.

### Language Switching

Users can change language in Settings. The app will recompose and apply the new language without restarting.

## Permissions

The app requires the following runtime permissions:

- `READ_CONTACTS` - Read all contacts from all accounts
- `WRITE_CONTACTS` - Merge and modify contact data
- `GET_ACCOUNTS` - List available accounts (Google, SIM, Device)

Users are prompted to grant these permissions on first launch with a clear rationale dialog.

## Known Limitations

- **Min SDK 26**: Some advanced Android 12+ features (like dynamic colors) degrade gracefully on older devices
- **SIM Card Handling**: SIM card detection varies by device and carrier (OEM-specific)
- **Contact Photos**: Large photos may impact performance; lazy-loading is implemented
- **Duplicate Detection**: O(n²) algorithm; very large contact lists (10,000+) may be slower
- **Offline Only**: No cloud sync or backup features

## Performance Considerations

- Contact scanning runs on background thread (IO dispatcher)
- Duplicate detection uses Coroutines for non-blocking operation
- Compose recomposition is optimized with proper state management
- Contact photos are lazy-loaded with Coil
- Large lists use LazyColumn for efficient rendering

## Privacy

**Important**: This app works entirely offline and locally on your device.

- No data is uploaded to any server
- No analytics or telemetry
- Contacts are not shared with third parties
- All operations are stored only in device ContactsProvider

Users are shown a privacy notice on first launch explaining these points.

## Development

### Code Style

- Follow Kotlin conventions
- Use meaningful variable/function names
- Add KDoc comments to public APIs
- Maximum line length: 120 characters

### Naming Conventions

- UI State: `<Feature>UiState` (e.g., `MainUiState`)
- ViewModel: `<Feature>ViewModel`
- Composables: PascalCase (e.g., `DuplicateGroupCard`)
- Classes: PascalCase
- Functions/Variables: camelCase

### Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests on emulator/device
./gradlew connectedAndroidTest

# Generate coverage report
./gradlew testDebugUnitTestCoverage
```

## Troubleshooting

### Build Issues

**Error**: `JAVA_HOME not set`
```bash
# Set JAVA_HOME to your JDK 17 installation
export JAVA_HOME=/path/to/jdk-17
```

**Error**: `ANDROID_HOME not set`
```bash
# Set ANDROID_HOME to your Android SDK installation
export ANDROID_HOME=$HOME/Android/sdk
```

**Error**: `Gradle sync fails`
```bash
# Clean gradle cache
./gradlew clean
./gradlew build --refresh-dependencies
```

### Runtime Issues

**Permission denied**: Ensure you grant permissions when prompted

**No contacts shown**: Check that you have contacts in your device's contact app

**Crashes on merge**: Ensure contacts are not being modified by other apps during merge

## Future Enhancements

- [ ] Backup/restore contacts
- [ ] Cloud sync integration (Google, Microsoft)
- [ ] Contact grouping and organization
- [ ] Custom merge rules
- [ ] Import/export VCF files
- [ ] Contact deduplication via photo recognition
- [ ] Scheduled automatic duplicate detection

## License

This project is provided as-is for personal and commercial use.

## Support

For issues or feature requests, please document:

1. Android version
2. Number of contacts
3. Account types (SIM/Google/Device)
4. Steps to reproduce
5. Error logs (via `adb logcat`)

---

**Version**: 1.0.0  
**Last Updated**: October 2026  
**Target SDK**: 35  
**Min SDK**: 26
