<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <h1 class="text-2xl font-bold text-slate-900 dark:text-white">公告管理</h1>
      <button @click="showCreateModal = true" class="flex items-center gap-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90">
        <span class="material-symbols-outlined text-xl">add</span>
        发布公告
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div class="bg-white dark:bg-slate-800 p-4 rounded-xl border border-slate-200 dark:border-slate-700">
        <p class="text-sm text-slate-500">总公告数</p>
        <p class="text-2xl font-bold text-slate-900 dark:text-white">{{ stats.totalCount || 0 }}</p>
      </div>
      <div class="bg-white dark:bg-slate-800 p-4 rounded-xl border border-slate-200 dark:border-slate-700">
        <p class="text-sm text-slate-500">已发布</p>
        <p class="text-2xl font-bold text-green-600">{{ stats.publishedCount || 0 }}</p>
      </div>
      <div class="bg-white dark:bg-slate-800 p-4 rounded-xl border border-slate-200 dark:border-slate-700">
        <p class="text-sm text-slate-500">草稿</p>
        <p class="text-2xl font-bold text-amber-600">{{ stats.draftCount || 0 }}</p>
      </div>
      <div class="bg-white dark:bg-slate-800 p-4 rounded-xl border border-slate-200 dark:border-slate-700">
        <p class="text-sm text-slate-500">有效公告</p>
        <p class="text-2xl font-bold text-blue-600">{{ stats.activeCount || 0 }}</p>
      </div>
    </div>

    <div class="bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700">
      <div class="p-4 border-b border-slate-200 dark:border-slate-700 flex gap-4">
        <select v-model="filters.type" class="px-3 py-2 border rounded-lg dark:bg-slate-700">
          <option value="">全部类型</option>
          <option value="SYSTEM">系统公告</option>
          <option value="MAINTENANCE">维护公告</option>
          <option value="UPDATE">更新公告</option>
          <option value="PROMOTION">推广公告</option>
        </select>
        <select v-model="filters.isPublished" class="px-3 py-2 border rounded-lg dark:bg-slate-700">
          <option value="">全部状态</option>
          <option value="1">已发布</option>
          <option value="0">草稿</option>
        </select>
        <select v-model="filters.targetAudience" class="px-3 py-2 border rounded-lg dark:bg-slate-700">
          <option value="">全部受众</option>
          <option value="ALL">所有用户</option>
          <option value="JOB_SEEKER">求职者</option>
          <option value="ENTERPRISE">企业</option>
        </select>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full">
          <thead class="bg-slate-50 dark:bg-slate-900">
            <tr>
              <th class="px-4 py-3 text-left text-sm font-semibold">标题</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">类型</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">受众</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">状态</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">浏览</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">发布时间</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in announcements" :key="item.id" class="border-t border-slate-200 dark:border-slate-700">
              <td class="px-4 py-3 font-medium">{{ item.title }}</td>
              <td class="px-4 py-3">
                <span :class="getTypeClass(item.type)" class="px-2 py-1 rounded-full text-xs">{{ getTypeName(item.type) }}</span>
              </td>
              <td class="px-4 py-3">{{ getAudienceName(item.targetAudience) }}</td>
              <td class="px-4 py-3">
                <span :class="item.isPublished ? 'bg-green-100 text-green-800' : 'bg-amber-100 text-amber-800'" class="px-2 py-1 rounded-full text-xs">
                  {{ item.isPublished ? '已发布' : '草稿' }}
                </span>
              </td>
              <td class="px-4 py-3">{{ item.viewCount }}</td>
              <td class="px-4 py-3 text-sm text-slate-500">{{ formatTime(item.publishTime) }}</td>
              <td class="px-4 py-3">
                <div class="flex gap-2">
                  <button @click="viewDetail(item)" class="p-1 text-slate-600 hover:bg-slate-50 rounded" title="查看详情">
                    <span class="material-symbols-outlined text-xl">visibility</span>
                  </button>
                  <button @click="editAnnouncement(item)" class="p-1 text-blue-600 hover:bg-blue-50 rounded" title="编辑">
                    <span class="material-symbols-outlined text-xl">edit</span>
                  </button>
                  <button v-if="item.isPublished" @click="unpublish(item.id)" class="p-1 text-amber-600 hover:bg-amber-50 rounded" title="取消发布">
                    <span class="material-symbols-outlined text-xl">unpublished</span>
                  </button>
                  <button v-else @click="publish(item.id)" class="p-1 text-green-600 hover:bg-green-50 rounded" title="发布">
                    <span class="material-symbols-outlined text-xl">publish</span>
                  </button>
                  <button @click="deleteAnnouncement(item.id)" class="p-1 text-red-600 hover:bg-red-50 rounded" title="删除">
                    <span class="material-symbols-outlined text-xl">delete</span>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="p-4 border-t border-slate-200 dark:border-slate-700 flex justify-center gap-2">
        <button @click="loadPage(page - 1)" :disabled="page === 0" class="px-3 py-1 border rounded disabled:opacity-50">上一页</button>
        <span class="px-3 py-1">第 {{ page + 1 }} 页</span>
        <button @click="loadPage(page + 1)" :disabled="!hasNext" class="px-3 py-1 border rounded disabled:opacity-50">下一页</button>
      </div>
    </div>

    <div v-if="showCreateModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-slate-800 rounded-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div class="p-4 border-b border-slate-200 dark:border-slate-700 flex justify-between items-center">
          <h2 class="text-lg font-bold">{{ editingId ? '编辑公告' : '发布公告' }}</h2>
          <button @click="closeModal" class="p-1 hover:bg-slate-100 rounded">
            <span class="material-symbols-outlined">close</span>
          </button>
        </div>
        <form @submit.prevent="saveAnnouncement" class="p-4 space-y-4">
          <div>
            <label class="block text-sm font-medium mb-1">标题</label>
            <input v-model="form.title" type="text" class="w-full px-3 py-2 border rounded-lg dark:bg-slate-700" required />
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">类型</label>
            <select v-model="form.type" class="w-full px-3 py-2 border rounded-lg dark:bg-slate-700" required>
              <option value="SYSTEM">系统公告</option>
              <option value="MAINTENANCE">维护公告</option>
              <option value="UPDATE">更新公告</option>
              <option value="PROMOTION">推广公告</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">目标受众</label>
            <select v-model="form.targetAudience" class="w-full px-3 py-2 border rounded-lg dark:bg-slate-700" required>
              <option value="ALL">所有用户</option>
              <option value="JOB_SEEKER">求职者</option>
              <option value="ENTERPRISE">企业</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">优先级</label>
            <select v-model="form.priority" class="w-full px-3 py-2 border rounded-lg dark:bg-slate-700">
              <option value="HIGH">高</option>
              <option value="NORMAL">普通</option>
              <option value="LOW">低</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">过期时间（可选）</label>
            <input v-model="form.expireTime" type="datetime-local" class="w-full px-3 py-2 border rounded-lg dark:bg-slate-700" />
          </div>
          <div>
            <label class="block text-sm font-medium mb-1">内容</label>
            <textarea v-model="form.content" rows="6" class="w-full px-3 py-2 border rounded-lg dark:bg-slate-700" required></textarea>
          </div>
          <div class="flex justify-end gap-3">
            <button type="button" @click="closeModal" class="px-4 py-2 border rounded-lg">取消</button>
            <button type="submit" class="px-4 py-2 bg-primary text-white rounded-lg">保存</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="showDetailModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-slate-800 rounded-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div class="p-4 border-b border-slate-200 dark:border-slate-700 flex justify-between items-center">
          <h2 class="text-lg font-bold">公告详情</h2>
          <button @click="showDetailModal = false" class="p-1 hover:bg-slate-100 rounded">
            <span class="material-symbols-outlined">close</span>
          </button>
        </div>
        <div class="p-4 space-y-4">
          <div class="flex items-center gap-2">
            <span :class="getTypeClass(detailItem.type)" class="px-2 py-1 rounded-full text-xs">{{ getTypeName(detailItem.type) }}</span>
            <span :class="detailItem.isPublished ? 'bg-green-100 text-green-800' : 'bg-amber-100 text-amber-800'" class="px-2 py-1 rounded-full text-xs">
              {{ detailItem.isPublished ? '已发布' : '草稿' }}
            </span>
          </div>
          <h3 class="text-xl font-bold text-slate-900 dark:text-white">{{ detailItem.title }}</h3>
          <div class="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span class="text-slate-500">目标受众：</span>
              <span class="font-medium">{{ getAudienceName(detailItem.targetAudience) }}</span>
            </div>
            <div>
              <span class="text-slate-500">优先级：</span>
              <span class="font-medium">{{ detailItem.priority === 'HIGH' ? '高' : detailItem.priority === 'LOW' ? '低' : '普通' }}</span>
            </div>
            <div>
              <span class="text-slate-500">浏览次数：</span>
              <span class="font-medium">{{ detailItem.viewCount || 0 }}</span>
            </div>
            <div>
              <span class="text-slate-500">创建时间：</span>
              <span class="font-medium">{{ formatTime(detailItem.createTime) }}</span>
            </div>
            <div v-if="detailItem.publishTime">
              <span class="text-slate-500">发布时间：</span>
              <span class="font-medium">{{ formatTime(detailItem.publishTime) }}</span>
            </div>
            <div v-if="detailItem.expireTime">
              <span class="text-slate-500">过期时间：</span>
              <span class="font-medium">{{ formatTime(detailItem.expireTime) }}</span>
            </div>
          </div>
          <div class="border-t border-slate-200 dark:border-slate-700 pt-4">
            <h4 class="font-medium mb-2">公告内容</h4>
            <p class="text-slate-700 dark:text-slate-300 whitespace-pre-wrap">{{ detailItem.content }}</p>
          </div>
          <div class="flex justify-end gap-3 pt-4 border-t border-slate-200 dark:border-slate-700">
            <button @click="showDetailModal = false" class="px-4 py-2 border rounded-lg">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import api from '@/api'

