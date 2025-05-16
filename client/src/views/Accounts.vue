<template>
  <div>
    <h1>Accounts</h1>
    <div v-if="store.loading">Loading...</div>
    <AccountCard v-else v-for="account in store.accounts" :key="account.id" :account="account" />
    <button @click="store.fetchAccounts">Load Accounts</button>
  </div>
</template>
<script setup lang="ts">
import { useAccountStore } from '@/store/modules/accounts';
import AccountCard from '@/components/business/AccountCard.vue';
const store = useAccountStore();
</script>