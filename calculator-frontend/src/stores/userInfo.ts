import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

export const saveUserInfo = defineStore('userInfo', () => {
  // Initialize state from localStorage if available
  const savedName = ref(localStorage.getItem('user_name') || '')
  const savedEmail = ref(localStorage.getItem('user_email') || '')
  const savedMessage = ref('')
  const savedPassword = ref('')
  const savedToken = ref(localStorage.getItem('user_token') || '')
  const statusMessage = ref('hi')

  async function handleSubmit(name: string, email: string, message: string) {
    savedName.value = name
    savedEmail.value = email
    savedMessage.value = message
    console.log(savedEmail.value, savedMessage.value, savedName.value)

    const response = await axios.get('http://localhost:3000/status')
    if (response.data == true) {
      statusMessage.value = 'Success: Your review has been submitted!'
    } else {
      statusMessage.value = 'Error: Something went wrong!'
    }
  }

  async function setUserInfo(username: string, email: string, password: string, token: string = '') {
    savedName.value = username
    savedEmail.value = email
    savedPassword.value = password
    savedToken.value = token

    // Save to localStorage
    localStorage.setItem('user_name', username)
    localStorage.setItem('user_email', email)
    localStorage.setItem('user_token', token)
  }

  function clearUserInfo() {
    savedName.value = ''
    savedEmail.value = ''
    savedPassword.value = ''
    savedToken.value = ''

    // Clear localStorage
    localStorage.removeItem('user_name')
    localStorage.removeItem('user_email')
    localStorage.removeItem('user_token')
  }

  return {
    handleSubmit,
    statusMessage,
    setUserInfo,
    clearUserInfo,
    savedName,
    savedPassword,
    savedToken,
  }
})
