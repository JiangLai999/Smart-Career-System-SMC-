<template>
  <Teleport to="body">
    <div class="fixed top-4 right-4 z-[9999] flex flex-col gap-3 pointer-events-none">
      <TransitionGroup name="toast">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          :class="[
            'pointer-events-auto flex items-center gap-3 px-5 py-4 rounded-xl shadow-xl min-w-[320px] max-w-md backdrop-blur-sm',
            toastTypeStyles[toast.type]
          ]"
        >
          <span class="material-symbols-outlined text-xl flex-shrink-0">{{ toastIcons[toast.type] }}</span>
          <div class="flex-1 min-w-0">
            <p v-if="toast.title" class="font-bold text-sm tracking-tight">{{ toast.title }}</p>
            <p class="text-sm font-medium opacity-90">{{ toast.message }}</p>
          </div>
          <button 
            @click="removeToast(toast.id)"
            class="flex-shrink-0 hover:opacity-70 transition-opacity"
          >
            <span class="material-symbols-outlined text-lg">close</span>
          </button>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup>
import { ref } from 'vue'

const toasts = ref([])
let toastId = 0

const toastTypeStyles = {
  success: 'bg-emerald-500 text-white',
  error: 'bg-red-500 text-white',
  warning: 'bg-amber-500 text-white',
  info: 'bg-primary text-white'
}

const toastIcons = {
  success: 'check_circle',
  error: 'error',
  warning: 'warning',
  info: 'info'
}

const addToast = (message, type = 'info', title = '', duration = 3000) => {
  const id = ++toastId
  toasts.value.push({ id, message, type, title })
  
  if (duration > 0) {
    setTimeout(() => {
      removeToast(id)
    }, duration)
  }
  
  return id
}

const removeToast = (id) => {
  const index = toasts.value.findIndex(t => t.id === id)
  if (index > -1) {
    toasts.value.splice(index, 1)
  }
}

const success = (message, title = '') => addToast(message, 'success', title)
const error = (message, title = '') => addToast(message, 'error', title)
const warning = (message, title = '') => addToast(message, 'warning', title)
const info = (message, title = '') => addToast(message, 'info', title)

defineExpose({
  addToast,
  removeToast,
  success,
  error,
  warning,
  info
})
</script>

<style scoped>
.toast-enter-active {
  animation: toast-in 0.3s ease-out;
}

.toast-leave-active {
  animation: toast-out 0.3s ease-in;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

@keyframes toast-in {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes toast-out {
  from {
    opacity: 1;
    transform: translateX(0);
  }
  to {
    opacity: 0;
    transform: translateX(100%);
  }
}
</style>
