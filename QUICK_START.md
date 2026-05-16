# 启动指南

## 环境准备

1. 安装 JDK 11+
2. 安装 Maven 3.6+
3. 安装 MySQL 8.0+
4. 安装 Node.js 16+ 和 npm 8+

## 数据库初始化

1. 登录 MySQL：
```bash
mysql -u root -p
```

2. 创建数据库：
```sql
CREATE DATABASE smart_career CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 导入数据：
```sql
USE smart_career;
SOURCE database-init.sql;
```

## 配置修改

### 后端配置
修改 `src/main/resources/application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_career
    username: root
    password: 你的密码
```

### 前端环境变量
修改 `frontend/.env`：
```env
VITE_API_BASE_URL=http://localhost:8080
VITE_API_PORT=8080
```

## 启动步骤

### 方式一：使用启动脚本

**Windows：** 双击 `start.bat`，按提示选择

**Linux/Mac：**
```bash
chmod +x start.sh
./start.sh
```

### 方式二：手动启动

**启动后端：**
```bash
mvn spring-boot:run
```

**启动前端（三个端口同时启动）：**
```bash
cd frontend
npm install
npm run dev:all
```

## 访问地址

- 求职者端：http://localhost:3000
- 企业端：http://localhost:3001
- 管理端：http://localhost:3002
- 后端API：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html

## 常见问题

### 后端启动失败
- 检查 MySQL 是否运行
- 检查 `application.yml` 数据库配置是否正确

### 前端启动失败
- 确保已执行 `npm install`
- 检查端口 3000/3001/3002 是否被占用

### 登录失败
- 检查数据库是否已导入初始数据
- 确认账号密码是否正确
