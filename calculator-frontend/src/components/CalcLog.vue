<script setup>
defineProps({
  resultArray: {
    type: Array,
    required: true,
  },
})
</script>

<template>
  <div>
    <ul>
      <li v-for="(result, index) in resultArray" :key="index">{{ result }}</li>
    </ul>
  </div>
</template>
