@echo off
echo Starting Smart Career System Frontends...
echo.
echo User Frontend: http://localhost:3000
echo Company Frontend: http://localhost:3001
echo Admin Frontend: http://localhost:3002
echo.
echo Press Ctrl+C to stop all servers
echo.

start "User Frontend" cmd /c "npm run dev:user"
timeout /t 2 /nobreak > nul
start "Company Frontend" cmd /c "npm run dev:company"
timeout /t 2 /nobreak > nul
start "Admin Frontend" cmd /c "npm run dev:admin"

echo All frontends started!
echo User: http://localhost:3000
echo Company: http://localhost:3001
echo Admin: http://localhost:3002
pause
