# Contact Unifier - Build & Testing Guide

## 🚀 Quick Start (5 minutes)

```bash
# Navigate to project
cd D:\project\cc_project\contactMerge\ContactUnifier

# Verify environment
echo %JAVA_HOME%
echo %ANDROID_HOME%
java -version
gradle -v

# Build
./gradlew clean build
```

## 📋 Pre-Build Checklist

Before building, verify:

- [ ] JAVA_HOME is set to Java JDK 17
- [ ] ANDROID_HOME is set to your Android SDK location
- [ ] Android SDK has platforms API 34 and 35 installed
- [ ] Gradle 8.10+ is installed
- [ ] You have ~2GB free disk space for build artifacts

## 🔨 Build Commands

### Full Build (Recommended First)
```bash
./gradlew clean build
```
**Expected**: Downloads dependencies, compiles all code, runs tests
**Output**: Gradle build completes successfully
**Time**: 3-5 minutes (first run) or 30 seconds (subsequent)

### Build Debug APK Only
```bash
./gradlew assembleDebug
```
**Expected**: Creates debug APK
**Output**: `app/build/outputs/apk/debug/app-debug.apk` (~5-10MB)
**Time**: 1-2 minutes

### Build with Logs
```bash
./gradlew build -i
```
**Expected**: Verbose output showing each compilation step
**Use when**: Debugging build errors

### Build without Kotlin Lint
```bash
./gradlew build -x lint
```
**Expected**: Skips static analysis (faster for quick testing)
**Use when**: You want to test quickly

## 🎮 Running on Android Emulator

### Prerequisites
- Android SDK with system images installed
- ~4GB RAM recommended for emulator
- KVM enabled (on Linux) or Hyper-V (on Windows)

### Create Emulator (One-time Setup)

**Via Android Studio GUI** (easiest):
1. Open Android Studio
2. Tools → Device Manager
3. Create Virtual Device
4. Select "Pixel 6" or similar
5. Choose Android 15 (API 35)
6. Allocate 4GB RAM, 4GB storage

**Via Command Line**:
```bash
# List available system images
sdkmanager --list | grep "system-images"

# Install system image if needed
sdkmanager "system-images;android-35;default;arm64-v8a"

# Create AVD
avdmanager create avd -n ContactUnifier_API35 -k "system-images;android-35;default;arm64-v8a" -d "Pixel 6"
```

### Launch Emulator

```bash
# List available emulators
emulator -list-avds

# Launch emulator
emulator -avd ContactUnifier_API35 -writable-system &

# Wait 2-3 minutes for full boot
# You'll see Android home screen when ready
```

**Or via Android Studio**: Device Manager → Select device → Play button

### Install & Run App

```bash
# Verify emulator is running
adb devices
# Output should show: emulator-5554   device

# Install debug APK
./gradlew installDebug

# OR manually:
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.contactunifier/.MainActivity

# View logs
adb logcat | grep -i contactunifier
```

## 📱 Running on Physical Device

### Prerequisites
- Android phone with USB cable
- USB debugging enabled (Developer Settings)
- Device unlocked

### Setup

1. **Connect device**
   ```bash
   adb devices
   ```
   Output should show your device ID

2. **Trust computer** (on device)
   - If prompted, tap "Allow" to allow USB debugging

### Install & Run

```bash
# Install app
./gradlew installDebug

# Launch app
adb shell am start -n com.contactunifier/.MainActivity

# View logs
adb logcat | grep -i contactunifier
```

## 🧪 Manual Testing Steps

### Test 1: App Launches (1 minute)
1. Install app as above
2. Tap app icon or use `adb shell am start...`
3. **Expected**: Splash screen appears with animated logo for 2-3 seconds
4. **Then**: Main screen appears

### Test 2: Permissions Screen (2 minutes)
1. App should show permission rationale screen
2. Tap "Grant Permissions"
3. **Expected**: System permission dialog appears
4. Grant all three permissions (READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS)
5. **Then**: Main screen should display

### Test 3: No Duplicates Found (1 minute)
1. Fresh emulator or device with few contacts
2. App scans and displays: "No duplicates found"
3. **Expected**: Empty state message with helpful text in both English and Persian

### Test 4: Create Test Duplicates (5 minutes)
1. Use Android Contacts app to manually create duplicate contacts:
   - "John Smith" + "555-1234"
   - "John Smith" + "555-1234" (duplicate)
   - "J. Smith" + "555-1234" (fuzzy match)

