@echo off
echo 🔍 Running pre-commit checks...

echo 📋 Checking code formatting with Spotless...
call mvn spotless:check
if %errorlevel% neq 0 (
    echo ❌ Code formatting check failed. Run 'mvn spotless:apply' to fix.
    exit /b 1
)

echo 🧪 Running tests...
call mvn test
if %errorlevel% neq 0 (
    echo ❌ Tests failed. Fix them before committing.
    exit /b 1
)

echo ✅ All pre-commit checks passed!
exit /b 0