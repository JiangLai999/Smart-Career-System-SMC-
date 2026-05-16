#!/bin/bash

echo "=========================================="
echo "  智能职业规划与就业系统 - 启动脚本"
echo "=========================================="
echo ""

echo "[1] 启动后端服务 (端口: 8080)"
echo "[2] 启动求职者端 (端口: 3000)"
echo "[3] 启动企业端 (端口: 3001)"
echo "[4] 启动管理端 (端口: 3002)"
echo "[5] 启动所有前端服务"
echo "[6] 启动后端 + 所有前端"
echo "[0] 退出"
echo ""

read -p "请选择操作 [0-6]: " choice

case $choice in
    1)
        echo "正在启动后端服务..."
        mvn spring-boot:run &
        echo "后端服务启动中..."
        echo "访问地址: http://localhost:8080"
        echo "API文档: http://localhost:8080/swagger-ui.html"
        ;;
    2)
        echo "正在启动求职者端..."
        cd frontend && npm run dev:user &
        echo "求职者端启动中..."
        echo "访问地址: http://localhost:3000"
        ;;
    3)
        echo "正在启动企业端..."
        cd frontend && npm run dev:company &
        echo "企业端启动中..."
        echo "访问地址: http://localhost:3001"
        ;;
    4)
        echo "正在启动管理端..."
        cd frontend && npm run dev:admin &
        echo "管理端启动中..."
        echo "访问地址: http://localhost:3002"
        ;;
    5)
        echo "正在启动所有前端服务..."
        cd frontend && npm run dev:all &
        echo "所有前端服务启动中..."
        echo "求职者端: http://localhost:3000"
        echo "企业端: http://localhost:3001"
        echo "管理端: http://localhost:3002"
        ;;
    6)
        echo "正在启动后端服务..."
        mvn spring-boot:run &
        sleep 5
        echo "正在启动所有前端服务..."
        cd frontend && npm run dev:all &
        echo ""
        echo "=========================================="
        echo "所有服务启动中，请稍候..."
        echo "=========================================="
        echo "后端服务: http://localhost:8080"
        echo "API文档: http://localhost:8080/swagger-ui.html"
        echo "求职者端: http://localhost:3000"
        echo "企业端: http://localhost:3001"
        echo "管理端: http://localhost:3002"
        echo "=========================================="
        ;;
    0)
        echo "退出启动脚本"
        exit 0
        ;;
    *)
        echo "无效选择"
        ;;
esac
