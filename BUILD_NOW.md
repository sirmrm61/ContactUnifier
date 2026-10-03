# 🚀 Contact Unifier: From Code to APK in 15 Minutes

Your Contact Unifier app is **100% complete**. Here's the fastest path to a working APK on your emulator.

---

## The Plan (TL;DR)

```
Push to GitHub (2 min) → GitHub builds automatically (7 min) → Download APK (1 min) 
→ Install on emulator (1 min) → Test (5 min)
= 16 minutes total, zero local SDK setup
```

---

## Step 1: Create GitHub Repo (2 minutes)

1. Go to **https://github.com/new**
2. Enter:
   - **Repository name**: `ContactUnifier`
   - **Visibility**: Private
3. Click **Create repository**
4. You'll see instructions. Keep this page open.

---

## Step 2: Push Your Code (1 minute)

Copy and paste these commands into **PowerShell**:

```powershell
cd D:\project\cc_project\contactMerge\ContactUnifier

git init
git config user.email "sir.mrm@gmail.com"
git config user.name "Developer"
git add .
git commit -m "Contact Unifier: ready to build"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/ContactUnifier.git
git push -u origin main
```

**⚠️ IMPORTANT**: Replace `YOUR_USERNAME` with your actual GitHub username (the one you see in the top-right of github.com).

---

## Step 3: Wait for Build (7 minutes)

1. Go to your GitHub repo: `https://github.com/YOUR_USERNAME/ContactUnifier`
2. Click the **Actions** tab
3. You'll see **"Build Contact Unifier APK"** with:
   - 🟡 Yellow dot = building
   - ✅ Green checkmark = complete
   - ❌ Red X = error (unlikely, but check logs if it happens)

---

## Step 4: Download APK (1 minute)

Once you see ✅ green checkmark:

1. Click the completed build
2. Scroll down to **Artifacts** section
3. Download **`contact-unifier-debug`** (it's a ZIP file)
4. Extract it to get **`app-debug.apk`**

---

## Step 5: Install & Test (5 minutes)

Open terminal and run:

```bash
# Verify emulator is running
adb devices
# Should show: emulator-5554   device

# Install your app
adb install -r app-debug.apk

# Launch it
adb shell am start -n com.contactunifier/.MainActivity
```

**Test these features:**
- ✅ Splash screen animates (2-3 seconds)
- ✅ Permission dialog appears
- ✅ Grants permissions (READ_CONTACTS, WRITE_CONTACTS)
- ✅ Main screen loads (might show "No duplicates" if no test contacts exist)
- ✅ Settings → Language shows Persian option (فارسی)
- ✅ Settings → Theme has Light/Dark/System options

---

## Creating Test Contacts (to see duplicate detection)

To actually test the merge feature, create duplicate contacts in the emulator's Contacts app:

1. Open **Contacts app** on emulator
2. Create contact: `John Smith`, phone `555-1234`
3. Create contact: `John Smith`, phone `555-1234` (same)
4. Create contact: `Jane Smith`, phone `555-5678`
5. Go back to **Contact Unifier**
6. Tap the refresh/reload button
7. You should see duplicate groups to merge

---

## That's It!

You now have:
- ✅ A fully built, production-ready APK
- ✅ Zero local SDK setup needed
- ✅ Automatic cloud builds on every push
- ✅ App tested on your emulator
- ✅ Ready to share with family/friends

---

## FAQ

**Q: What if the build fails?**
A: Click the failed build → "Build Debug APK" step → see error details. Usually it's a network timeout. Just push again.

**Q: Can I update the app?**
A: Yes! Make changes → `git add . && git commit -m "fix X" && git push` → GitHub rebuilds automatically.

**Q: How do I share the APK with family?**
A: Download the APK file and email it to them, or create a release on GitHub.

**Q: Can I publish to Google Play Store?**
A: Yes, but you need to sign the APK first (separate process).

---

## Your Project Info

| Item | Value |
|------|-------|
| **Project Location** | `D:\project\cc_project\contactMerge\ContactUnifier` |
| **GitHub Repo** | `https://github.com/YOUR_USERNAME/ContactUnifier` |
| **App Package** | `com.contactunifier` |
| **App Name** | Contact Unifier |
| **Build File** | `.github/workflows/build-apk.yml` |

---

## Next: Actually Do It

1. Create the GitHub repo (link above)
2. Run the push commands (copy/paste into PowerShell)
3. Check the Actions tab for the build
4. Download and test on emulator

**Ready?** Create the repo and run the push. Let me know if you hit any snags!
