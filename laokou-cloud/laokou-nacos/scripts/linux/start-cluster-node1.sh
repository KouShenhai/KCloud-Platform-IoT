#!/bin/bash

# Nacos Cluster Mode - Node 1
# Copyright (c) 2022-2026 KCloud-Platform-IoT

echo "Starting Nacos Cluster Node 1..."

# ========================
# 基础配置
# ========================
export NACOS_CONSOLE_PORT=8048
export NACOS_SERVER_PORT=8848
export NACOS_MCP_REGISTRY_PORT=9080
export NACOS_DEPLOYMENT_TYPE=merged

export NACOS_LOG_PATH=./logs/nacos-node1
export NACOS_HOME=$(cd "$(dirname "$0")" && pwd)/logs/nacos-node1
export NACOS_IP=127.0.0.1

export NACOS_MEMBER_LIST="127.0.0.1:8848,127.0.0.1:8858,127.0.0.1:8868"

# ========================
# 数据源配置
# ========================
export NACOS_DS_PLATFORM=postgresql
export NACOS_DS_HOST=127.0.0.1
export NACOS_DS_PORT=5432
export NACOS_DS_DB=kcloud_platform_nacos

export SECRET_KEY=your_secret_key

# ========================
# JVM 参数
# ========================
JAVA_OPT="-Xms512m -Xmx512m -XX:+UseZGC -XX:+ZGenerational"
JAVA_OPT="$JAVA_OPT -Dnacos.standalone=false"
JAVA_OPT="$JAVA_OPT -Dnacos.home=$NACOS_HOME"
JAVA_OPT="$JAVA_OPT -Dnacos.inetutils.ip-address=$NACOS_IP"
JAVA_OPT="$JAVA_OPT -Dnacos.core.member.lookup.type=file"
JAVA_OPT="$JAVA_OPT -Dnacos.member.list=$NACOS_MEMBER_LIST"
JAVA_OPT="$JAVA_OPT -Dfile.encoding=UTF-8"
JAVA_OPT="$JAVA_OPT --add-opens java.base/java.util=ALL-UNNAMED"
JAVA_OPT="$JAVA_OPT --add-opens java.base/java.lang=ALL-UNNAMED"

# ========================
# JAR 路径
# ========================
BASE_DIR=$(cd "$(dirname "$0")" && pwd)
JAR_FILE="$BASE_DIR/target/laokou-nacos.jar"

# ========================
# 创建目录
# ========================
if [ ! -d "$NACOS_HOME" ]; then
    mkdir -p "$NACOS_HOME"
fi

# ========================
# 检查 JAR
# ========================
if [ ! -f "$JAR_FILE" ]; then
    echo ""
    echo "[ERROR] JAR file not found: $JAR_FILE"
    echo "[INFO] Please run 'mvn clean package -DskipTests'"
    echo ""
    exit 1
fi

# ========================
# 启动信息
# ========================
echo ""
echo "========================================"
echo "  Nacos Cluster Node 1 Starting..."
echo "========================================"
echo "  IP Address:   $NACOS_IP"
echo "  Console Port: $NACOS_CONSOLE_PORT"
echo "  Server Port:  $NACOS_SERVER_PORT"
echo "  Nacos Home:   $NACOS_HOME"
echo "  Cluster:      $NACOS_MEMBER_LIST"
echo "========================================"
echo ""

# ========================
# 启动
# ========================
java $JAVA_OPT -jar "$JAR_FILE"
