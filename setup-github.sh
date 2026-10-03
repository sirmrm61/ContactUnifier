#!/bin/bash
# Contact Unifier - GitHub Push Setup Script
# This script prepares your project for GitHub and GitHub Actions

echo "=========================================="
echo "Contact Unifier - GitHub Setup"
echo "=========================================="
echo ""

PROJECT_ROOT="D:\project\cc_project\contactMerge\ContactUnifier"

# Step 1: Initialize Git (if not already done)
if [ ! -d "$PROJECT_ROOT/.git" ]; then
    echo "📁 Initializing Git repository..."
    cd "$PROJECT_ROOT"
    git init
    git config user.email "sir.mrm@gmail.com"
    git config user.name "Contact Unifier Developer"
    echo "✅ Git initialized"
else
    echo "✅ Git repository already exists"
fi

# Step 2: Add all files
echo ""
echo "📦 Staging all project files..."
cd "$PROJECT_ROOT"
git add .
git status
echo ""

# Step 3: Create initial commit
echo "💾 Creating initial commit..."
git commit -m "Initial commit: Contact Unifier - complete implementation

- Complete data layer with duplicate detection and merge logic
- Full presentation layer with 4 screens (Splash, Main, Detail, Settings)
- Material 3 UI with light/dark mode support
- Full localization (English + Persian) with RTL support
- Hilt dependency injection
- Kotlin Coroutines + Flow
- Ready for production build and testing"

echo ""
echo "=========================================="
echo "✅ Repository Ready!"
echo "=========================================="
echo ""
echo "Next steps:"
echo "1. Create repository on GitHub:"
echo "   Go to: https://github.com/new"
echo "   Repository name: ContactUnifier"
echo "   Choose: Private"
echo "   Click: Create repository"
echo ""
echo "2. Add remote and push (copy from GitHub):"
echo "   git remote add origin https://github.com/YOUR_USERNAME/ContactUnifier.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "3. Check build:"
echo "   Go to: https://github.com/YOUR_USERNAME/ContactUnifier"
echo "   Click: Actions tab"
echo "   Wait for green checkmark (5-7 minutes)"
echo ""
echo "4. Download APK:"
echo "   Click successful build → Artifacts → contact-unifier-debug"
echo ""
