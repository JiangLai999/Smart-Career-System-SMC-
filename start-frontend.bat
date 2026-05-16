@echo off
echo.
echo ========================================
echo   Start Frontend Services
echo ========================================
echo.

set "PROJECT_DIR=C:\projects\smart-career-system"

if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found!
    echo Please run: cd %PROJECT_DIR%\frontend && npm install
    pause
    exit /b 1
)

echo Select frontend to start:
echo.
echo [1] User Frontend (Port 3000)
echo [2] Company Frontend (Port 3001)
echo [3] Admin Frontend (Port 3002)
echo [4] All Frontend Services
echo.

set /p choice="Please select [1-4]: "

if "%choice%"=="1" goto :user
if "%choice%"=="2" goto :company
if "%choice%"=="3" goto :admin
if "%choice%"=="4" goto :all
echo Invalid choice!
pause
exit /b 1

:user
echo.
echo Starting User Frontend...
start "User Frontend - Port 3000" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:user"
echo URL: http://localhost:3000
pause
exit /b 0

:company
echo.
echo Starting Company Frontend...
start "Company Frontend - Port 3001" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:company"
echo URL: http://localhost:3001
pause
exit /b 0

:admin
echo.
echo Starting Admin Frontend...
start "Admin Frontend - Port 3002" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:admin"
echo URL: http://localhost:3002
pause
exit /b 0

:all
echo.
echo Starting All Frontend Services...
start "All Frontend Services" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:all"
echo User: http://localhost:3000
echo Company: http://localhost:3001
echo Admin: http://localhost:3002
pause
exit /b 0
