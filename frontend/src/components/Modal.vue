<template>
  <Teleport to="body">
    <Transition name="modal">
      <div 
        v-if="modelValue" 
        class="fixed inset-0 z-[9999] flex items-center justify-center p-4"
        @click.self="!persistent && close()"
      >
        <div class="absolute inset-0 bg-black/50 backdrop-blur-sm"></div>
        <div 
          :class="[
            'relative bg-white dark:bg-slate-900 rounded-2xl shadow-2xl w-full max-h-[90vh] overflow-hidden',
            sizeClass
          ]"
          @click.stop
        >
          <div v-if="title || $slots.header" class="flex items-center justify-between p-6 border-b border-slate-200 dark:border-slate-800">
            <h3 class="text-xl font-bold tracking-tight text-slate-900 dark:text-white">{{ title }}</h3>
            <button 
              v-if="showClose"
              class="text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 transition-colors"
              @click="close"
            >
              <span class="material-symbols-outlined text-2xl">close</span>
            </button>
          </div>
          
          <div :class="['overflow-y-auto', bodyClass]">
            <slot></slot>
          </div>
          
          <div v-if="$slots.footer" class="flex items-center justify-end gap-3 p-6 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/50">
            <slot name="footer"></slot>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { computed, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  size: {
    type: String,
    default: 'md',
    validator: (v) => ['sm', 'md', 'lg', 'xl', 'full'].includes(v)
  },
  persistent: {
    type: Boolean,
    default: false
  },
  showClose: {
    type: Boolean,
    default: true
  },
  bodyClass: {
    type: String,
    default: 'p-6'
  }
})

const emit = defineEmits(['update:modelValue', 'close'])

const sizeClass = computed(() => {
  const sizes = {
    sm: 'max-w-sm',
    md: 'max-w-md',
    lg: 'max-w-lg',
    xl: 'max-w-xl',
    full: 'max-w-4xl'
  }
  return sizes[props.size]
})

const close = () => {
  emit('update:modelValue', false)
  emit('close')
}

watch(() => props.modelValue, (val) => {
  if (val) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
})
</script>

<style scoped>
.modal-enter-active,
.modal-leave-active {
  transition: all 0.3s ease;
}

.modal-enter-active > div:last-child,
.modal-leave-active > div:last-child {
  transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from > div:last-child,
.modal-leave-to > div:last-child {
  transform: scale(0.95) translateY(10px);
  opacity: 0;
}
</style>