const announcements = ref([])
const stats = ref({})
const page = ref(0)
const size = ref(10)
const hasNext = ref(false)
const showCreateModal = ref(false)
const showDetailModal = ref(false)
const editingId = ref(null)
const detailItem = ref({})

const filters = ref({ type: '', isPublished: '', targetAudience: '' })
const form = ref({ title: '', type: 'SYSTEM', targetAudience: 'ALL', priority: 'NORMAL', content: '', expireTime: '' })

const getTypeName = (type) => ({ SYSTEM: '系统公告', MAINTENANCE: '维护公告', UPDATE: '更新公告', PROMOTION: '推广公告' }[type] || type)
const getTypeClass = (type) => ({ SYSTEM: 'bg-blue-100 text-blue-800', MAINTENANCE: 'bg-orange-100 text-orange-800', UPDATE: 'bg-purple-100 text-purple-800', PROMOTION: 'bg-pink-100 text-pink-800' }[type] || 'bg-gray-100')
const getAudienceName = (audience) => ({ ALL: '所有用户', JOB_SEEKER: '求职者', ENTERPRISE: '企业' }[audience] || audience)
const formatTime = (time) => time ? new Date(time).toLocaleString('zh-CN') : '-'

const loadData = async () => {
  try {
    const params = { page: page.value, size: size.value }
    if (filters.value.type) params.type = filters.value.type
    if (filters.value.isPublished !== '') params.isPublished = parseInt(filters.value.isPublished)
    if (filters.value.targetAudience) params.targetAudience = filters.value.targetAudience
    const res = await api.getAnnouncementList(params)
    announcements.value = (res.data?.content || res.content || [])
    hasNext.value = !(res.data?.last || res.last)
    const statsRes = await api.getAnnouncementStatistics()
    stats.value = statsRes.data || statsRes || {}
  } catch (e) { 
    console.error(e) 
    announcements.value = []
    stats.value = {}
  }
}

