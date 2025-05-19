<!-- src/views/Accounts.vue -->
<template>
  <div>
    <h1>账户列表</h1>
    <div v-if="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <AccountCard v-for="account in accounts" :key="account.accountId" :account="account" />
    </div>
    <h2>创建账户</h2>
    <form @submit.prevent="handleSubmit">
      <div>
        <label>账户名称:</label>
        <input v-model="form.accountName" required />
      </div>
      <div>
        <label>账户类型:</label>
        <select v-model="form.type" required>
          <option v-for="type in accountTypes" :key="type" :value="type">{{ type }}</option>
        </select>
      </div>
      <Button type="submit" :disabled="loading">创建</Button>
    </form>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from 'vue';
import { useAccountStore } from '@/store/modules/accounts';
import { AccountRequest, AccountRequestTypeEnum } from '@/models/accounts';
import AccountCard from '@/components/business/AccountCard.vue';
import Button from '@/components/common/Button.vue';
const store = useAccountStore();
const accounts = computed(() => store.accounts);
const loading = computed(() => store.loading);
const error = computed(() => store.error);
const accountTypes = Object.values(AccountRequestTypeEnum);
const form = ref<AccountRequest>({
  accountName: '',
  typeId: 1, // 假设 typeId 与 type 对应
  type: AccountRequestTypeEnum.Bank,
});
const handleSubmit = async () => {
  await store.addAccount(form.value);
  form.value = { accountName: '', typeId: 1, type: AccountRequestTypeEnum.Bank };
};
// 初始加载账户列表
store.fetchAccounts();
</script>
<style scoped>
.error {
  color: red;
}
form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-width: 300px;
}
</style>