<template>
  <div>
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
      <div>
        <h1 class="text-3xl font-black text-slate-900 dark:text-white">权限管理</h1>
        <p class="text-slate-500 dark:text-slate-400">配置各角色用户的操作权限</p>
      </div>
    </div>

    <!-- 用户类型分类 -->
    <div class="mb-6">
      <div class="flex gap-2">
        <button
          v-for="cat in categories"
          :key="cat.value"
          :class="[
            'px-4 py-2 rounded-lg font-medium text-sm transition-colors',
            activeCategory === cat.value
              ? 'bg-primary text-white'
              : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-slate-200 dark:hover:bg-slate-700'
          ]"
          @click="activeCategory = cat.value; selectedRole = cat.defaultRole"
        >
          {{ cat.label }}
        </button>
      </div>
    </div>

    <!-- Role Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div v-for="role in currentRoles" :key="role.id"
        class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 hover:border-primary/50 transition-colors cursor-pointer"
        :class="{ 'border-primary ring-2 ring-primary/20': selectedRole === role.id }"
        @click="selectedRole = role.id; fetchPermissions()">
        <div class="flex items-center gap-4 mb-4">
          <div class="w-12 h-12 rounded-xl flex items-center justify-center" :style="{ backgroundColor: role.color + '20' }">
            <span class="material-symbols-outlined text-2xl" :style="{ color: role.color }">{{ role.icon }}</span>
          </div>
          <div>
            <h3 class="font-bold text-slate-900 dark:text-white">{{ role.name }}</h3>
            <p class="text-sm text-slate-500">{{ role.userCount }} 用户</p>
          </div>
        </div>
        <p class="text-sm text-slate-600 dark:text-slate-400">{{ role.description }}</p>
      </div>
    </div>
    
    <!-- Permission Settings -->
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">
      <div class="p-4 border-b border-slate-200 dark:border-slate-800">
        <h3 class="font-bold text-slate-900 dark:text-white">权限配置</h3>
      </div>
      
      <div class="divide-y divide-slate-100 dark:divide-slate-800">
        <div v-for="module in filteredModules" :key="module.id" class="p-4">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center gap-3">
              <span class="material-symbols-outlined text-primary">{{ module.icon }}</span>
              <span class="font-bold text-slate-900 dark:text-white">{{ module.name }}</span>
            </div>
            <label class="flex items-center gap-2">
              <input type="checkbox" v-model="module.enabled" class="rounded border-slate-300 text-primary focus:ring-primary" />
              <span class="text-sm text-slate-600">全部权限</span>
            </label>
          </div>
          
          <div class="grid grid-cols-2 md:grid-cols-4 gap-3 ml-9">
            <label v-for="permission in module.permissions" :key="permission.id" class="flex items-center gap-2 text-sm">
              <input type="checkbox" v-model="permission.enabled" class="rounded border-slate-300 text-primary focus:ring-primary" />
              <span class="text-slate-600 dark:text-slate-400">{{ permission.name }}</span>
            </label>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Save Button -->
    <div class="mt-6 flex justify-end gap-3">
      <button @click="resetPermissions" class="px-6 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-lg font-bold text-sm">
        重置
      </button>
      <button @click="savePermissions" class="px-6 py-2 bg-primary text-white rounded-lg font-bold text-sm hover:bg-primary/90">
        保存设置
      </button>
    </div>
    
    <!-- Recent Changes -->
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 mt-6">
      <h3 class="font-bold text-slate-900 dark:text-white mb-4">最近权限变更</h3>
      <div class="space-y-3">
        <div v-for="change in recentChanges" :key="change.id" class="flex items-center justify-between p-3 bg-slate-50 dark:bg-slate-800 rounded-lg">
          <div class="flex items-center gap-3">
            <span class="material-symbols-outlined text-primary">edit</span>
            <div>
              <p class="text-sm font-medium text-slate-900 dark:text-white">{{ change.action }}</p>
              <p class="text-xs text-slate-500">{{ change.role }} - {{ change.module }}</p>
            </div>
          </div>
          <div class="text-right">
            <p class="text-xs text-slate-500">{{ change.time }}</p>
            <p class="text-xs text-slate-400">{{ change.operator }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import api from '@/api'

const activeCategory = ref('SEEKER')
const selectedRole = ref('JOB_SEEKER')
const roles = ref([])
const permissionModules = ref([])
const recentChanges = ref([])
const loading = ref(false)

const categories = [
  { label: '求职者端', value: 'SEEKER', defaultRole: 'JOB_SEEKER' },
  { label: '企业端', value: 'ENTERPRISE', defaultRole: 'ENTERPRISE_HR' }
]

const roleByCategory = {
  'SEEKER': [
    { id: 'JOB_SEEKER', name: '求职者', icon: 'person', color: '#137fec', description: '求职用户，可浏览职位、投递简历、管理个人信息' }
  ],
  'ENTERPRISE': [
    { id: 'ENTERPRISE_HR', name: '企业HR', icon: 'business', color: '#10b981', description: '企业招聘人员，可发布职位、筛选简历、管理面试' }
  ]
}

const roleModules = {
  'JOB_SEEKER': [1, 2, 3],
  'ENTERPRISE_HR': [1, 2, 4]
}

const moduleNames = {
  1: '职位管理',
  2: '简历管理',
  3: '申请管理',
  4: '企业招聘',
  5: '系统管理'
}

const currentRoles = computed(() => {
  const baseRoles = roleByCategory[activeCategory.value] || []
  return baseRoles.map(baseRole => {
    const roleFromApi = roles.value.find(r => r.id === baseRole.id)
    return {
      ...baseRole,
      userCount: roleFromApi?.userCount || 0
    }
  })
})

const filteredModules = computed(() => {
  const allowedModules = roleModules[selectedRole.value] || []
  return permissionModules.value.filter(m => allowedModules.includes(m.id))
})

const roleColors = {
  'JOB_SEEKER': '#137fec',
  'ENTERPRISE_HR': '#10b981',
  'ENTERPRISE_ADMIN': '#059669'
}

const roleIcons = {
  'JOB_SEEKER': 'person',
  'ENTERPRISE_HR': 'business',
  'ENTERPRISE_ADMIN': 'admin_panel_settings'
}

const fetchRoles = async () => {
  try {
    const response = await api.getAllRoles()
    if (response && response.content) {
      roles.value = response.content.map(role => ({
        id: role.roleCode,
        name: role.roleName,
        icon: roleIcons[role.roleCode] || 'person',
        color: roleColors[role.roleCode] || '#6b7280',
        userCount: role.userCount || 0,
        description: role.description || ''
      }))
    }
  } catch (error) {
    console.error('获取角色失败', error)
  }
}

const fetchPermissions = async () => {
  try {
    const response = await api.$get(`/admin/permission/role/${selectedRole.value}/permissions`)
    if (response && response.permissions) {
      permissionModules.value = response.permissions
    }
  } catch (error) {
    console.error('获取权限失败', error)
  }
}

const fetchRecentChanges = async () => {
  try {
    const response = await api.$get('/admin/logs', { params: { operationType: 'PERMISSION', size: 10 } })
    if (response && response.content) {
      recentChanges.value = response.content.map(log => ({
        id: log.id,
        action: log.operationDetail || '权限变更',
        role: log.username || '系统',
        module: '权限管理',
        time: log.operationTime ? new Date(log.operationTime).toLocaleString('zh-CN') : '-',
        operator: '管理员'
      }))
    }
  } catch (error) {
    recentChanges.value = []
  }
}

const savePermissions = async () => {
  loading.value = true
  try {
    const permissions = []
    permissionModules.value.forEach(module => {
      module.permissions.forEach(p => {
        if (p.enabled) {
          permissions.push(p.id)
        }
      })
    })
    await api.updatePermissions({
      roleCode: selectedRole.value,
      permissions: permissions
    })
    alert('权限保存成功')
    fetchRecentChanges()
  } catch (error) {
    alert('保存失败')
  } finally {
    loading.value = false
  }
}

const resetPermissions = () => {
  fetchPermissions()
}

watch(activeCategory, () => {
  const categoryRoles = roleByCategory[activeCategory.value]
  if (categoryRoles && categoryRoles.length > 0) {
    selectedRole.value = categoryRoles[0].id
    fetchPermissions()
  }
})

onMounted(() => {
  fetchRoles()
  fetchPermissions()
  fetchRecentChanges()
})
</script>
