<!-- src/components/business/AccountCard.vue -->
<template>
  <div class="account-card">
    <h3>{{ account.accountName }}</h3>
    <p>类型: {{ account.accountType?.typeName }}</p>
  </div>
</template>
<script setup lang="ts">
import { Account } from '@/models/accounts';
defineProps<{ account: Account }>();
</script>
<style scoped>
.account-card {
  border: 1px solid #ccc;
  padding: 16px;
  margin: 8px 0;
  border-radius: 4px;
}
</style>