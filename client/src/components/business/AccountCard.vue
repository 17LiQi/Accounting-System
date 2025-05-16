<template>
    <div class="account-card">
    <h3>{{ account.name }}</h3>
<p>Balance: {{ account.balance }}</p>
</div>
</template>
<script setup lang="ts">
import { Account } from '@/models/accounts/account';
defineProps<{ account: Account }>();
</script>
<style scoped>
.account-card { border: 1px solid #ccc; padding: 16px; }
</style>