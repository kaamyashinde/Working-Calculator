import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

export const saveUserInfo = defineStore('userInfo', () => {
  const savedName = ref('')
  const savedEmail = ref('')
  const savedMessage = ref('')
  const savedPassword = ref('')
  const savedToken = ref('')
  const statusMessage = ref('hi')

  async function handleSubmit(name: string, email: string, message: string) {
    savedName.value = name
    savedEmail.value = email
    savedMessage.value = message
    console.log(savedEmail.value, savedMessage.value, savedName.value)

    const response = await axios.get('http://localhost:5170/status')
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
  }

  function clearUserInfo() {
    savedName.value = ''
    savedEmail.value = ''
    savedPassword.value = ''
    savedToken.value = ''
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
