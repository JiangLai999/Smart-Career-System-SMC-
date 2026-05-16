@echo off
echo ========================================
echo   Database Status Check
echo ========================================
echo.
set /p DB_PASS="Enter MySQL password: "

mysql -u root -p%DB_PASS% -e "USE smart_career; SELECT 'enterprise_hr count:' as '', COUNT(*) as '' FROM enterprise_hr;"
echo.

mysql -u root -p%DB_PASS% -e "USE smart_career; SELECT 'users by dtype:' as ''; SELECT dtype, COUNT(*) as count FROM users GROUP BY dtype;"
echo.

mysql -u root -p%DB_PASS% -e "USE smart_career; SELECT 'enterprise table:' as ''; SELECT id, enterprise_name FROM enterprise LIMIT 10;"
echo.

mysql -u root -p%DB_PASS% -e "USE smart_career; SELECT 'users with enterprise:' as ''; SELECT u.id, u.username, u.dtype, u.user_type, eh.enterprise_id, e.enterprise_name FROM users u LEFT JOIN enterprise_hr eh ON u.id = eh.id LEFT JOIN enterprise e ON eh.enterprise_id = e.id WHERE u.username LIKE '%%_hr' LIMIT 15;"
echo.

mysql -u root -p%DB_PASS% -e "USE smart_career; SELECT 'users by user_type:' as ''; SELECT user_type, COUNT(*) as count FROM users GROUP BY user_type;"
echo.

pause