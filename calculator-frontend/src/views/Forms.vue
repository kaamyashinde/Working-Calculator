<script lang="ts">
import { useField, useForm } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import { saveUserInfo } from '@/stores/userInfo'

export default {
  setup() {
    const router = useRouter()
    const store = saveUserInfo()

    // Define the validation schema
    const schema = yup.object({
      name: yup.string().required('Name is required'),
      email: yup.string().email('Invalid email').required('Email is required'),
      message: yup
        .string()
        .min(10, 'Message must be at least 10 characters long')
        .required('Message is required'),
    })

    // Initialize the form and fields
    const { handleSubmit, errors, isSubmitting, meta } = useForm({
      validationSchema: schema,
    })

    const { value: name, errorMessage: nameError } = useField('name')
    const { value: email, errorMessage: emailError } = useField('email')
    const { value: message, errorMessage: messageError } = useField<string>('message')

    // Form submit handler
    const submitReviewForm = handleSubmit(async (values) => {
      await store.handleSubmit(name.value as string, email.value as string, message.value as string)
      alert(store.statusMessage)
      router.push({ name: 'home' })
    })

    function goToCalc() {
      router.push({ name: 'home' }) // Navigate to the home route
    }

    return {
      name,
      nameError,
      email,
      emailError,
      message,
      messageError,
      submitReviewForm,
      errors,
      isSubmitting,
      meta,
      goToCalc,
    }
  },
}
</script>

<template>
  <button id="goToCalc" @click="goToCalc">Go back to the calculator</button>
  <div id="formContainer">
    <form @submit.prevent="submitReviewForm">
      <div id="formTitle">
        <label>Leave us a review!</label>
      </div>
      <div id="name">
        <label id="nameLabel">Name: </label>
        <input type="text" v-model="name" />
        <span id="nameErrSpan">{{ nameError }}</span>
      </div>
      <div id="email">
        <label id="emailLabel">Email: </label>
        <input type="email" v-model="email" />
        <span id="emailErrSpan">{{ emailError }}</span>
      </div>
      <div id="message">
        <label id="messageLabel">Message: </label>
        <textarea v-model="message"></textarea>
        <span id="messageErrSpan">{{ messageError }}</span>
      </div>
      <button id="submit" type="submit" :disabled="isSubmitting || !meta.valid">
        Submit and Go back
      </button>
    </form>
  </div>
</template>

<style scoped>
input,
textarea {
  width: 100%;
  margin-bottom: 10px;
}

span {
  color: red;
  font-size: 12px;
}
</style>
