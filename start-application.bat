@echo off
echo ========================================
echo   Catering Management System Launcher
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher and try again
    echo.
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Apache Maven and try again
    echo.
    pause
    exit /b 1
)

REM Check if PostgreSQL is running
echo Checking PostgreSQL connection...
pg_isready -h localhost -p 5432 >nul 2>&1
if %errorlevel% neq 0 (
    echo WARNING: PostgreSQL is not running on localhost:5432
    echo Please ensure PostgreSQL is installed and running
    echo.
    echo You can start PostgreSQL service using:
    echo   net start postgresql-x64-13  (or your PostgreSQL service name)
    echo.
    set /p continue="Continue anyway? (y/n): "
    if /i not "%continue%"=="y" (
        exit /b 1
    )
)

echo.
echo Building the application...
echo ========================================

REM Clean and build the project
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed!
    echo Please check the error messages above
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.

REM Check if database is initialized
echo Checking database setup...
psql -h localhost -p 5432 -U catering_user -d catering_db -c "\dt" >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo Database not found or not accessible.
    echo Please run the database setup first:
    echo.
    echo 1. Create database: createdb -U postgres catering_db
    echo 2. Create user: psql -U postgres -c "CREATE USER catering_user WITH PASSWORD 'catering_pass';"
    echo 3. Grant privileges: psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE catering_db TO catering_user;"
    echo 4. Initialize schema: psql -U catering_user -d catering_db -f database/init.sql
    echo 5. Load sample data: psql -U catering_user -d catering_db -f database/sample-data.sql
    echo.
    set /p continue="Continue anyway? (y/n): "
    if /i not "%continue%"=="y" (
        exit /b 1
    )
)

echo.
echo Starting Catering Management System...
echo ========================================
echo.
echo The application will be available at:
echo   http://localhost:8080/catering
echo.
echo Press Ctrl+C to stop the application
echo.

REM Start the Spring Boot application
java -jar target/catering-management.war

REM If we reach here, the application has stopped
echo.
echo Application stopped.
pause

