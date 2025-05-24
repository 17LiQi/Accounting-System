<template>
  <div class="transaction-form">
    <h3>{{ isEdit ? '修改交易记录' : '新增交易记录' }}</h3>
    
    <form @submit.prevent="handleSubmit" class="form">
      <div class="form-group" v-if="!fixedSubAccount">
        <label>选择子账户</label>
        <select v-model="formData.subAccountId" required>
          <option value="">请选择子账户</option>
          <option v-for="account in subAccounts" :key="account.subAccountId" :value="account.subAccountId">
            {{ account.accountName }} ({{ account.accountNumber }})
          </option>
        </select>
      </div>

      <div class="form-group">
        <label>交易类型</label>
        <select v-model="formData.typeId" required>
          <option value="">请选择交易类型</option>
          <option v-for="type in transactionTypes" :key="type.typeId" :value="type.typeId">
            {{ type.typeName }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label>金额</label>
        <input 
          type="number" 
          v-model="formData.amount" 
          step="0.01" 
          min="0.01" 
          required
          placeholder="请输入金额"
        />
      </div>

      <div class="form-group">
        <label>交易时间</label>
        <input 
          type="datetime-local" 
          v-model="formData.time" 
          required
        />
      </div>

      <div class="form-group">
        <label>备注</label>
        <textarea 
          v-model="formData.remarks" 
          placeholder="请输入备注信息"
          rows="3"
        ></textarea>
      </div>

      <div class="form-actions">
        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '保存中...' : '保存' }}
        </button>
        <button type="button" class="cancel-btn" @click="$emit('cancel')" :disabled="loading">
          取消
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { transactionsService } from '@/api/services/transactions';
import { accountsService } from '@/api/services/accounts';
import type { TransactionDTO } from '@/api/models/transactions/transaction-dto';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';

const props = defineProps<{
  fixedSubAccount?: SubAccountDTO;
  transaction?: TransactionDTO;
}>();

const emit = defineEmits<{
  (e: 'submit', transaction: TransactionDTO): void;
  (e: 'cancel'): void;
}>();

const loading = ref(false);
const subAccounts = ref<SubAccountDTO[]>([]);
const transactionTypes = ref<TransactionTypeDTO[]>([]);

const formData = ref({
  subAccountId: props.fixedSubAccount?.subAccountId || props.transaction?.subAccountId || '',
  typeId: props.transaction?.typeId || '',
  amount: props.transaction?.amount || '',
  time: props.transaction?.time 
    ? new Date(props.transaction.time).toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }).slice(0, 16).replace(' ', 'T')
    : new Date().toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }).slice(0, 16).replace(' ', 'T'),
  remarks: props.transaction?.remarks || ''
});

const isEdit = computed(() => !!props.transaction);

const fetchSubAccounts = async () => {
  try {
    subAccounts.value = await accountsService.getAllSubAccounts();
  } catch (err) {
    console.error('获取子账户列表失败:', err);
  }
};

const fetchTransactionTypes = async () => {
  try {
    transactionTypes.value = await transactionsService.getTransactionTypes();
  } catch (err) {
    console.error('获取交易类型列表失败:', err);
  }
};

const handleSubmit = async () => {
  try {
    loading.value = true;
    
    // 确保所有必要字段都存在且格式正确
    const request = {
      subAccountId: Number(formData.value.subAccountId),
      typeId: Number(formData.value.typeId),
      amount: Number(formData.value.amount).toFixed(2), // 确保金额格式正确
      time: new Date(formData.value.time + ':00+08:00').toISOString(), // 添加时区信息
      remarks: formData.value.remarks || '' // 确保remarks不为undefined
    };

    console.log('提交的交易数据:', request); // 添加调试日志

    let result: TransactionDTO;
    try {
      if (isEdit.value && props.transaction) {
        result = await transactionsService.updateTransaction(props.transaction.transactionId, request);
      } else {
        result = await transactionsService.createTransaction(request);
      }
    } catch (updateError: any) {
      // 如果更新失败，尝试重新获取最新的交易记录
      if (isEdit.value && props.transaction) {
        try {
          result = await transactionsService.getTransaction(props.transaction.transactionId);
          console.log('重新获取交易记录成功:', result);
        } catch (getError) {
          console.error('重新获取交易记录失败:', getError);
          throw updateError; // 如果重新获取也失败，则抛出原始错误
        }
      } else {
        throw updateError;
      }
    }

    // 更新子账户余额
    try {
      if (props.fixedSubAccount) {
        await accountsService.recalculateBalance(props.fixedSubAccount.subAccountId);
      } else {
        await accountsService.recalculateBalance(Number(formData.value.subAccountId));
      }
    } catch (balanceError) {
      console.error('更新余额失败:', balanceError);
      // 继续执行，不影响整体流程
    }

    // 通知父组件更新成功
    emit('submit', result);
  } catch (err: any) {
    console.error('保存交易记录失败:', err);
    const errorMessage = err.response?.data?.message || err.message || '未知错误';
    alert('保存失败: ' + errorMessage);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  if (!props.fixedSubAccount) {
    await fetchSubAccounts();
  }
  await fetchTransactionTypes();
});

defineExpose({});
</script>

<script lang="ts">
export default {
  name: 'TransactionForm'
};
</script>

<style scoped>
.transaction-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-group label {
  color: #666;
  font-size: 0.9rem;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.submit-btn,
.cancel-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.submit-btn {
  background-color: var(--primary-color);
  color: white;
}

.submit-btn:hover:not(:disabled) {
  background-color: var(--primary-color-dark);
}

.cancel-btn {
  background-color: #666;
  color: white;
}

.cancel-btn:hover:not(:disabled) {
  background-color: #555;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style> 