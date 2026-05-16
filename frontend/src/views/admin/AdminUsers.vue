<template>
  <div>
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">用户管理</h1>
      <p class="text-slate-500 mt-2">管理系统中的所有用户账户和权限</p>
    </div>
    
    <!-- Filters & Actions -->
    <div class="bg-white rounded-2xl p-6 shadow-sm border border-slate-200 mb-6">
      <div class="flex flex-col lg:flex-row gap-4 items-center justify-between">
        <div class="flex flex-wrap gap-3">
          <div class="relative">
            <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">search</span>
            <input 
              v-model="searchKeyword"
              type="text"
              placeholder="搜索用户..."
              class="pl-10 pr-4 py-2 bg-slate-50 border border-slate-200 rounded-lg w-64 focus:ring-2 focus:ring-violet-500 focus:border-transparent transition-all"
            />
          </div>
          
          <select 
            v-model="userType"
            class="px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-violet-500 focus:border-transparent"
            @change="fetchUsers"
          >
            <option value="">全部类型</option>
            <option value="JOB_SEEKER">求职者</option>
            <option value="ENTERPRISE_HR">企业HR</option>
            <option value="SYSTEM_ADMIN">管理员</option>
          </select>
          
          <select 
            v-model="statusFilter"
            class="px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-violet-500 focus:border-transparent"
            @change="fetchUsers"
          >
            <option value="">全部状态</option>
            <option value="1">正常</option>
            <option value="0">禁用</option>
          </select>
        </div>
        
        <div class="flex gap-3">
        </div>
      </div>
    </div>
    
    <!-- Users Table -->
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead class="bg-slate-50 border-b border-slate-200">
            <tr>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">用户信息</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">类型</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">状态</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">注册时间</th>
              <th class="px-6 py-4 text-right text-xs font-semibold text-slate-500 uppercase tracking-wider">操作</th>
            </tr>
          </thead>
          
          <tbody class="divide-y divide-slate-200">
            <tr 
              v-for="user in users" 
              :key="user.id"
              class="hover:bg-slate-50 transition-colors"
            >
              <td class="px-6 py-4">
                <div class="flex items-center gap-3">
                  <div class="size-10 rounded-full bg-gradient-to-br from-violet-500 to-purple-600 flex items-center justify-center text-white font-bold text-sm">
                    {{ user.username?.charAt(0)?.toUpperCase() }}
                  </div>
                  <div>
                    <p class="text-sm font-semibold text-slate-900">{{ user.username }}</p>
                    <p class="text-xs text-slate-500">{{ user.email || user.phone || '未填写' }}</p>
                    <p v-if="user.userType === 'ENTERPRISE_HR' && user.enterpriseName" class="text-xs text-emerald-600 mt-0.5">{{ user.enterpriseName }}</p>
                  </div>
                </div>
              </td>
              
              <td class="px-6 py-4">
                <span :class="[
                  'px-3 py-1 text-xs font-semibold rounded-full',
                  user.userType === 'JOB_SEEKER' ? 'bg-blue-100 text-blue-700' :
                  user.userType === 'ENTERPRISE_HR' ? 'bg-emerald-100 text-emerald-700' :
                  'bg-violet-100 text-violet-700'
                ]">
                  {{ getTypeLabel(user.userType) }}
                </span>
              </td>
              
