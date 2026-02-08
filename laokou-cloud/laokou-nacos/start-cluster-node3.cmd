@echo off
REM Nacos Cluster Mode - Node 3
REM Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.

title Nacos Cluster Node 3

chcp 65001 > nul

set NACOS_CONSOLE_PORT=8068
set NACOS_SERVER_PORT=8868
set NACOS_MCP_REGISTRY_PORT=9082
set NACOS_DEPLOYMENT_TYPE=merged
set NACOS_LOG_PATH=./logs/nacos-node3
set NACOS_HOME=%~dp0/logs/nacos-node3
set NACOS_IP=127.0.0.1

set NACOS_MEMBER_LIST=127.0.0.1:8848,127.0.0.1:8858,127.0.0.1:8868

set NACOS_DS_PLATFORM=postgresql
set NACOS_DS_HOST=127.0.0.1
set NACOS_DS_PORT=5432
set NACOS_DS_DB=kcloud_platform_nacos

set SECRET-KEY=your_secret_key

set JAVA_OPT=-Xms512m -Xmx512m -XX:+UseZGC -XX:+ZGenerational
set JAVA_OPT=%JAVA_OPT% -Dnacos.standalone=false
set JAVA_OPT=%JAVA_OPT% -Dnacos.home=%NACOS_HOME%
set JAVA_OPT=%JAVA_OPT% -Dnacos.inetutils.ip-address=%NACOS_IP%
set JAVA_OPT=%JAVA_OPT% -Dnacos.core.member.lookup.type=file
set JAVA_OPT=%JAVA_OPT% -Dnacos.member.list=%NACOS_MEMBER_LIST%
set JAVA_OPT=%JAVA_OPT% -Dfile.encoding=UTF-8

set JAR_FILE=%~dp0target\laokou-nacos.jar

if not exist "%NACOS_HOME%" mkdir "%NACOS_HOME%"

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
echo   Nacos Cluster Node 3 Starting...
echo ========================================
echo   IP Address:   %NACOS_IP%
echo   Console Port: %NACOS_CONSOLE_PORT%
echo   Server Port:  %NACOS_SERVER_PORT%
echo   Nacos Home:   %NACOS_HOME%
echo   Cluster:      %NACOS_MEMBER_LIST%
echo ========================================
echo.

java %JAVA_OPT% -jar "%JAR_FILE%"

pause
