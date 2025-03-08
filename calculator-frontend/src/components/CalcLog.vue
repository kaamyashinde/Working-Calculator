<script setup>
import { ref, watch, computed } from 'vue'

const props = defineProps({
  resultArray: {
    type: Array,
    required: true,
  },
})

const currentPage = ref(0)
const itemsPerPage = 10

const totalPages = ref(0)
watch(() => props.resultArray, (newArray) => {
  totalPages.value = Math.ceil(newArray.length / itemsPerPage)
}, { immediate: true })

const paginatedResults = computed(() => {
  const start = currentPage.value * itemsPerPage
  const end = start + itemsPerPage
  return props.resultArray.slice(start, end)
})

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--
  }
}
</script>

<template>
  <div class="calc-log">
    <ul>
      <li v-for="(result, index) in paginatedResults" :key="index">{{ result }}</li>
    </ul>
    <div class="pagination">
      <button :disabled="currentPage === 0" @click="prevPage">Previous</button>
      <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
      <button :disabled="currentPage >= totalPages - 1" @click="nextPage">Next</button>
    </div>
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

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

.pagination button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background-color: #4a90e2;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination button:hover:not(:disabled) {
  background-color: #357abd;
}

.pagination button:disabled {
  background-color: #cbd5e0;
  cursor: not-allowed;
}

.pagination span {
  color: #2c3e50;
  font-size: 0.9rem;
}
</style>