<td class="px-6 py-4">
      <span :class="[
        'px-3 py-1 text-xs font-semibold rounded-full',
        (user.status === 'ACTIVE' || user.status === 1) ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700'
      ]">
        {{ (user.status === 'ACTIVE' || user.status === 1) ? '正常' : '禁用' }}
      </span>
    </td>
              
              <td class="px-6 py-4 text-sm text-slate-600">
                {{ formatDate(user.createdTime) }}
              </td>
              
              <td class="px-6 py-4">
                <div class="flex items-center justify-end gap-2">
                  <button 
                    @click="viewUser(user)"
                    class="p-2 text-slate-400 hover:text-violet-600 hover:bg-violet-50 rounded-lg transition-colors"
                    title="查看详情"
                  >
                    <span class="material-symbols-outlined text-lg">visibility</span>
                  </button>
                  
                  <button 
                    @click="editUser(user)"
                    class="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                    title="编辑"
                  >
                    <span class="material-symbols-outlined text-lg">edit</span>
                  </button>

                  <button 
                    @click="openWarnModal(user)"
                    v-if="user.userType !== 'SYSTEM_ADMIN'"
                    class="p-2 text-slate-400 hover:text-amber-600 hover:bg-amber-50 rounded-lg transition-colors"
                    title="警告"
                  >
                    <span class="material-symbols-outlined text-lg">warning</span>
                  </button>
                  
                  <button 
                    @click="toggleUserStatus(user)"
                    v-if="user.userType !== 'SYSTEM_ADMIN'"
                    :class="[
                      'p-2 rounded-lg transition-colors',
                      (user.status === 'ACTIVE' || user.status === 1)
                        ? 'text-slate-400 hover:text-amber-600 hover:bg-amber-50'
                        : 'text-slate-400 hover:text-emerald-600 hover:bg-emerald-50'
                    ]"
                    :title="(user.status === 'ACTIVE' || user.status === 1) ? '禁用' : '启用'"
                  >
                    <span class="material-symbols-outlined text-lg">{{ (user.status === 'ACTIVE' || user.status === 1) ? 'block' : 'check_circle' }}</span>
                  </button>
                  
                  <button 
                    @click="deleteUser(user)"
                    v-if="user.userType !== 'SYSTEM_ADMIN'"
                    class="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                    title="删除"
                  >
                    <span class="material-symbols-outlined text-lg">delete</span>
                  </button>
                  
                  <span v-if="user.userType === 'SYSTEM_ADMIN'" class="text-xs text-slate-400 italic ml-2">
                    管理员账号
                  </span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- Pagination -->
      <div class="flex items-center justify-between px-6 py-4 border-t border-slate-200">
        <p class="text-sm text-slate-500">
          显示 {{ users.length }} 条，共 {{ total }} 条
        </p>
        
        <div class="flex items-center gap-2">
          <button 
            class="px-3 py-1 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors disabled:opacity-50"
            :disabled="currentPage === 1"
            @click="currentPage--"
          >
            上一页
          </button>
          
          <button 
            v-for="page in displayPages" 
            :key="page"
            :class="[
              'px-3 py-1 text-sm rounded-lg transition-colors',
              currentPage === page ? 'bg-violet-600 text-white' : 'text-slate-600 hover:bg-slate-100'
            ]"
            @click="currentPage = page"
          >
            {{ page }}
          </button>
          
          <button 
            class="px-3 py-1 text-sm text-slate-600 hover:bg-slate-100 rounded-lg transition-colors disabled:opacity-50"
            :disabled="currentPage === totalPages"
            @click="currentPage++"
          >
            下一页
          </button>
        </div>
      </div>
    </div>

    <!-- User Detail Modal -->
    <div v-if="showDetailModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl w-full max-w-lg max-h-[90vh] overflow-y-auto">
        <div class="p-6 border-b border-slate-200">
          <div class="flex items-center justify-between">
            <h3 class="text-xl font-bold text-slate-900">用户详情</h3>
            <button @click="showDetailModal = false" class="text-slate-400 hover:text-slate-600">
              <span class="material-symbols-outlined">close</span>
            </button>
          </div>
        </div>
        <div class="p-6 space-y-4">
          <div class="flex items-center gap-4 mb-6">
            <div class="size-16 rounded-full bg-gradient-to-br from-violet-500 to-purple-600 flex items-center justify-center text-white font-bold text-xl">
              {{ selectedUser?.username?.charAt(0)?.toUpperCase() }}
            </div>
            <div>
              <h4 class="text-lg font-bold text-slate-900">{{ selectedUser?.username }}</h4>
              <p class="text-sm text-slate-500">{{ getTypeLabel(selectedUser?.userType) }}</p>
            </div>
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div>
              <p class="text-xs text-slate-500 mb-1">用户ID</p>
              <p class="text-sm font-medium text-slate-900">{{ selectedUser?.id }}</p>
            </div>
            <div>
              <p class="text-xs text-slate-500 mb-1">状态</p>
              <span :class="['px-2 py-1 text-xs font-semibold rounded-full', (selectedUser?.status === 'ACTIVE' || selectedUser?.status === 1) ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700']">
                {{ (selectedUser?.status === 'ACTIVE' || selectedUser?.status === 1) ? '正常' : '禁用' }}
              </span>
            </div>
            <div v-if="selectedUser?.userType === 'ENTERPRISE_HR'">
              <p class="text-xs text-slate-500 mb-1">所属企业</p>
              <p class="text-sm font-medium text-slate-900">{{ selectedUser?.enterpriseName || '未关联' }}</p>
            </div>
            <div>
              <p class="text-xs text-slate-500 mb-1">邮箱</p>
              <p class="text-sm font-medium text-slate-900">{{ selectedUser?.email || '未填写' }}</p>
            </div>
            <div>
              <p class="text-xs text-slate-500 mb-1">手机号</p>
              <p class="text-sm font-medium text-slate-900">{{ selectedUser?.phone || '未填写' }}</p>
            </div>
            <div>
              <p class="text-xs text-slate-500 mb-1">注册时间</p>
              <p class="text-sm font-medium text-slate-900">{{ formatDate(selectedUser?.createdTime) }}</p>
            </div>
            <div>
              <p class="text-xs text-slate-500 mb-1">最后登录</p>
              <p class="text-sm font-medium text-slate-900">{{ formatDate(selectedUser?.lastLoginTime) || '从未登录' }}</p>
            </div>
          </div>

          <div v-if="selectedUser?.warningCount > 0" class="p-3 bg-amber-50 border border-amber-200 rounded-lg">
            <p class="text-xs text-amber-600 font-medium">警告次数: {{ selectedUser.warningCount }}次</p>
            <p class="text-xs text-amber-500 mt-1">{{ selectedUser.lastWarningReason }}</p>
          </div>
        </div>
        <div class="p-6 border-t border-slate-200 flex justify-end gap-3">
          <button @click="showDetailModal = false" class="px-4 py-2 bg-slate-100 text-slate-700 rounded-lg hover:bg-slate-200">关闭</button>
          <button @click="openEditModal" v-if="selectedUser?.userType !== 'SYSTEM_ADMIN'" class="px-4 py-2 bg-violet-600 text-white rounded-lg hover:bg-violet-700">编辑</button>
        </div>
      </div>
    </div>

    <!-- Edit User Modal -->
    <div v-if="showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl w-full max-w-md">
        <div class="p-6 border-b border-slate-200">
          <h3 class="text-xl font-bold text-slate-900">编辑用户</h3>
        </div>
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">用户名</label>
            <input v-model="editForm.username" type="text" class="w-full px-4 py-2 border border-slate-200 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">邮箱</label>
            <input v-model="editForm.email" type="email" class="w-full px-4 py-2 border border-slate-200 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">手机号</label>
            <input v-model="editForm.phone" type="text" class="w-full px-4 py-2 border border-slate-200 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">用户类型</label>
            <select v-model="editForm.userType" class="w-full px-4 py-2 border border-slate-200 rounded-lg">
              <option value="JOB_SEEKER">求职者</option>
              <option value="ENTERPRISE_HR">企业HR</option>
            </select>
          </div>
        </div>
        <div class="p-6 border-t border-slate-200 flex gap-3">
          <button @click="showEditModal = false" class="flex-1 px-4 py-2 bg-slate-100 text-slate-700 rounded-lg hover:bg-slate-200">取消</button>
          <button @click="saveUser" class="flex-1 px-4 py-2 bg-violet-600 text-white rounded-lg hover:bg-violet-700">保存</button>
        </div>
      </div>
    </div>

    <!-- Warn User Modal -->
    <div v-if="showWarnModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-2xl w-full max-w-md">
        <div class="p-6 border-b border-slate-200">
          <h3 class="text-xl font-bold text-slate-900">发送警告</h3>
        </div>
        <div class="p-6">
          <label class="block text-sm font-medium text-slate-700 mb-2">警告原因</label>
          <textarea v-model="warnForm.reason" class="w-full px-4 py-3 border border-slate-200 rounded-lg" rows="4" placeholder="请输入警告原因..."></textarea>
        </div>
        <div class="p-6 border-t border-slate-200 flex gap-3">
          <button @click="showWarnModal = false" class="flex-1 px-4 py-2 bg-slate-100 text-slate-700 rounded-lg hover:bg-slate-200">取消</button>
          <button @click="submitWarn" class="flex-1 px-4 py-2 bg-amber-500 text-white rounded-lg hover:bg-amber-600">发送警告</button>
        </div>
      </div>
    </div>

    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import api from '@/api'

