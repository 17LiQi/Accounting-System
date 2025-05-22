<script setup lang="ts">
import { ref } from 'vue';
import type { AccountDTO } from '@/api/models/accounts';

const emit = defineEmits<{
  (e: 'submit', account: AccountDTO): void;
  (e: 'cancel'): void;
}>();

const account = ref<Partial<AccountDTO>>({
  accountName: '',
  typeId: 1,
  description: ''
});

const handleSubmit = () => {
  emit('submit', account.value as AccountDTO);
};

const handleCancel = () => {
  emit('cancel');
};
</script>

<template>
  <div class="account-form">
    <h2>新增账户</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="accountName">账户名称</label>
        <input
          id="accountName"
          v-model="account.accountName"
          type="text"
          required
          placeholder="请输入账户名称"
        />
      </div>

      <div class="form-group">
        <label for="typeId">账户类型</label>
        <select id="typeId" v-model="account.typeId" required>
          <option value="1">银行账户</option>
          <option value="2">微信账户</option>
          <option value="3">支付宝账户</option>
        </select>
      </div>

      <div class="form-group">
        <label for="description">描述</label>
        <textarea
          id="description"
          v-model="account.description"
          placeholder="请输入账户描述"
        />
      </div>

      <div class="form-actions">
        <button type="button" class="cancel-btn" @click="handleCancel">取消</button>
        <button type="submit" class="submit-btn">保存</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.account-form {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

input,
select,
textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

textarea {
  height: 100px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn,
.submit-btn {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.cancel-btn {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  color: #666;
}

.submit-btn {
  background-color: var(--primary-color);
  border: none;
  color: white;
}

.submit-btn:hover {
  background-color: var(--primary-color-dark);
}
</style>

<script lang="ts">
export default {
  name: 'AccountForm'
};
</script> 