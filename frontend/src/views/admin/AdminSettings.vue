<template>
  <div>
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
      <div>
        <h1 class="text-3xl font-black text-slate-900 dark:text-white">系统设置</h1>
        <p class="text-slate-500 dark:text-slate-400">管理系统配置参数</p>
      </div>
    </div>
    
    <!-- Settings List -->
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden mb-6">
      <div class="p-4 border-b border-slate-200 dark:border-slate-800">
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
            @click="activeCategory = cat.value; fetchSettings()"
          >
            {{ cat.label }}
          </button>
        </div>
      </div>
      
      <div class="divide-y divide-slate-100 dark:divide-slate-800">
        <div v-for="setting in settings" :key="setting.id" class="p-4 flex items-center justify-between hover:bg-slate-50 dark:hover:bg-slate-800">
          <div class="flex-1">
            <p class="font-bold text-slate-900 dark:text-white">{{ setting.settingKey }}</p>
            <p class="text-sm text-slate-500">{{ setting.description }}</p>
          </div>
          <div class="flex items-center gap-4">
            <input 
              v-if="editingId === setting.id"
              v-model="editingValue"
              class="px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm w-48"
            />
            <span v-else class="text-sm text-slate-600 dark:text-slate-400 font-mono bg-slate-100 dark:bg-slate-800 px-3 py-1 rounded">
              {{ setting.settingValue }}
            </span>
            
            <template v-if="editingId === setting.id">
              <button @click="saveSetting(setting.id)" class="text-green-500 hover:text-green-600">
                <span class="material-symbols-outlined">check</span>
              </button>
              <button @click="editingId = null" class="text-red-500 hover:text-red-600">
                <span class="material-symbols-outlined">close</span>
              </button>
            </template>
            <template v-else>
              <button @click="startEdit(setting)" class="text-slate-400 hover:text-primary">
                <span class="material-symbols-outlined">edit</span>
              </button>
            </template>
          </div>
        </div>
      </div>
      
      <!-- Empty State -->
      <div v-if="settings.length === 0" class="text-center py-12">
        <span class="material-symbols-outlined text-5xl text-slate-300 dark:text-slate-600 mb-4">settings</span>
        <p class="text-slate-500 dark:text-slate-400">暂无系统设置</p>
      </div>
    </div>
    
    <!-- Change Logs -->
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6">
      <h3 class="text-lg font-bold mb-4">设置变更历史</h3>
      <div class="space-y-3">
        <div v-for="log in changeLogs" :key="log.id" class="flex items-center justify-between p-3 bg-slate-50 dark:bg-slate-800 rounded-lg">
          <div>
            <p class="text-sm font-medium text-slate-900 dark:text-white">{{ log.settingKey }}</p>
            <p class="text-xs text-slate-500">
              {{ log.oldValue }} → {{ log.newValue }}
            </p>
          </div>
          <div class="text-right">
            <p class="text-xs text-slate-500">{{ log.operatorName }}</p>
            <p class="text-xs text-slate-400">{{ formatTime(log.changeTime) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const settings = ref([])
const changeLogs = ref([])
const activeCategory = ref('SYSTEM')
const editingId = ref(null)
const editingValue = ref('')

const categories = [
  { label: '系统', value: 'SYSTEM' },
  { label: '业务', value: 'BUSINESS' },
  { label: '安全', value: 'SECURITY' },
  { label: '其他', value: 'OTHER' }
]

const fetchSettings = async () => {
  try {
    const response = await api.getAdminSettingsByCategory(activeCategory.value, { size: 50 })
    const data = response?.data || response?.data?.content || response?.content || []
    if (data) {
      settings.value = Array.isArray(data) ? data : []
    }
  } catch (error) {
    console.error('获取设置失败', error)
  }
}

const fetchChangeLogs = async () => {
  try {
    const response = await api.getSettingChangeLogs({ size: 10 })
    if (response && response.content) {
      changeLogs.value = response.content
    } else if (Array.isArray(response)) {
      changeLogs.value = response
    }
  } catch (error) {
    console.error('获取变更日志失败', error)
  }
}

const startEdit = (setting) => {
  editingId.value = setting.id
  editingValue.value = setting.settingValue
}

const saveSetting = async (settingId) => {
  try {
    await api.updateAdminSetting(settingId, {
      value: editingValue.value,
      reason: '管理员修改'
    })
    alert('设置已保存')
    editingId.value = null
    fetchSettings()
    fetchChangeLogs()
  } catch (error) {
    alert('保存失败')
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString()
}

onMounted(() => {
  fetchSettings()
  fetchChangeLogs()
})
</script>