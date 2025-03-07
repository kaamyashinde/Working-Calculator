<script setup>
import { useCalculatorStore } from '@/stores/calculatorStore'
const store = useCalculatorStore()

const handleBtnClick = (value) => {
  store.handleBtnClick(value)
}
const props = defineProps({
  placeholder: {
    type: String,
    required: true,
  },
  btnType: {
    type: String,
    required: true,
  },
  customClass: {
    type: String,
    required: false,
  },
  btnClass: {
    type: String,
    required: false,
  },
})
</script>

<template>
  <div :class="customClass">
    <button :class="btnClass" @click="handleBtnClick(props.placeholder)">
      {{ props.placeholder }}
    </button>
  </div>
</template>

<style scoped>
button {
  width: 100%;
  height: 20px;
  border: black solid 1px;
  border-radius: 2px;
  color: black;
  background-color: white;
}
</style>
