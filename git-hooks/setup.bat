@echo off
echo 🔧 Setting up Git hooks for Windows...

copy git-hooks\pre-commit.bat .git\hooks\pre-commit.bat
copy git-hooks\commit-msg.bat .git\hooks\commit-msg.bat

echo ✅ Git hooks installed successfully!
echo.
echo Hooks enabled:
echo   - pre-commit: runs Spotless check and tests
echo   - commit-msg: validates Conventional Commits format