<template>
  <button
    :class="[
      'custom-button',
      variant,
      { 'is-loading': loading }
    ]"
    :disabled="disabled || loading"
    @click="$emit('click', $event)"
  >
    <span v-if="loading" class="loading-spinner"></span>
    <slot></slot>
  </button>
</template>

<script setup lang="ts">
defineProps<{
  variant?: 'primary' | 'secondary' | 'danger';
  disabled?: boolean;
  loading?: boolean;
}>();

defineEmits<{
  (e: 'click', event: MouseEvent): void;
}>();
</script>

<script lang="ts">
export default {
  name: 'Button'
};
</script>

<style scoped>
.custom-button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.custom-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.primary {
  background-color: #4CAF50;
  color: white;
}

.primary:hover:not(:disabled) {
  background-color: #45a049;
}

.secondary {
  background-color: #f0f0f0;
  color: #333;
}

.secondary:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.danger {
  background-color: #f44336;
  color: white;
}

.danger:hover:not(:disabled) {
  background-color: #da190b;
}

.is-loading {
  position: relative;
  color: transparent;
}

.loading-spinner {
  position: absolute;
  width: 16px;
  height: 16px;
  border: 2px solid #ffffff;
  border-radius: 50%;
  border-top-color: transparent;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>