<template>
  <div></div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

onMounted(() => {
  router.replace({ path: '/company/resumes', query: route.query })
})
</script>
