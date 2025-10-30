@echo off
echo Starting Edge Detection Web Viewer...
echo.
cd /d "%~dp0"
echo Current directory: %CD%
echo.
echo Building TypeScript...
call npm run build
echo.
echo Starting web server on http://localhost:8080...
call npx http-server dist -p 8080 -o

