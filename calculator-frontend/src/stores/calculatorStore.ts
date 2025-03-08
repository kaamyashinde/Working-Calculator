import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { saveUserInfo } from './userInfo'

export const useCalculatorStore = defineStore('calculator', () => {
  // State
  const clear = ref('C')
  const del = ref('DEL')
  const equal = ref('=')
  const ans = ref('ANS')
  const text = ref('')
  const ansValue = ref<string | number>('')
  const history = ref<string[]>([])
  const userInfo = saveUserInfo()

  // Actions
  const handleBtnClick = (value: string): void => {
    if (value === clear.value) {
      text.value = ''
    } else if (value === del.value) {
      text.value = text.value.slice(0, -1)
    } else if (value === equal.value) {
      // Use the async method to calculate via the REST backend.
      performCalculationAsync(text.value)
      text.value = ''
    } else if (value === ans.value) {
      if (ansValue.value === null || ansValue.value === '') {
        alert('Error: ANS value is null or empty')
        return
      }
      text.value += ansValue.value.toString()
    } else {
      text.value += value
    }
  }

  // Synchronous calculation method (kept for reference)
  const performCalculation = (expression: string): void => {
    try {
      if (!expression.trim()) {
        console.log('Error: Empty expression')
        return
      }
      const sanitizedExpression = expression.replace(/--/g, '+')
      if (/[+\-*/]$/.test(sanitizedExpression)) {
        console.log('Error: Invalid expression format:', sanitizedExpression)
        return
      }
      if (/\/0(?!\d)/.test(sanitizedExpression)) {
        combineResults(expression, 'undefined')
        return
      }
      console.log('Evaluating:', sanitizedExpression)
      ansValue.value = new Function(`'use strict'; return (${sanitizedExpression})`)()
      combineResults(expression, ansValue.value)
    } catch (error) {
      console.error('Calculation error:', error)
    }
  }

  // New asynchronous method that uses the REST backend to perform the calculation
  const performCalculationAsync = async (expression: string): Promise<void> => {
    try {
      if (!expression.trim()) {
        console.log('Error: Empty expression')
        return
      }
      const payload = {
        username: userInfo.savedName,
        password: userInfo.savedPassword,
        expression: expression
      }
      console.log('Sending expression to backend:', payload)
      const response = await axios.post('http://localhost:5170/api/calculate', payload)
      const data = response.data
      ansValue.value = data.result
      combineResults(expression, data.result)
    } catch (error) {
      console.error('Calculation error with backend:', error)
    }
  }

  function setHistory(historyData: Array<{ id: number, expression: string, result: number | string, timestamp: string }>) {
    history.value = historyData.map(item => `${item.expression} = ${item.result}`);
  }

  const combineResults = (expression: string, result: string | number): void => {
    const resultString = `${expression} = ${result}`
    history.value.unshift(resultString)
  }

  const updateText = (newText: string): void => {
    text.value = newText
    console.log('updated text in store')
  }

  return {
    clear,
    del,
    equal,
    ans,
    text,
    ansValue,
    history,
    handleBtnClick,
    performCalculation,
    performCalculationAsync,
    combineResults,
    updateText,
    setHistory,
  }
})
