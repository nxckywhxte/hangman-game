@echo off
setlocal enabledelayedexpansion

set "commit_msg_file=%~1"

if not exist "%commit_msg_file%" (
    echo ❌ Commit message file not found!
    exit /b 1
)

:: Читаем первую строку
set /p commit_msg=<"%commit_msg_file%"

:: Проверяем формат через findstr (Windows-аналог grep)
echo %commit_msg% | findstr /R "^(feat\|fix\|docs\|style\|refactor\|perf\|test\|build\|ci\|chore\|revert)(\(.*\))?: .*" >nul
if errorlevel 1 (
    echo ❌ Commit message does not follow Conventional Commits format!
    echo.
    echo Expected format: ^<type^(^scope^)^>: ^<description^>
    echo.
    echo Your message: %commit_msg%
    exit /b 1
)

echo ✅ Commit message format is valid!
exit /b 0