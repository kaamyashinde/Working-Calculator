<script setup>
import CalcBtn from '@/components/CalcBtn.vue'
import { ref } from 'vue'
import Input from '@/components/Input.vue'
import NumBtnDisplay from '@/displays/NumBtnDisplay.vue'
import MiscBtnDisplay from '@/displays/MiscBtnDisplay.vue'
import OperatorBtnDisplay from '@/displays/OperatorBtnDisplay.vue'
import LastRowDisplay from '@/displays/LastRowDisplay.vue'
import CalcLog from '@/components/CalcLog.vue'
import { useCalculatorStore } from '@/stores/calculatorStore.js'
import { saveUserInfo } from '@/stores/userInfo'
import { useRouter } from 'vue-router'

const router = useRouter()

const types = ['number', 'operator']

const store = useCalculatorStore()
const userInfo = saveUserInfo()

const handleBtnClick = (value) => {
  store.handleBtnClick(value)
}

function goToReview() {
  router.push({ name: 'review' }) // Navigate to the home route
}

function logOut(){
  userInfo.setUserInfo('', '', '')
  router.push({ name: 'login' })
}
</script>

<template>
  <div>
    <button id="goToReview" @click="goToReview">Leave us a review?</button>
    <button id="logOut" @click="logOut">Log out</button>
  </div>
  <div class="custom-container">
    <div class="calc-container">
      <div>
        <h1>Calculator</h1>
      </div>
      <div>
        <Input
          v-model:text="store.text"
          @update:text="store.updateText()"
          @perform-calculation="store.performCalculation()"
        />
      </div>
      <div class="btns-container">
        <div>
          <MiscBtnDisplay @miscBtnClick="handleBtnClick" />
          <NumBtnDisplay @numBtnClick="handleBtnClick" types="types" />
        </div>
        <div>
          <OperatorBtnDisplay types="types" @operatorBtnClick="handleBtnClick" />
        </div>
        <div class="add-span">
          <LastRowDisplay @lastRowClick="handleBtnClick" />
        </div>
      </div>
    </div>
    <div>
      <h1>Calculation Log</h1>
      <CalcLog :resultArray="store.history" />
    </div>
  </div>
</template>

<style scoped>
.custom-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
}
.calc-container {
  display: grid;
  grid-template-columns: 1fr;
}

.btns-container {
  display: grid;
  grid-template-columns: 3fr 1fr;
}
.add-span {
  grid-column: span 2;
}

h1 {
  text-align: center;
}
</style>
