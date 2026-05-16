<#
.SYNOPSIS
    智能职业规划与就业系统 - 综合功能测试脚本
.DESCRIPTION
    测试后端服务、数据库连接、API接口、前端路由等所有功能
#>

$ErrorActionPreference = "Continue"

# Database credentials - set via environment variable or use default
$DB_USER = $env:DB_USERNAME ?? "root"
$DB_PASS = $env:DB_PASSWORD ?? "your_password"
$MYSQL_BIN = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  智能职业规划与就业系统 - 功能测试" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# 测试结果统计
$script:passed = 0
$script:failed = 0
$script:warnings = 0

# 测试函数
function Test-Item {
    param($name, $testScript)
    
    try {
        $result = & $testScript
        if ($result) {
            Write-Host "  [PASS] $name" -ForegroundColor Green
            $script:passed++
            return $true
        } else {
            Write-Host "  [FAIL] $name" -ForegroundColor Red
            $script:failed++
            return $false
        }
    } catch {
        Write-Host "  [ERROR] $name - $($_.Exception.Message)" -ForegroundColor Red
        $script:failed++
        return $false
    }
}

function Test-Warning {
    param($name, $message)
    Write-Host "  [WARN] $name - $message" -ForegroundColor Yellow
    $script:warnings++
}

# ==================== 1. 环境检查 ====================
Write-Host "[1] 环境检查" -ForegroundColor Yellow
Write-Host ""

Test-Item "Java环境" {
    $java = java -version 2>&1
    $java -ne $null
}

Test-Item "Maven环境" {
    $mvn = mvn -version 2>&1
    $mvn -ne $null
}

Test-Item "MySQL服务" {
    $service = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
    $service.Status -eq "Running"
}

Test-Item "Node.js环境" {
    $node = node -version 2>&1
    $node -ne $null
}

Write-Host ""

# ==================== 2. 数据库检查 ====================
Write-Host "[2] 数据库检查" -ForegroundColor Yellow
Write-Host ""

Test-Item "数据库连接" {
    $result = & $MYSQL_BIN -u$DB_USER -p$DB_PASS -e "SELECT 1" 2>&1
    $result -match "1"
}

Test-Item "数据库存在" {
    $result = & $MYSQL_BIN -u$DB_USER -p$DB_PASS -e "SHOW DATABASES LIKE 'smart_career'" 2>&1
    $result -match "smart_career"
}

Test-Item "表结构完整" {
    $result = & $MYSQL_BIN -u$DB_USER -p$DB_PASS -e "USE smart_career; SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='smart_career'" 2>&1
    $result -match "\d+" -and [int]($matches[0]) -ge 20
}

Write-Host ""

# ==================== 3. 后端项目检查 ====================
Write-Host "[3] 后端项目检查" -ForegroundColor Yellow
Write-Host ""

Test-Item "POM文件存在" {
    Test-Path "pom.xml"
}

Test-Item "源代码目录存在" {
    Test-Path "src\main\java"
}

Test-Item "资源目录存在" {
    Test-Path "src\main\resources"
}

Test-Item "配置文件存在" {
    Test-Path "src\main\resources\application.yml"
}

Test-Item "主类存在" {
    Test-Path "src\main\java\com\example\smartcareer\SmartCareerApplication.java"
}

$controllers = Get-ChildItem "src\main\java\com\example\smartcareer\controller" -Filter "*.java" -ErrorAction SilentlyContinue
Test-Item "Controller类 ($($controllers.Count)个)" {
    $controllers.Count -ge 10
}

$services = Get-ChildItem "src\main\java\com\example\smartcareer\service" -Filter "*.java" -ErrorAction SilentlyContinue
Test-Item "Service类 ($($services.Count)个)" {
    $services.Count -ge 5
}

$entities = Get-ChildItem "src\main\java\com\example\smartcareer\entity" -Filter "*.java" -ErrorAction SilentlyContinue
Test-Item "Entity类 ($($entities.Count)个)" {
    $entities.Count -ge 15
}

Write-Host ""

# ==================== 4. 前端项目检查 ====================
Write-Host "[4] 前端项目检查" -ForegroundColor Yellow
Write-Host ""

Test-Item "前端目录存在" {
    Test-Path "frontend"
}

Test-Item "Package.json存在" {
    Test-Path "frontend\package.json"
}

Test-Item "Node modules已安装" {
    Test-Path "frontend\node_modules"
}

Test-Item "Vite配置文件存在" {
    Test-Path "frontend\vite.config.user.js" -and 
    Test-Path "frontend\vite.config.company.js" -and 
    Test-Path "frontend\vite.config.admin.js"
}

Test-Item "HTML入口文件存在" {
    Test-Path "frontend\index-user.html" -and 
    Test-Path "frontend\index-company.html" -and 
    Test-Path "frontend\index-admin.html"
}

Test-Item "JS入口文件存在" {
    Test-Path "frontend\src\main-user.js" -and 
    Test-Path "frontend\src\main-company.js" -and 
    Test-Path "frontend\src\main-admin.js"
}

$views = Get-ChildItem "frontend\src\views" -Recurse -Filter "*.vue" -ErrorAction SilentlyContinue
Test-Item "Vue组件 ($($views.Count)个)" {
    $views.Count -ge 20
}

Write-Host ""

# ==================== 5. 端口检查 ====================
Write-Host "[5] 端口检查" -ForegroundColor Yellow
Write-Host ""

