#!/bin/bash

# Nacos Standalone Mode Startup Script
# Copyright (c) 2022-2026 KCloud-Platform-IoT

echo "Nacos Standalone Mode"

# ========== 基础配置 ==========
export NACOS_CONSOLE_PORT=8048
export NACOS_SERVER_PORT=8848
export NACOS_MCP_REGISTRY_PORT=9080
export NACOS_DEPLOYMENT_TYPE=merged
export NACOS_LOG_PATH=./logs/nacos
export NACOS_HOME=$(cd "$(dirname "$0")"; pwd)/logs/nacos

# ========== 数据源 ==========
export NACOS_DS_PLATFORM=postgresql
export NACOS_DS_HOST=127.0.0.1
export NACOS_DS_PORT=5432
export NACOS_DS_DB=kcloud_platform_nacos

export SECRET_KEY=your_secret_key

# ========== JVM 参数 ==========
JAVA_OPT="-Xms512m -Xmx512m -XX:+UseG1GC -XX:+ZGenerational"
JAVA_OPT="$JAVA_OPT -Dnacos.standalone=true"
JAVA_OPT="$JAVA_OPT -Dnacos.mode=standalone"
JAVA_OPT="$JAVA_OPT -Dfile.encoding=UTF-8"
JAVA_OPT="$JAVA_OPT --add-opens java.base/java.util=ALL-UNNAMED"
JAVA_OPT="$JAVA_OPT --add-opens java.base/java.lang=ALL-UNNAMED"

# ========== JAR 路径 ==========
BASE_DIR=$(cd "$(dirname "$0")"; pwd)
JAR_FILE="$BASE_DIR/target/laokou-nacos.jar"

# ========== 校验 ==========
if [ ! -f "$JAR_FILE" ]; then
    echo
    echo "[ERROR] JAR file not found: $JAR_FILE"
    echo "[INFO] Please run 'mvn clean package -DskipTests' first."
    echo
    exit 1
fi

echo
echo "========================================"
echo "  Nacos Standalone Mode Starting..."
echo "========================================"
echo "  Console Port: $NACOS_CONSOLE_PORT"
echo "  Server Port:  $NACOS_SERVER_PORT"
echo "  MCP Port:     $NACOS_MCP_REGISTRY_PORT"
echo "========================================"
echo

# ========== 启动 ==========
java $JAVA_OPT -jar "$JAR_FILE"
