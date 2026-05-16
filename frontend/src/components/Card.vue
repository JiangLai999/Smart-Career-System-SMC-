<template>
  <div 
    :class="[
      'bg-white dark:bg-slate-900 rounded-2xl border transition-all duration-300',
      shadowClass,
      hoverClass,
      paddingClass,
      borderClass,
      clickable ? 'cursor-pointer' : ''
    ]"
    @click="clickable && $emit('click')"
  >
    <div v-if="title || $slots.header" class="flex items-center justify-between mb-4">
      <div class="flex items-center gap-3">
        <span v-if="icon" class="material-symbols-outlined text-2xl text-primary">{{ icon }}</span>
        <h3 class="text-lg font-bold tracking-tight text-slate-900 dark:text-white">{{ title }}</h3>
      </div>
      <div v-if="$slots.action">
        <slot name="action"></slot>
      </div>
    </div>
    <slot></slot>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  icon: {
    type: String,
    default: ''
  },
  shadow: {
    type: String,
    default: 'md',
    validator: (v) => ['none', 'sm', 'md', 'lg', 'xl'].includes(v)
  },
  hover: {
    type: Boolean,
    default: false
  },
  padding: {
    type: String,
    default: 'md',
    validator: (v) => ['none', 'sm', 'md', 'lg'].includes(v)
  },
  border: {
    type: Boolean,
    default: true
  },
  clickable: {
    type: Boolean,
    default: false
  }
})

defineEmits(['click'])

const shadowClass = computed(() => {
  const shadows = {
    none: '',
    sm: 'shadow-sm',
    md: 'shadow-sm',
    lg: 'shadow-lg',
    xl: 'shadow-xl'
  }
  return shadows[props.shadow]
})

const hoverClass = computed(() => {
  if (!props.hover) return ''
  return 'hover:shadow-lg hover:-translate-y-1 hover:border-primary/30'
})

const paddingClass = computed(() => {
  const paddings = {
    none: '',
    sm: 'p-4',
    md: 'p-6',
    lg: 'p-8'
  }
  return paddings[props.padding]
})

const borderClass = computed(() => {
  return props.border ? 'border-slate-200 dark:border-slate-800' : 'border-transparent'
})
</script>
