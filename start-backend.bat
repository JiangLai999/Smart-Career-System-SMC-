@echo off
echo.
echo ========================================
echo   Start Backend Only (Port 8080)
echo ========================================
echo.

set "PROJECT_DIR=C:\projects\smart-career-system"

echo Project directory: %PROJECT_DIR%
echo.
echo Starting Spring Boot backend...
cd /d %PROJECT_DIR%
mvn spring-boot:run
