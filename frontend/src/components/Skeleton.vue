<template>
  <div class="animate-pulse">
    <div v-if="type === 'card'" class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6">
      <div class="flex items-start justify-between mb-4">
        <div class="h-4 bg-slate-200 dark:bg-slate-700 rounded-lg w-1/3"></div>
        <div class="h-4 bg-slate-200 dark:bg-slate-700 rounded-lg w-16"></div>
      </div>
      <div class="space-y-3">
        <div class="h-3 bg-slate-200 dark:bg-slate-700 rounded w-full"></div>
        <div class="h-3 bg-slate-200 dark:bg-slate-700 rounded w-5/6"></div>
        <div class="h-3 bg-slate-200 dark:bg-slate-700 rounded w-4/6"></div>
      </div>
      <div class="flex gap-2 mt-4">
        <div class="h-6 bg-slate-200 dark:bg-slate-700 rounded-full w-20"></div>
        <div class="h-6 bg-slate-200 dark:bg-slate-700 rounded-full w-16"></div>
      </div>
    </div>
    
    <div v-else-if="type === 'list'" class="space-y-3">
      <div v-for="i in count" :key="i" class="flex items-center gap-4 bg-white dark:bg-slate-900 rounded-xl p-4 border border-slate-200 dark:border-slate-800">
        <div class="size-12 bg-slate-200 dark:bg-slate-700 rounded-full flex-shrink-0"></div>
        <div class="flex-1 min-w-0">
          <div class="h-4 bg-slate-200 dark:bg-slate-700 rounded w-1/3 mb-2"></div>
          <div class="h-3 bg-slate-200 dark:bg-slate-700 rounded w-2/3"></div>
        </div>
        <div class="h-8 bg-slate-200 dark:bg-slate-700 rounded-lg w-20"></div>
      </div>
    </div>
    
    <div v-else-if="type === 'text'" class="space-y-2">
      <div v-for="i in count" :key="i" class="h-3 bg-slate-200 dark:bg-slate-700 rounded" :style="{ width: randomWidth }"></div>
    </div>
    
    <div v-else-if="type === 'table'" class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">
      <div class="border-b border-slate-200 dark:border-slate-800 p-4 flex gap-4">
        <div v-for="i in columns" :key="i" class="h-4 bg-slate-200 dark:bg-slate-700 rounded flex-1"></div>
      </div>
      <div v-for="i in count" :key="i" class="border-b border-slate-200 dark:border-slate-800 p-4 flex gap-4">
        <div v-for="j in columns" :key="j" class="h-3 bg-slate-200 dark:bg-slate-700 rounded flex-1"></div>
      </div>
    </div>
    
    <div v-else-if="type === 'stats'" class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6">
      <div v-for="i in count" :key="i" class="rounded-2xl p-6 bg-slate-200 dark:bg-slate-800 h-32">
        <div class="flex justify-between items-start mb-4">
          <div class="h-3 bg-slate-300 dark:bg-slate-700 rounded w-20"></div>
          <div class="size-6 bg-slate-300 dark:bg-slate-700 rounded-full"></div>
        </div>
        <div class="h-8 bg-slate-300 dark:bg-slate-700 rounded w-24 mb-3"></div>
        <div class="h-3 bg-slate-300 dark:bg-slate-700 rounded w-16"></div>
      </div>
    </div>
    
    <div v-else-if="type === 'form'" class="space-y-6 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6">
      <div v-for="i in count" :key="i">
        <div class="h-3 bg-slate-200 dark:bg-slate-700 rounded w-24 mb-2"></div>
        <div class="h-11 bg-slate-200 dark:bg-slate-700 rounded-lg w-full"></div>
      </div>
      <div class="h-11 bg-slate-200 dark:bg-slate-700 rounded-lg w-32"></div>
    </div>
    
    <div v-else class="h-4 bg-slate-200 dark:bg-slate-700 rounded-lg" :style="{ width: width }"></div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'card',
    validator: (value) => ['card', 'list', 'text', 'table', 'stats', 'form', 'default'].includes(value)
  },
  count: {
    type: Number,
    default: 3
  },
  columns: {
    type: Number,
    default: 4
  },
  width: {
    type: String,
    default: '100%'
  }
})

const randomWidth = computed(() => {
  const widths = ['75%', '80%', '85%', '90%', '95%', '100%']
  return widths[Math.floor(Math.random() * widths.length)]
})
</script>
