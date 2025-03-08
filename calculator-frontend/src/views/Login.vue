<script lang="ts">
import {useField, useForm} from 'vee-validate'
import * as yup from 'yup'
import {useRouter} from 'vue-router'
import {saveUserInfo} from '@/stores/userInfo'
import {useCalculatorStore} from '@/stores/calculatorStore'
import axios from 'axios'
export default {
  setup(props, ctx) {
    const router = useRouter()
    const store = saveUserInfo()
    const calcStore = useCalculatorStore()

    //Define the validation schema
    const schema = yup.object({
      username: yup.string().required('Username is required'),
      password: yup.string().required('Password is required')
    })

    //Initialise the form and fields
    const {handleSubmit, errors, isSubmitting, meta} = useForm({
      validationSchema: schema,
    })

    const {value: username, errorMessage: usernameError} = useField('username')
    const {value: password, errorMessage: passwordError} = useField('password')

    //Form submit handler
    const loginUser = handleSubmit(async (values) => {
      console.log("sending data to server...")
      await axios.post('http://localhost:5170/auth/login', values)
      .then((response) => {
        console.log("Login successful")
        store.setUserInfo(response.data.username, response.data.email, response.data.password)
        router.push('/')
        updateHistory(response.data.username, response.data.password)
      })
      .catch((error) => {
        console.error('Login failed:', error)
      })
    })

    async function updateHistory(username: string, password: string){
      console.log("Retreieving history from the server...")
      await axios.get('http://localhost:5170/api/history?username=' + username + '&password=' + password + "&page=0&size=10" )
      .then((response) => {
        console.log("History retrieved successfully")
        console.log(response.data.content)
        calcStore.setHistory(response.data.content)
      })
      .catch((error) => {
        console.error('History retrieval failed:', error)
      })
    }

    function switchToRegister(){
      router.push('/register')
    }

    return {
      username,
      usernameError,
      password,
      passwordError,
      loginUser,
      switchToRegister,
      isSubmitting,
      meta,
    }
  },
}

</script>

<template>
<button id="loginBtn" @click="switchToRegister">Don't have an account? Register here</button>
<div id="formContainer">
  <form @submit.prevent="loginUser">
    <div id="formTitle">
      <label>Login</label>
    </div>
    <div id="username">
      <label id="usernameLabel">Username: </label>
      <input type="text" v-model="username" />
      <span id="usernameErrSpan">{{ usernameError }}</span>
    </div>

    <div id="password">
      <label id="passwordLabel">Password: </label>
      <input type="password" v-model="password" />
      <span id="passwordErrSpan">{{ passwordError }}</span>
    </div>
    <button id="submit" type="submit" :disabled="isSubmitting || !meta.valid">Login</button>
  </form>
</div>
</template>
<style scoped>
input {
  width: 100%;
  margin-bottom: 10px;
}

span {
  color: red;
  font-size: 12px;
}
</style>
