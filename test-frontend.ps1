<#
.SYNOPSIS
    智能职业规划系统 - 三端功能测试脚本
.DESCRIPTION
    测试用户端、企业端、管理端的功能和配置
#>

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  智能职业规划系统 - 三端功能测试" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 测试结果统计
$passed = 0
$failed = 0

# 测试函数
function Test-Endpoint {
    param($url, $expectedTitle, $expectedScript)
    
    try {
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
        $content = $response.Content
        
        $titleMatch = $content -match $expectedTitle
        $scriptMatch = $content -match $expectedScript
        
        if ($titleMatch -and $scriptMatch) {
            Write-Host "  [PASS] $url" -ForegroundColor Green
            Write-Host "    - 标题: $expectedTitle" -ForegroundColor Gray
            Write-Host "    - 脚本: $expectedScript" -ForegroundColor Gray
            $script:passed++
            return $true
        } else {
            Write-Host "  [FAIL] $url" -ForegroundColor Red
            if (-not $titleMatch) { Write-Host "    - 标题不匹配" -ForegroundColor Red }
            if (-not $scriptMatch) { Write-Host "    - 脚本不匹配" -ForegroundColor Red }
            $script:failed++
            return $false
        }
    } catch {
        Write-Host "  [ERROR] $url - $($_.Exception.Message)" -ForegroundColor Red
        $script:failed++
        return $false
    }
}

# 1. 测试端口监听
Write-Host "[1] 测试端口监听状态..." -ForegroundColor Yellow
Write-Host ""

$ports = @(3000, 3001, 3002)
foreach ($port in $ports) {
    $listening = netstat -ano | findstr ":$port" | findstr "LISTENING"
    if ($listening) {
        Write-Host "  [PASS] 端口 $port 正在监听" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  [FAIL] 端口 $port 未监听" -ForegroundColor Red
        $failed++
    }
}
Write-Host ""

# 2. 测试用户端
Write-Host "[2] 测试用户端 (http://localhost:3000)..." -ForegroundColor Yellow
Write-Host ""
Test-Endpoint -url "http://localhost:3000" -expectedTitle "求职者平台" -expectedScript "main-user.js"
Write-Host ""

# 3. 测试企业端
Write-Host "[3] 测试企业端 (http://localhost:3001)..." -ForegroundColor Yellow
Write-Host ""
Test-Endpoint -url "http://localhost:3001" -expectedTitle "企业招聘平台" -expectedScript "main-company.js"
Write-Host ""

# 4. 测试管理端
Write-Host "[4] 测试管理端 (http://localhost:3002)..." -ForegroundColor Yellow
Write-Host ""
Test-Endpoint -url "http://localhost:3002" -expectedTitle "系统管理后台" -expectedScript "main-admin.js"
Write-Host ""

# 5. 测试路由配置文件
Write-Host "[5] 检查路由配置文件..." -ForegroundColor Yellow
Write-Host ""

$routes = @(
    @{File="frontend\src\main-user.js"; Name="用户端"},
    @{File="frontend\src\main-company.js"; Name="企业端"},
    @{File="frontend\src\main-admin.js"; Name="管理端"}
)

foreach ($route in $routes) {
    if (Test-Path $route.File) {
        Write-Host "  [PASS] $($route.Name)路由配置存在" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  [FAIL] $($route.Name)路由配置缺失" -ForegroundColor Red
        $failed++
    }
}
Write-Host ""

# 6. 测试视图组件
Write-Host "[6] 检查视图组件..." -ForegroundColor Yellow
Write-Host ""

$views = @{
    "用户端" = @(
        "frontend\src\views\Home.vue",
        "frontend\src\views\Login.vue",
        "frontend\src\views\Register.vue",
        "frontend\src\views\Dashboard.vue",
        "frontend\src\views\JobList.vue",
        "frontend\src\views\Resume.vue"
    )
    "企业端" = @(
        "frontend\src\views\company\CompanyLogin.vue",
        "frontend\src\views\company\CompanyRegister.vue",
        "frontend\src\views\company\CompanyDashboard.vue",
        "frontend\src\views\company\CompanyJobs.vue"
    )
    "管理端" = @(
        "frontend\src\views\admin\AdminLogin.vue",
        "frontend\src\views\admin\AdminDashboard.vue",
        "frontend\src\views\admin\AdminUsers.vue"
    )
}

foreach ($group in $views.Keys) {
    Write-Host "  [$group]" -ForegroundColor Cyan
    foreach ($view in $views[$group]) {
        if (Test-Path $view) {
            Write-Host "    [PASS] $view" -ForegroundColor Green
            $passed++
        } else {
            Write-Host "    [FAIL] $view" -ForegroundColor Red
            $failed++
        }
    }
}
Write-Host ""

# 7. 测试布局组件
Write-Host "[7] 检查布局组件..." -ForegroundColor Yellow
Write-Host ""

$layouts = @{
    "用户端" = "frontend\src\components\Layout.vue"
    "企业端" = "frontend\src\components\company\CompanyLayout.vue"
    "管理端" = "frontend\src\components\admin\AdminLayout.vue"
}

foreach ($key in $layouts.Keys) {
    if (Test-Path $layouts[$key]) {
        Write-Host "  [PASS] $key 布局组件存在" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  [FAIL] $key 布局组件缺失" -ForegroundColor Red
        $failed++
    }
}
Write-Host ""

# 8. 测试API配置
Write-Host "[8] 检查API配置..." -ForegroundColor Yellow
Write-Host ""

$apiFiles = @(
    "frontend\src\api\index.js",
    "frontend\src\store\user.js"
)

foreach ($apiFile in $apiFiles) {
    if (Test-Path $apiFile) {
        Write-Host "  [PASS] $apiFile 存在" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  [FAIL] $apiFile 缺失" -ForegroundColor Red
        $failed++
    }
}
Write-Host ""

# 测试总结
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  测试总结" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  通过: $passed" -ForegroundColor Green
Write-Host "  失败: $failed" -ForegroundColor Red
Write-Host "  总计: $($passed + $failed)" -ForegroundColor Cyan
Write-Host ""

if ($failed -eq 0) {
    Write-Host "  状态: 所有测试通过 ✓" -ForegroundColor Green
} else {
    Write-Host "  状态: 存在失败的测试项 ✗" -ForegroundColor Red
}
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 返回退出码
if ($failed -gt 0) {
    exit 1
}
