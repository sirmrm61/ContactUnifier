# Contact Unifier - GitHub Push Instructions

## 📋 Prerequisites
- GitHub account (create at https://github.com/signup if needed)
- Git installed on your system
- Your Contact Unifier project at: `D:\project\cc_project\contactMerge\ContactUnifier`

---

## ✅ Step 1: Create GitHub Repository

1. Go to **https://github.com/new**
2. Fill in:
   - **Repository name**: `ContactUnifier`
   - **Description**: Contact deduplication app (optional)
   - **Visibility**: Private
3. Click **Create repository**
4. Copy the URL shown (should be: `https://github.com/YOUR_USERNAME/ContactUnifier.git`)

---

## ✅ Step 2: Push Your Project

Open **PowerShell** and run these commands exactly:

```powershell
# Navigate to your project
cd D:\project\cc_project\contactMerge\ContactUnifier

# Initialize git (if not already done)
git init

# Configure git (one-time)
git config user.email "sir.mrm@gmail.com"
git config user.name "Contact Unifier Developer"

# Stage all files
git add .

# Create commit
git commit -m "Initial commit: Contact Unifier - production ready

- Complete data layer with duplicate detection and merge logic
- Full presentation layer with 4 screens
- Material 3 UI with light/dark mode and RTL support
- Full English and Persian localization
- Hilt dependency injection
- Ready for GitHub Actions cloud build"

# Rename branch to main
git branch -M main

# Add remote (REPLACE YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/ContactUnifier.git

# Push to GitHub
git push -u origin main
```

**⚠️ Replace `YOUR_USERNAME`** with your actual GitHub username in the `git remote add` command.

---

## ✅ Step 3: Verify Push Succeeded

1. Go to: `https://github.com/YOUR_USERNAME/ContactUnifier`
2. You should see all your project files
3. Look for the yellow/orange dot next to the commit hash (workflow running)

---

## ✅ Step 4: Watch the Build

1. Click the **Actions** tab
2. You'll see **"Build Contact Unifier APK"** workflow
3. Status indicators:
   - 🟡 **Yellow circle** = Building
   - ✅ **Green checkmark** = Success (5-7 minutes)
   - ❌ **Red X** = Failed (click to see error)

---

## ✅ Step 5: Download Your APK

Once the build succeeds (green checkmark):

1. Click the successful build
2. Scroll to **Artifacts** section
3. Download **`contact-unifier-debug.zip`**
4. Extract the ZIP file to get **`app-debug.apk`**

---

## ✅ Step 6: Install on Emulator

```bash
# Verify emulator is running
adb devices
# Expected: emulator-5554   device

# Install the APK
adb install -r app-debug.apk

# Launch the app
adb shell am start -n com.contactunifier/.MainActivity
```

---

## 📊 Timeline

| Step | Time | What Happens |
|------|------|---|
| Push code | 1 min | Upload to GitHub |
| Build starts | Auto | GitHub Actions triggered |
| Build complete | 5-7 min | APK created and uploaded |
| Download | 1 min | Download from artifacts |
| Install | 1 min | Install on emulator |
| **Total** | **~15 min** | **App ready to test** |

---

## 🆘 Troubleshooting

### "fatal: not a git repository"
```powershell
cd D:\project\cc_project\contactMerge\ContactUnifier
git init
```

### "fatal: remote origin already exists"
```powershell
git remote remove origin
git remote add origin https://github.com/YOUR_USERNAME/ContactUnifier.git
```

### Build Failed
Click the failed build in Actions tab → see the error. Usually:
- Network timeout → try again
- SDK issue → workflow will auto-fix on retry

### APK not in artifacts
The build probably failed. Check the build logs for errors.

---

## ✨ That's It!

Once your APK is built and installed, you have a complete, production-ready Contact Unifier app ready to:
- ✅ Test duplicate detection and merging
- ✅ Test English ↔ Persian language switching
- ✅ Test light/dark mode themes
- ✅ Share with family and friends
- ✅ Deploy to Google Play Store (optional)

**Ready? Run the commands above and let me know when you hit the GitHub build page!**
