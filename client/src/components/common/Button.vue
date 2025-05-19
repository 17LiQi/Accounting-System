<template>
  <button class="btn" :type="type" :disabled="disabled">
    <slot />
  </button>
</template>
<script setup lang="ts">
defineProps<{
  type?: 'button' | 'submit';
  disabled?: boolean;
}>();
</script>
<style scoped>
.btn {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>