$port8080 = netstat -ano | findstr ":8080" | findstr "LISTENING"
Test-Item "后端端口8080" {
    $port8080 -ne $null
}

$port3000 = netstat -ano | findstr ":3000" | findstr "LISTENING"
Test-Item "用户端端口3000" {
    $port3000 -ne $null
}

$port3001 = netstat -ano | findstr ":3001" | findstr "LISTENING"
Test-Item "企业端端口3001" {
    $port3001 -ne $null
}

$port3002 = netstat -ano | findstr ":3002" | findstr "LISTENING"
Test-Item "管理端端口3002" {
    $port3002 -ne $null
}

Write-Host ""

# ==================== 6. 前端页面检查 ====================
Write-Host "[6] 前端页面检查" -ForegroundColor Yellow
Write-Host ""

if ($port3000) {
    Test-Item "用户端首页访问" {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:3000" -UseBasicParsing -TimeoutSec 5
            $response.Content -match "求职者平台"
        } catch {
            $false
        }
    }
} else {
    Test-Warning "用户端端口未启动" "跳过用户端页面测试"
}

if ($port3001) {
    Test-Item "企业端首页访问" {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:3001" -UseBasicParsing -TimeoutSec 5
            $response.Content -match "企业招聘平台"
        } catch {
            $false
        }
    }
} else {
    Test-Warning "企业端端口未启动" "跳过企业端页面测试"
}

if ($port3002) {
    Test-Item "管理端首页访问" {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:3002" -UseBasicParsing -TimeoutSec 5
            $response.Content -match "系统管理后台"
        } catch {
            $false
        }
    }
} else {
    Test-Warning "管理端端口未启动" "跳过管理端页面测试"
}

Write-Host ""

# ==================== 7. API接口检查 ====================
Write-Host "[7] API接口检查" -ForegroundColor Yellow
Write-Host ""

if ($port8080) {
    Test-Item "后端健康检查" {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5
            $response.StatusCode -eq 200
        } catch {
            # 健康检查端点可能未启用，尝试其他方式
            try {
                $response = Invoke-WebRequest -Uri "http://localhost:8080" -UseBasicParsing -TimeoutSec 5
                $true
            } catch {
                $false
            }
        }
    }
    
    Test-Item "Swagger文档访问" {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui.html" -UseBasicParsing -TimeoutSec 5
            $response.StatusCode -eq 200
        } catch {
            $false
        }
    }
} else {
    Test-Warning "后端服务未启动" "跳过API接口测试"
}

Write-Host ""

# ==================== 8. 功能模块检查 ====================
Write-Host "[8] 功能模块检查" -ForegroundColor Yellow
Write-Host ""

# 用户端功能
$userViews = @(
    "Home.vue",
    "Login.vue",
    "Register.vue",
    "JobList.vue",
    "JobDetail.vue",
    "Resume.vue",
    "Applications.vue",
    "Profile.vue"
)

$allUserViewsExist = $true
foreach ($view in $userViews) {
    if (-not (Test-Path "frontend\src\views\$view")) {
        $allUserViewsExist = $false
        break
    }
}
Test-Item "用户端功能模块" {
    $allUserViewsExist
}

# 企业端功能
$companyViews = @(
    "company\CompanyLogin.vue",
    "company\CompanyRegister.vue",
    "company\CompanyDashboard.vue",
    "company\CompanyJobs.vue",
    "company\CompanyApplications.vue"
)

$allCompanyViewsExist = $true
foreach ($view in $companyViews) {
    if (-not (Test-Path "frontend\src\views\$view")) {
        $allCompanyViewsExist = $false
        break
    }
}
Test-Item "企业端功能模块" {
    $allCompanyViewsExist
}

# 管理端功能
$adminViews = @(
    "admin\AdminLogin.vue",
    "admin\AdminDashboard.vue",
    "admin\AdminUsers.vue",
    "admin\AdminPermissions.vue",
    "admin\AdminAudits.vue"
)

$allAdminViewsExist = $true
foreach ($view in $adminViews) {
    if (-not (Test-Path "frontend\src\views\$view")) {
        $allAdminViewsExist = $false
        break
    }
}
Test-Item "管理端功能模块" {
    $allAdminViewsExist
}

Write-Host ""

# ==================== 9. 数据库表检查 ====================
Write-Host "[9] 数据库表详细检查" -ForegroundColor Yellow
Write-Host ""

$tables = @(
    "users", "job_seeker", "enterprise_hr", "system_admin",
    "enterprise", "enterprise_audit", "job", "application",
    "interview", "assessment", "notification", "role",
    "permission", "system_log"
)

foreach ($table in $tables) {
    Test-Item "表 $table" {
        $result = & $MYSQL_BIN -u$DB_USER -p$DB_PASS -e "USE smart_career; SHOW TABLES LIKE '$table'" 2>&1
        $result -match $table
    }
}

Write-Host ""

# ==================== 10. 测试总结 ====================
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  测试总结" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  通过: $script:passed" -ForegroundColor Green
Write-Host "  失败: $script:failed" -ForegroundColor Red
Write-Host "  警告: $script:warnings" -ForegroundColor Yellow
Write-Host "  总计: $($script:passed + $script:failed)" -ForegroundColor Cyan
Write-Host ""

if ($script:failed -eq 0) {
    Write-Host "  状态: 所有测试通过 ✓" -ForegroundColor Green
} else {
    Write-Host "  状态: 存在失败的测试项 ✗" -ForegroundColor Red
}
Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# 返回退出码
if ($script:failed -gt 0) {
    exit 1
}
