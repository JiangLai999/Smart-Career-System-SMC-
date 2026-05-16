import { defineStore } from 'pinia'
import api from '@/api'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null
  }),
  getters: {
    isLoggedIn: state => !!state.token,
    userType: state => state.userInfo?.userType || ''
  },
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    async login(credentials) {
      const data = await api.login(credentials)
      this.setToken(data.token)
      await this.fetchUserInfo()
      return data
    },
    async fetchUserInfo() {
      try {
        this.userInfo = await api.getUserInfo()
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    },
    async register(userData) {
      return await api.register(userData)
    },
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    }
  }
})
