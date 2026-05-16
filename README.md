# 智能职业规划与就业系统

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-6DB33F?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue.js-3.4-4FC08D?logo=vue.js)](https://vuejs.org/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)](https://www.mysql.com/)
[![Vite](https://img.shields.io/badge/Vite-5.0-646CFF?logo=vite)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.4-06B6D4?logo=tailwind-css)](https://tailwindcss.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

## 项目简介

智能职业规划与就业推荐系统是一个连接求职者与企业的全栈Web应用，采用前后端分离架构，为用户提供职位搜索、简历管理、招聘发布、面试安排等一站式就业服务。

## 项目大纲

### 技术架构

```
smart-career-system/
├── src/                          # Java后端源码
│   └── main/
│       ├── java/com/example/smartcareer/
│       │   ├── controller/       # REST API控制器
│       │   ├── service/           # 业务逻辑层
│       │   ├── repository/        # 数据访问层
│       │   ├── entity/            # 实体类
│       │   ├── dto/               # 数据传输对象
│       │   ├── config/            # 配置类
│       │   └── security/          # 安全认证
│       └── resources/
│           └── application.yml    # 应用配置
├── frontend/                     # Vue3前端源码
│   ├── src/
│   │   ├── api/                  # API接口封装
│   │   ├── components/           # 公共组件
│   │   ├── views/                # 页面组件
│   │   ├── router/               # 路由配置
│   │   ├── store/                # 状态管理
│   │   └── composables/          # 组合式函数
│   ├── index-user.html           # 求职者端入口
│   ├── index-company.html        # 企业端入口
│   ├── index-admin.html          # 管理端入口
│   └── vite.config.*.js          # Vite配置
└── pom.xml                      # Maven配置
```

### 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.7.18 |
| Java版本 | Java 17 |
| 数据库 | MySQL 8.0 |
| ORM | Spring Data JPA |
| 安全认证 | Spring Security + JWT |
| API文档 | SpringDoc OpenAPI |
| 前端框架 | Vue 3 |
| 构建工具 | Vite |
| 状态管理 | Pinia |
| HTTP客户端 | Axios |
| UI框架 | Tailwind CSS |

---

## 功能介绍

### 一、求职者端 (端口 3000)

#### 1. 用户模块
- **账号注册** - 手机号/邮箱注册，支持验证码验证
- **账号登录** - 密码登录，支持JWT token认证
- **个人信息** - 查看和编辑个人资料

#### 2. 职位搜索
- **职位列表** - 分页浏览所有在招职位
- **职位搜索** - 按关键词、职位类型、工作地点筛选
- **职位详情** - 查看职位要求、薪资范围、福利待遇
- **职位收藏** - 收藏感兴趣的职位

#### 3. 简历管理
- **创建简历** - 填写基本信息、教育经历、工作经验
- **简历预览** - 查看简历完整展示效果
- **简历投递** - 一键投递简历到目标企业

#### 4. 投递管理
- **投递记录** - 查看所有已投递的职位
- **面试通知** - 查看企业发送的面试邀请
- **录用结果** - 查看录用状态

#### 5. 职业测评
- **能力测评** - 在线完成职业能力测试
- **测评报告** - 查看测评结果和建议

#### 6. 消息通知
- **系统通知** - 接收平台推送的消息
- **面试提醒** - 面试时间提醒

---

### 二、企业端 (端口 3001)

#### 1. 企业模块
- **企业注册** - 提交企业资质信息
- **企业登录** - HR账号登录
- **企业资料** - 编辑企业简介、规模、福利

#### 2. 职位管理
- **发布职位** - 创建新的招聘职位
- **职位编辑** - 修改职位信息
- **职位下架** - 停止招聘
- **职位列表** - 查看所有发布中的职位

#### 3. 简历管理
- **简历筛选** - 查看投递的简历列表
- **简历搜索** - 按条件搜索人才
- **简历详情** - 查看求职者完整简历
- **简历收藏** - 收藏优秀人才

#### 4. 面试管理
- **发送面试** - 向求职者发送面试邀请
- **面试安排** - 设置面试时间、地点
- **面试评价** - 记录面试评价

#### 5. 录用管理
- **发送录用** - 发送录用通知
- **录用跟踪** - 跟踪候选人入职状态

---

### 三、管理端 (端口 3002)

#### 1. 用户管理
- **求职者列表** - 查看所有注册用户
- **用户禁用/启用** - 账号状态管理

#### 2. 企业管理
- **企业审核** - 审核企业入驻资质
- **企业列表** - 查看所有企业账号
- **企业禁用/启用** - 企业账号管理

#### 3. 职位管理
- **职位审核** - 审核企业发布的职位
- **职位下架** - 违规职位强制下架

#### 4. 数据统计
- **用户统计** - 用户数量增长趋势
- **职位统计** - 各行业职位分布
- **投递统计** - 投递成功率分析

---

## 数据库表结构

### 核心表

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| enterprise | 企业表 |
| job | 职位表 |
| resume | 简历表 |
| application | 投递记录表 |
| interview | 面试安排表 |
| notification | 通知消息表 |
| favorite | 收藏表 |

---

## API接口

启动后端后访问Swagger文档查看完整API：
http://localhost:8080/swagger-ui.html

---

## 快速启动

### 方式一：一键启动（推荐）

Windows 用户直接双击运行 `start.bat`，按菜单选择启动服务。

### 方式二：手动启动

**后端：**
```bash
mvn clean compile
mvn spring-boot:run
```

**前端：**
```bash
cd frontend
npm install
npm run dev:all   # 同时启动三端
# 或分别启动
npm run dev:user    # 求职者端 :3000
npm run dev:company # 企业端 :3001
npm run dev:admin   # 管理端 :3002
```

### 数据库配置

首次运行需配置数据库：
1. 创建数据库 `smart_career`
2. 在 `application.yml` 中配置数据库连接，或设置环境变量：
   ```bash
   set DB_USERNAME=root
   set DB_PASSWORD=your_password
   ```
3. JPA 会自动创建表结构（`ddl-auto: update`）

详见 `QUICK_START.md`

## 测试账号

> 注意：以下为开发环境默认账号，生产环境请修改密码。

| 端 | 账号 | 密码 |
|---|------|------|
| 求职者端 | 13900000001 | 123456 |
| 企业端 | 13800008001 | 123456 |
| 管理端 | admin | admin123 |

## 环境要求

- JDK 17
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+
- npm 8+

## 部署说明

### 本地开发

1. 克隆仓库
2. 配置数据库（设置环境变量 `DB_USERNAME` 和 `DB_PASSWORD`）
3. 启动后端：`mvn spring-boot:run`
4. 启动前端：`cd frontend && npm install && npm run dev:all`

### 生产部署

1. 构建后端：`mvn clean package -DskipTests`
2. 构建前端：`cd frontend && npm run build:all`
3. 配置生产环境变量
4. 运行：`java -jar target/smart-career-system-1.0.0.jar`

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

- 项目地址: https://github.com/your-username/smart-career-system
- 问题反馈: 提交 Issue