watch(filters, () => {
  page.value = 0
  loadData()
}, { deep: true })

const loadPage = (p) => { page.value = p; loadData() }

const editAnnouncement = (item) => {
  editingId.value = item.id
  form.value = { title: item.title, type: item.type, targetAudience: item.targetAudience, priority: item.priority || 'NORMAL', content: item.content, expireTime: item.expireTime ? item.expireTime.slice(0, 16) : '' }
  showCreateModal.value = true
}

const closeModal = () => { showCreateModal.value = false; editingId.value = null; form.value = { title: '', type: 'SYSTEM', targetAudience: 'ALL', priority: 'NORMAL', content: '', expireTime: '' } }

const viewDetail = (item) => {
  detailItem.value = item
  showDetailModal.value = true
}

const saveAnnouncement = async () => {
  try {
    const data = { ...form.value }
    if (data.expireTime) data.expireTime = new Date(data.expireTime).toISOString()
    if (editingId.value) {
      await api.updateAnnouncement(editingId.value, data)
    } else {
      await api.createAnnouncement(data)
    }
    closeModal()
    loadData()
  } catch (e) { alert('保存失败: ' + e.message) }
}

const publish = async (id) => {
  try {
    await api.publishAnnouncement(id)
    loadData()
  } catch (e) { alert('发布失败: ' + e.message) }
}

const unpublish = async (id) => {
  try {
    await api.unpublishAnnouncement(id)
    loadData()
  } catch (e) { alert('取消发布失败: ' + e.message) }
}

const deleteAnnouncement = async (id) => {
  if (!confirm('确定删除此公告？')) return
  try {
    await api.deleteAnnouncement(id)
    loadData()
  } catch (e) { alert('删除失败: ' + e.message) }
}

onMounted(loadData)
</script>