const searchKeyword = ref('')
const userType = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const totalPages = ref(5)
const total = ref(0)
const users = ref([])

const showDetailModal = ref(false)
const showEditModal = ref(false)
const showWarnModal = ref(false)
const selectedUser = ref(null)
const editForm = ref({ username: '', email: '', phone: '', userType: '' })
const warnForm = ref({ reason: '' })

const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const matchKeyword = !searchKeyword.value || 
      user.username?.includes(searchKeyword.value) ||
      user.email?.includes(searchKeyword.value) ||
      user.phone?.includes(searchKeyword.value)
    
    const matchStatus = !statusFilter.value || 
        (statusFilter.value === '1' && (user.status === 'ACTIVE' || user.status === 1)) ||
        (statusFilter.value === '0' && (user.status === 'DISABLED' || user.status === 0))
    
    return matchKeyword && matchStatus
  })
})

const displayPages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, start + 4)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const getTypeLabel = (type) => {
  const labels = {
    'JOB_SEEKER': '求职者',
    'ENTERPRISE_HR': '企业HR',
    'SYSTEM_ADMIN': '管理员'
  }
  return labels[type] || type
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN')
}

const fetchUsers = async () => {
  try {
    const response = await api.getAdminUsers({
      page: currentPage.value - 1,
      size: 10,
      userType: userType.value,
      keyword: searchKeyword.value
    })
    const data = response?.data?.content || response?.content || response || []
    if (data) {
      users.value = data
      total.value = response?.data?.totalElements || response?.totalElements || users.value.length
      totalPages.value = response?.data?.totalPages || response?.totalPages || 1
    }
  } catch (error) {
    console.error('获取用户列表失败', error)
    users.value = []
  }
}

