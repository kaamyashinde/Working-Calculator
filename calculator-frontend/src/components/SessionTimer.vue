<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { saveUserInfo } from '@/stores/userInfo'
import { useRouter } from 'vue-router'

const timeRemaining = ref('')
const router = useRouter()
const userInfo = saveUserInfo()
let timerInterval: number | undefined

function updateTimer() {
  const token = userInfo.savedToken
  if (!token) {
    timeRemaining.value = 'Session expired'
    return
  }

  // Get expiration time from JWT token (payload is in the middle part between dots)
  const payload = JSON.parse(atob(token.split('.')[1]))
  const expirationTime = payload.exp * 1000 // Convert to milliseconds
  const now = Date.now()
  const remainingTime = expirationTime - now

  if (remainingTime <= 0) {
    timeRemaining.value = 'Session expired'
    userInfo.clearUserInfo()
    router.push('/login')
    return
  }

  // Convert remaining time to minutes and seconds
  const minutes = Math.floor(remainingTime / 60000)
  const seconds = Math.floor((remainingTime % 60000) / 1000)
  timeRemaining.value = `${minutes}:${seconds.toString().padStart(2, '0')}`
}

onMounted(() => {
  updateTimer()
  timerInterval = setInterval(updateTimer, 1000) as unknown as number
})

onUnmounted(() => {
  if (timerInterval) {
    clearInterval(timerInterval)
  }
})
</script>

<template>
  <div class="session-timer">
    Session time remaining: {{ timeRemaining }}
  </div>
</template>

<style scoped>
.session-timer {
  position: fixed;
  top: 1rem;
  right: 1rem;
  background-color: #4a90e2;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.9rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
