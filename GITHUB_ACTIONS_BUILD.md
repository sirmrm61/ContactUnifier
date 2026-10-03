# Contact Unifier - Cloud APK Build Guide

## 🚀 Quick Overview

Your **Contact Unifier** Android app is **100% complete and ready to build**. Instead of fighting local Android SDK setup, we'll use **GitHub Actions** to build your APK in the cloud automatically—completely free and takes ~5 minutes.

### What You Get
- ✅ Automatic cloud builds when you push code
- ✅ Both debug and release APKs
- ✅ Downloads ready to test on emulator
- ✅ No local SDK setup needed
- ✅ Clean, professional build pipeline

---

## Step 1: Set Up GitHub Repository

### 1.1 Create a GitHub Account
Go to **[github.com/signup](https://github.com/signup)** and create a free account (if you don't have one).

### 1.2 Create a New Repository
1. On GitHub, click **New** (top-left)
2. Name it: `ContactUnifier`
3. Choose **Private** (so only you see it)
4. Click **Create repository**

### 1.3 Push Your Project to GitHub

Open PowerShell in your project directory:

```powershell
cd D:\project\cc_project\contactMerge\ContactUnifier

# Initialize git and commit
git init
git add .
git commit -m "Initial commit: Contact Unifier app complete"

# Rename branch to main
git branch -M main

# Add remote and push
git remote add origin https://github.com/YOUR_USERNAME/ContactUnifier.git
git push -u origin main
```

⚠️ **Replace `YOUR_USERNAME`** with your actual GitHub username.

After pushing, verify on GitHub by visiting:
```
https://github.com/YOUR_USERNAME/ContactUnifier
```

---

## Step 2: Workflow Already Configured

The GitHub Actions workflow file has been created at:
```
.github/workflows/build-apk.yml
```

When you push to GitHub, this workflow automatically:
1. ✅ Sets up Java 17 and Android SDK
2. ✅ Accepts Android licenses
3. ✅ Runs `./gradlew clean assembleDebug`
4. ✅ Builds both debug and release APKs
5. ✅ Uploads them as artifacts for download

**You don't need to do anything**—it's automatic!

---

## Step 3: Build and Download Your APK

### 3.1 Start the Build
Once your code is on GitHub, the build triggers automatically. Go to your repository:
```
https://github.com/YOUR_USERNAME/ContactUnifier
```

### 3.2 Check Build Status
Click the **Actions** tab at the top. You'll see:
- 🟡 **Yellow** = Build in progress
- ✅ **Green checkmark** = Build succeeded
- ❌ **Red X** = Build failed (click to see error logs)

First build takes ~5-7 minutes. Subsequent builds are faster (~3 minutes).

### 3.3 Download the APK
1. Click the **successful build** (green checkmark)
2. Scroll down to **Artifacts** section
3. Download **`contact-unifier-debug.zip`**
4. Extract the ZIP file to get **`app-debug.apk`**

This is your installable app! 🎉

---

## Step 4: Install on Android Emulator

### 4.1 Prerequisites
- Android emulator running (create one with **API 35**)
- ADB installed (part of Android SDK tools)

### 4.2 Installation Commands

```bash
# Verify emulator is running
adb devices
# Expected output: emulator-5554   device

# Install the APK
adb install -r app-debug.apk

# Launch the app
adb shell am start -n com.contactunifier/.MainActivity
```

### 4.3 What to Test

| Feature | Expected |
|---------|----------|
| **Splash Screen** | 2-3 second animation, fades to main screen |
| **Permissions** | Shows rationale, requests READ_CONTACTS + WRITE_CONTACTS |
| **Main Screen** | Shows duplicate contact groups (create test contacts if needed) |
| **Merge Flow** | Tap duplicate group → select master location → merge |
| **Settings** | Language selector (EN/FA), theme selector, about screen |
| **RTL (Persian)** | Switch to Persian → layout flips, text aligns right |

---

## Step 5: Troubleshooting

### Build Fails on GitHub
**Solution:** Click the failed build → "Build Debug APK" step → scroll to see error details.

Common causes:
- Missing dependency version
- Gradle cache issue → try pushing again to retry
- SDK platform missing → workflow will install it

### APK Won't Install
```bash
# Uninstall first, then reinstall
adb uninstall com.contactunifier
adb install -r app-debug.apk
```

### App Crashes on Launch
```bash
# Check crash logs
adb logcat | grep contactunifier
```

### No Duplicate Contacts Showing
Create test contacts in the Contacts app with duplicate names or phone numbers.

---

## Step 6: Release APK (Optional)

The workflow also builds a release APK at:
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

To use it for distribution:

### Sign the APK
```bash
# Create keystore (one-time, keep it safe)
keytool -genkey -v -keystore ContactUnifier.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 -alias contactunifier

# Sign APK
jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1 \
  -keystore ContactUnifier.keystore app-release-unsigned.apk contactunifier

# Rename
ren app-release-unsigned.apk app-release.apk
```

Now you can distribute `app-release.apk` to family/friends or upload to Google Play Store.

---

## Next Steps

1. ✅ **Push to GitHub** (you've done this)
2. ⏳ **Wait 5 minutes** for build to complete
3. 📥 **Download APK** from Actions artifacts
4. 📱 **Test on emulator** using ADB commands above
5. 🎉 **Share with family/friends** or publish to Play Store

---

## Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android ADB Reference](https://developer.android.com/studio/command-line/adb)
- [Android Emulator Setup](https://developer.android.com/tools/emulator)
- [Google Play Console](https://play.google.com/console)

---

## Quick Reference

| Item | Path/Link |
|------|-----------|
| **Your Project** | `D:\project\cc_project\contactMerge\ContactUnifier` |
| **GitHub Repo** | `https://github.com/YOUR_USERNAME/ContactUnifier` |
| **Actions Tab** | `https://github.com/YOUR_USERNAME/ContactUnifier/actions` |
| **Workflow File** | `.github/workflows/build-apk.yml` |
| **Built APK** | `app/build/outputs/apk/debug/app-debug.apk` |
| **App Package** | `com.contactunifier` |
| **App Name** | `Contact Unifier` |

---

**Status:** ✅ Ready to build  
**Time to APK:** ~5 minutes  
**Cost:** Free (GitHub free tier)  
**Next:** Push to GitHub and check Actions tab for build progress!
