@echo off
echo.
echo ========================================
echo   Smart Career System - Start Menu
echo ========================================
echo.

REM Set project directory
set "PROJECT_DIR=C:\projects\smart-career-system"

echo Project directory: %PROJECT_DIR%
echo.

REM Check if project directory exists
if not exist "%PROJECT_DIR%" (
    echo ERROR: Project directory not found: %PROJECT_DIR%
    pause
    exit /b 1
)

echo [1] Start Backend (Port 8080)
echo [2] Start User Frontend (Port 3000)
echo [3] Start Company Frontend (Port 3001)
echo [4] Start Admin Frontend (Port 3002)
echo [5] Start All Frontend Services
echo [6] Start Backend + All Frontend
echo [7] Install Frontend Dependencies (npm install)
echo [0] Exit
echo.

set /p choice="Please select [0-7]: "

if "%choice%"=="0" goto :end
if "%choice%"=="1" goto :backend
if "%choice%"=="2" goto :user
if "%choice%"=="3" goto :company
if "%choice%"=="4" goto :admin
if "%choice%"=="5" goto :frontend
if "%choice%"=="6" goto :all
if "%choice%"=="7" goto :install
echo Invalid choice!
pause
exit /b 1

:backend
echo.
echo Cleaning and compiling...
cd /d %PROJECT_DIR%
call mvn clean compile -q
if errorlevel 1 (
    echo ERROR: Maven compile failed!
    pause
    exit /b 1
)
echo Starting backend service...
start "Smart Career Backend" cmd /k "cd /d %PROJECT_DIR% && mvn spring-boot:run"
echo Backend starting...
echo URL: http://localhost:8080
echo API Docs: http://localhost:8080/swagger-ui.html
pause
exit /b 0

:user
echo.
echo Starting user frontend...
if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found! Please run option [7] first.
    pause
    exit /b 1
)
start "User Frontend - Port 3000" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:user"
echo User frontend starting...
echo URL: http://localhost:3000
pause
exit /b 0

:company
echo.
echo Starting company frontend...
if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found! Please run option [7] first.
    pause
    exit /b 1
)
start "Company Frontend - Port 3001" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:company"
echo Company frontend starting...
echo URL: http://localhost:3001
pause
exit /b 0

:admin
echo.
echo Starting admin frontend...
if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found! Please run option [7] first.
    pause
    exit /b 1
)
start "Admin Frontend - Port 3002" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:admin"
echo Admin frontend starting...
echo URL: http://localhost:3002
pause
exit /b 0

:frontend
echo.
echo Starting all frontend services...
if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found! Please run option [7] first.
    pause
    exit /b 1
)
start "All Frontend Services" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:all"
echo All frontend services starting...
echo User: http://localhost:3000
echo Company: http://localhost:3001
echo Admin: http://localhost:3002
pause
exit /b 0

:all
echo.
echo Cleaning and compiling...
cd /d %PROJECT_DIR%
call mvn clean compile -q
if errorlevel 1 (
    echo ERROR: Maven compile failed!
    pause
    exit /b 1
)
echo Starting backend service...
start "Smart Career Backend" cmd /k "cd /d %PROJECT_DIR% && mvn spring-boot:run"
echo Waiting 8 seconds for backend...
ping 127.0.0.1 -n 9 >nul

echo Starting all frontend services...
if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found! Please run option [7] first.
    pause
    exit /b 1
)
start "All Frontend Services" cmd /k "cd /d %PROJECT_DIR%\frontend && npm run dev:all"
echo.
echo ========================================
echo   All services are starting!
echo ========================================
echo Backend: http://localhost:8080
echo API Docs: http://localhost:8080/swagger-ui.html
echo User Frontend: http://localhost:3000
echo Company Frontend: http://localhost:3001
echo Admin Frontend: http://localhost:3002
echo ========================================
pause
exit /b 0

:install
echo.
echo Installing frontend dependencies...
if not exist "%PROJECT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found!
    pause
    exit /b 1
)
echo Running npm install...
cd /d %PROJECT_DIR%\frontend
call npm install
echo.
echo Dependencies installed successfully!
pause
exit /b 0

:end
echo Exiting...
exit /b 0
