<template>
  <div class="flex flex-col items-center justify-center py-16 px-4">
    <div class="size-32 rounded-full bg-slate-100 dark:bg-slate-800 flex items-center justify-center mb-6">
      <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">{{ icon }}</span>
    </div>
    <h3 class="text-xl font-bold text-slate-700 dark:text-slate-300 mb-2 tracking-tight">{{ title }}</h3>
    <p class="text-sm text-slate-500 dark:text-slate-400 text-center max-w-sm mb-6">{{ description }}</p>
    <slot name="action">
      <button 
        v-if="actionText"
        @click="$emit('action')"
        class="px-6 py-2.5 bg-primary hover:bg-primary/90 text-white rounded-lg font-semibold text-sm transition-all shadow-lg shadow-primary/20 flex items-center gap-2"
      >
        <span v-if="actionIcon" class="material-symbols-outlined text-lg">{{ actionIcon }}</span>
        {{ actionText }}
      </button>
    </slot>
  </div>
</template>

<script setup>
defineProps({
  icon: {
    type: String,
    default: 'search_off'
  },
  title: {
    type: String,
    default: '暂无数据'
  },
  description: {
    type: String,
    default: '这里还没有任何内容'
  },
  actionText: {
    type: String,
    default: ''
  },
  actionIcon: {
    type: String,
    default: ''
  }
})

defineEmits(['action'])
</script>
