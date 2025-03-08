<script setup>
defineProps({
  resultArray: {
    type: Array,
    required: true,
  },
})
</script>

<template>
  <div class="calc-log">
    <ul>
      <li v-for="(result, index) in resultArray" :key="index">{{ result }}</li>
    </ul>
  </div>
</template>

<style scoped>
.calc-log {
  background: #ffffff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 400px;
  overflow-y: auto;
}

li {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #e2e8f0;
  color: #2c3e50;
  font-size: 1rem;
}

li:last-child {
  border-bottom: none;
}
</style>