2. Or use adb to insert test contacts:
   ```bash
   # Contact 1
   adb shell content insert --uri content://com.android.contacts/raw_contacts \
     --bind account_name:s:testaccount@gmail.com \
     --bind account_type:s:com.google
   
   # (Requires knowledge of RawContacts structure - easier to use Contacts app)
   ```

3. Return to Contact Unifier app
4. **Expected**: Tap refresh (pull-to-refresh), app detects duplicate groups

### Test 5: View Duplicate Group (3 minutes)
1. From main screen, tap duplicate group card
2. Navigate to detail screen
3. **Expected**: Shows all contacts in group with comparison
4. Tap on one contact to select as master
5. Select master location (Google/Device/SIM)
6. Tap "Merge & Keep in [Location]"

### Test 6: Merge Confirmation (2 minutes)
1. Confirmation dialog appears
2. Shows which contacts will be deleted
3. Shows master contact and storage location
4. Tap "Yes, Merge"
5. **Expected**: Loading indicator, then success message

### Test 7: Language Switching (2 minutes)
1. Navigate to Settings screen
2. Select "Persian (فارسی)"
3. **Expected**: App UI changes to Persian text
4. RTL layout should automatically flip (text right-aligned)
5. Select "English" to switch back
6. **Expected**: Back to English, LTR layout

### Test 8: Theme Switching (1 minute)
1. In Settings, select "Dark" theme
2. **Expected**: UI colors invert to dark scheme
3. Select "Light" theme
4. **Expected**: Back to light scheme
5. Select "System Default"
6. **Expected**: Follows device theme setting

## 📊 Expected Test Results

| Test | Result | Notes |
|------|--------|-------|
| Splash screen | 2-3 sec animation | Must transition smoothly |
| Permissions | All granted | App won't work without them |
| No duplicates | Empty state shown | Should show helpful message |
| Duplicates found | List displayed | Cards should be clickable |
| Merge flow | Confirmation shown | Should disable button until selection |
| Merge success | Success message | Contacts should merge |
| Language | Persian text visible | RTL layout should flip |
| Theme | Dark/Light colors | Should match Material 3 |

## 🐛 Debugging

### App Crashes
```bash
# See crash logs
adb logcat | grep "AndroidRuntime"
```

### Permission Errors
```bash
# Check granted permissions
adb shell pm list permissions -d
adb shell pm grant com.contactunifier android.permission.READ_CONTACTS
```

### Slow Contact Loading
- Reduce contacts count for testing
- Profile with Android Profiler in Android Studio

### No Duplicates Detected
- Ensure test contacts match the algorithm
- Check logcat for debug messages

## 📈 Performance Targets

- **App launch**: < 2 seconds
- **Contact scan**: < 5 seconds for 100 contacts
- **Duplicate detection**: < 1 second for 100 contacts
- **Merge operation**: < 2 seconds
- **UI responsiveness**: No jank or stuttering

## 🎯 Success Criteria

✅ All tests pass
✅ App builds without errors
✅ No crashes on any screen
✅ Permissions handled gracefully
✅ Merge/delete operations work
✅ Language switching works
✅ Theme switching works
✅ Duplicate detection finds matches
✅ UI is smooth and responsive

## 🚨 Known Issues to Watch For

1. **First build is slow** - Gradle downloads dependencies (~200MB)
2. **Emulator slow on first launch** - Wait 5+ minutes for full boot
3. **No contacts on fresh emulator** - Need to create test contacts first
4. **Permission denials** - App shows main screen but can't load contacts
5. **SIM card contacts** - May not appear on emulator (device-dependent)

## 📞 Troubleshooting

### "JAVA_HOME not set"
```bash
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17
```

### "ANDROID_HOME not set"
```bash
set ANDROID_HOME=C:\Android\sdk
```

### Gradle download times out
```bash
./gradlew build --refresh-dependencies
```

### Emulator won't launch
- Check virtualization is enabled in BIOS
- Try: `emulator -avd ContactUnifier_API35 -no-snapshot-load`

### App won't install
```bash
adb uninstall com.contactunifier
./gradlew installDebug
```

### Contacts not showing
- Create test contacts in Contacts app first
- Grant READ_CONTACTS permission
- Kill and relaunch app

## ✅ Next Steps

1. Run `./gradlew clean build` - Verify build succeeds
2. Create emulator with API 35
3. Run `./gradlew installDebug`
4. Manually test flows above
5. Create bug report if anything breaks
6. Report success metrics

---

**Estimated Total Time**: 30-45 minutes (first setup)
**Subsequent Builds**: 1-2 minutes
