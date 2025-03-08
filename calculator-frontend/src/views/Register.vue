<script lang="ts">
import {useField, useForm} from 'vee-validate'
import * as yup from 'yup'
import {useRouter} from 'vue-router'
import {saveUserInfo} from '@/stores/userInfo'
import axios from 'axios'
export default {
  setup(props, ctx) {
    const router = useRouter()
    const store = saveUserInfo()

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
    const registerUser = handleSubmit(async (values) => {
      console.log("sending data to server...")
      await axios.post('http://localhost:5170/auth/register', values)
      .then((response) => {
        console.log("Registration successful")
        store.setUserInfo(response.data.username, response.data.email, response.data.password)
        router.push('/')
      })
      .catch((error) => {
        console.error('Registration failed:', error)
      })
    })

    function switchToLogin(){
      router.push('/login')
    }

    return {
      username,
      usernameError,
      password,
      passwordError,
      registerUser,
      switchToLogin,
      isSubmitting,
      meta,
    }
  },
}

</script>

<template>
<button id="loginBtn" @click="switchToLogin">Already have an account? Login here</button>
<div id="formContainer">
  <form @submit.prevent="registerUser">
    <div id="formTitle">
      <label>Register</label>
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
    <button id="submit" type="submit" :disabled="isSubmitting || !meta.valid">Register</button>
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
