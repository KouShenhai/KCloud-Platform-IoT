@echo off
REM Nacos Standalone Mode Startup Script
REM Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.

title Nacos Standalone Mode

chcp 65001 > nul

set NACOS_CONSOLE_PORT=8048
set NACOS_SERVER_PORT=8848
set NACOS_MCP_REGISTRY_PORT=9080
set NACOS_DEPLOYMENT_TYPE=merged
set NACOS_LOG_PATH=./logs/nacos
set NACOS_HOME=%~dp0/logs/nacos

set NACOS_DS_PLATFORM=postgresql
set NACOS_DS_HOST=127.0.0.1
set NACOS_DS_PORT=5432
set NACOS_DS_DB=kcloud_platform_nacos

set SECRET-KEY=your_secret_key

set JAVA_OPT=-Xms512m -Xmx512m -XX:+UseG1GC -XX:+ZGenerational
set JAVA_OPT=%JAVA_OPT% -Dnacos.standalone=true
set JAVA_OPT=%JAVA_OPT% -Dnacos.mode=standalone
set JAVA_OPT=%JAVA_OPT% -Dfile.encoding=UTF-8

set JAR_FILE=%~dp0target\laokou-nacos.jar

if not exist "%JAR_FILE%" (
    echo.
    echo [ERROR] JAR file not found: %JAR_FILE%
    echo [INFO] Please run 'mvn clean package -DskipTests' to build the project first.
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Nacos Standalone Mode Starting...
echo ========================================
echo   Console Port: %NACOS_CONSOLE_PORT%
echo   Server Port:  %NACOS_SERVER_PORT%
echo   MCP Port:     %NACOS_MCP_REGISTRY_PORT%
echo ========================================
echo.

java %JAVA_OPT% -jar "%JAR_FILE%"

pause
