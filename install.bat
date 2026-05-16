
@echo off
echo.
echo ========================================
echo   Install Frontend Dependencies
echo ========================================
echo.

set "PROJECT_DIR=C:\projects\smart-career-system"

echo Project directory: %PROJECT_DIR%
echo.

if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: frontend\package.json not found!
    echo Make sure the project structure is correct.
    pause
    exit /b 1
)

echo Installing npm dependencies...
echo This may take a few minutes...
echo.

cd /d %PROJECT_DIR%\frontend
call npm install

echo.
echo ========================================
echo   Installation Complete!
echo ========================================
echo.
pause
