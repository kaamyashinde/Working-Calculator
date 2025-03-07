<script setup>
import { ref, watch } from 'vue'
import { useCalculatorStore } from '@/stores/calculatorStore.js'

const props = defineProps({
  text: {
    type: String,
    required: true,
  },
})

const store = useCalculatorStore()
const localText = ref(props.text)

watch(
  () => props.text,
  (newVal) => {
    localText.value = newVal
  },
)

function onInput(e) {
  const allowedKeys = /[0-9+\-*/.]|Backspace/
  if (!allowedKeys.test(e.data) && e.inputType !== 'deleteContentBackward') {
    e.target.value = localText.value
    console.log('inside if statement in OnInput')
    return
  }
  console.log('outside if statement in onInput')
  console.log('localtext: ' + localText)
  localText.value = e.target.value
  store.updateText(localText.value)
}

function getInputValue() {
  return localText.value
}
</script>

<template>
  <input @input="onInput" :value="localText" placeholder="Type here" />
</template>

<style scoped>
input {
  width: 100%;
  height: 25px;
  background-color: rgba(127, 128, 131, 0.7);
  color: white;
  border-radius: 5px;
  border: 1px solid black;
}
</style>