const viewUser = async (user) => {
  try {
    const response = await api.getAdminUserDetail(user.id)
    if (response) {
      selectedUser.value = response.user || response
    } else {
      selectedUser.value = user
    }
  } catch (error) {
    selectedUser.value = user
  }
  showDetailModal.value = true
}

const editUser = (user) => {
  selectedUser.value = user
  editForm.value = {
    username: user.username,
    email: user.email || '',
    phone: user.phone || '',
    userType: user.userType
  }
  showDetailModal.value = false
  showEditModal.value = true
}

const openEditModal = () => {
  showDetailModal.value = false
  showEditModal.value = true
}

const saveUser = async () => {
  try {
    await api.updateAdminUser(selectedUser.value.id, editForm.value)
    alert('用户信息已更新')
    showEditModal.value = false
    fetchUsers()
  } catch (error) {
    console.error('更新失败', error)
    alert('更新失败')
  }
}

const openWarnModal = (user) => {
  selectedUser.value = user
  warnForm.value.reason = ''
  showWarnModal.value = true
}

const submitWarn = async () => {
  if (!warnForm.value.reason.trim()) {
    alert('请输入警告原因')
    return
  }
  try {
    await api.warnUser(selectedUser.value.id, warnForm.value.reason)
    alert('警告已发送')
    showWarnModal.value = false
    fetchUsers()
  } catch (error) {
    console.error('发送警告失败', error)
    alert('发送失败')
  }
}

const toggleUserStatus = async (user) => {
  const isActive = user.status === 'ACTIVE' || user.status === 1
  const action = isActive ? '禁用' : '启用'
  if (!confirm(`确定要${action}用户 ${user.username} 吗？`)) return
  
  try {
    if (isActive) {
      await api.disableUser(user.id)
    } else {
      await api.enableUser(user.id)
    }
    user.status = isActive ? 'DISABLED' : 'ACTIVE'
    alert(`用户已${action}`)
  } catch (error) {
    console.error('操作失败', error)
    alert('操作失败')
  }
}

const deleteUser = async (user) => {
  if (confirm(`确定要删除用户 ${user.username} 吗？此操作不可恢复！`)) {
    try {
      await api.deleteAdminUser(user.id)
      alert('用户已删除')
      await fetchUsers()
    } catch (error) {
      console.error('删除失败', error)
      alert('删除失败')
    }
  }
}

watch(currentPage, () => {
  fetchUsers()
})

watch(searchKeyword, () => {
  currentPage.value = 1
  fetchUsers()
})

watch(userType, () => {
  currentPage.value = 1
  fetchUsers()
})

watch(statusFilter, () => {
  currentPage.value = 1
  fetchUsers()
})

onMounted(() => {
  fetchUsers()
})
</script>
