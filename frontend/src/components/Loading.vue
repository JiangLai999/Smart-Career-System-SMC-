<template>
  <div v-if="loading" :class="fullscreen ? 'fixed inset-0 z-[9998] flex items-center justify-center bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm' : 'flex items-center justify-center py-12'">
    <div class="flex flex-col items-center gap-4">
      <div class="relative">
        <div class="size-12 rounded-full border-4 border-slate-200 dark:border-slate-700"></div>
        <div class="size-12 rounded-full border-4 border-primary border-t-transparent absolute top-0 left-0 animate-spin"></div>
      </div>
      <p v-if="text" class="text-sm font-medium text-slate-500 dark:text-slate-400">{{ text }}</p>
    </div>
  </div>
  <slot v-else></slot>
</template>

<script setup>
defineProps({
  loading: {
    type: Boolean,
    default: true
  },
  text: {
    type: String,
    default: ''
  },
  fullscreen: {
    type: Boolean,
    default: false
  }
})
</script>
