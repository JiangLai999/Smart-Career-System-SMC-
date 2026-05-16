import { createApp } from 'vue'
import Toast from '@/components/Toast.vue'

let toastInstance = null
let toastContainer = null

export const useToast = () => {
  if (!toastInstance) {
    toastContainer = document.createElement('div')
    document.body.appendChild(toastContainer)
    const app = createApp(Toast)
    toastInstance = app.mount(toastContainer)
  }
  
  return {
    success: (message, title = '') => toastInstance.success(message, title),
    error: (message, title = '') => toastInstance.error(message, title),
    warning: (message, title = '') => toastInstance.warning(message, title),
    info: (message, title = '') => toastInstance.info(message, title),
    show: (message, type = 'info', title = '', duration = 3000) => 
      toastInstance.addToast(message, type, title, duration)
  }
}

export default useToast
