@echo off
echo Testing frontend configurations...
echo.

echo [1/3] Starting User Frontend on port 3000...
start "User Frontend" cmd /k "cd /d %~dp0frontend && npm run dev:user"
timeout /t 3 /nobreak >nul

echo [2/3] Starting Company Frontend on port 3001...
start "Company Frontend" cmd /k "cd /d %~dp0frontend && npm run dev:company"
timeout /t 3 /nobreak >nul

echo [3/3] Starting Admin Frontend on port 3002...
start "Admin Frontend" cmd /k "cd /d %~dp0frontend && npm run dev:admin"
timeout /t 3 /nobreak >nul

echo.
echo ========================================
echo All frontend services are starting!
echo ========================================
echo.
echo User Frontend:    http://localhost:3000
echo Company Frontend: http://localhost:3001
echo Admin Frontend:   http://localhost:3002
echo.
echo Press any key to exit...
pause >nul
