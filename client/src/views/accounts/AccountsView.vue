<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useAccountsStore } from '@/store/modules/accounts';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';
import type { AccountDTO } from '@/api/models/accounts';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { AccountTypeDTO } from '@/api/models/accountTypes';
import { accountsService } from '@/api/services/accounts';
import { accountTypesService } from '@/api/services/accountTypes';
import { useRouter } from 'vue-router';
import { usersService } from '@/api/services/users';
import type { UserDTO } from '@/api/models/user/user-dto';
import { subAccountsService } from '@/api/services/subAccounts';
import { useAuthStore } from '@/store/modules/auth';
import type { SubAccountRequest } from '@/api/models/sub-accounts/sub-account-request';
import { SubAccountRequestCardTypeEnum } from '@/api/models/sub-accounts/sub-account-request';

const showEditUsersModal = ref(false);
const editingSubAccount = ref<SubAccountDTO | null>(null);
const selectedUsers = ref<number[]>([]);

const goToUserTransactions = (userId: number) => {
  router.push({
    name: 'transactions',
    query: {
      page: 0,
      size: 10,
      sort: 'desc',
      userId
    }
  });
};

const form = ref({
  accountName: '',
  typeId: 1,
  type: AccountRequestTypeEnum.Bank,
  userId: 0
});

const subAccountForm = ref({
  accountName: '',
  accountNumber: '',
  cardType: SubAccountRequestCardTypeEnum.Savings,
  balance: '0.00'
});

const cardTypes = Object.entries(SubAccountRequestCardTypeEnum).map(([key, value]) => ({
  value,
  label: {
    [SubAccountRequestCardTypeEnum.Savings]: '储蓄卡',
    [SubAccountRequestCardTypeEnum.Debit]: '借记卡',
    [SubAccountRequestCardTypeEnum.Credit]: '信用卡',
    [SubAccountRequestCardTypeEnum.Visa]: 'Visa卡',
    [SubAccountRequestCardTypeEnum.Mastercard]: '万事达卡',
    [SubAccountRequestCardTypeEnum.Wallet]: '钱包'
  }[value]
}));

const authStore = useAuthStore();
const accountsStore = useAccountsStore();
const router = useRouter();
const loading = ref(false);
const error = ref<string | null>(null);
const showCreateModal = ref(false);
const editingAccount = ref<AccountDTO | null>(null);
const debugInfo = ref<any>(null);

const currentUser = computed(() => authStore.currentUser);
const isAdmin = computed(() => currentUser.value?.role === 'ADMIN');

const accounts = ref<AccountDTO[]>([]);
const users = ref<UserDTO[]>([]);
const subAccounts = ref<Record<number, SubAccountDTO[]>>({});
const accountTypes = ref<AccountTypeDTO[]>([]);
const selectedTypeId = ref<number>(0); // 默认显示所有类型

const getAccountTypeName = (typeId: number): string => {
  const type = accountTypes.value.find(t => t.typeId === typeId);
  return type ? type.typeName : '未知类型';
};

const getAccountTypeId = (typeName: string): number => {
  const type = accountTypes.value.find(t => t.typeName === typeName);
  return type ? type.typeId : 1;
};

const getAccountRequestType = (typeName: string): AccountRequestTypeEnum => {
  const typeNameUpper = typeName.toUpperCase();
  return typeNameUpper as AccountRequestTypeEnum;
};

const getUserName = (userId: number): string => {
  const user = users.value.find(u => u.userId === userId);
  return user ? user.username : '未知用户';
};

const fetchAccounts = async () => {
  loading.value = true;
  error.value = null;
  try {
    const usersList = await usersService.getUsers();
    users.value = usersList;
    console.log('获取到的用户列表:', usersList);

    const accountsList = await accountsService.getAccounts();
    accounts.value = isAdmin.value
      ? accountsList
      : accountsList.filter(account => account.userId === currentUser.value?.userId);

    const allSubAccounts = await subAccountsService.getAllSubAccounts();
    console.log('获取到的子账户列表:', allSubAccounts);

    subAccounts.value = {};
    for (const account of accounts.value) {
      const accountSubAccounts = allSubAccounts
        .filter((subAccount: SubAccountDTO) => subAccount.accountId === account.accountId)
        .map((subAccount: SubAccountDTO) => ({
          ...subAccount,
          users: subAccount.users || []
        }));
      if (accountSubAccounts.length > 0) {
        subAccounts.value[account.accountId] = accountSubAccounts;
      }
    }
    console.log('分组后的子账户:', subAccounts.value);
  } catch (err: any) {
    console.error('获取账户列表失败:', err);
    error.value = err.response?.data?.message || err.message || '获取账户列表失败';
  } finally {
    loading.value = false;
  }
};

const getAccountName = (accountId: number): string => {
  const account = accounts.value.find(a => a.accountId === accountId);
  return account ? account.accountName : '未知账户';
};

const editAccount = (account: AccountDTO) => {
  editingAccount.value = account;
  form.value = {
    accountName: account.accountName || '',
    typeId: getAccountTypeId(account.type),
    type: getAccountRequestType(account.type),
    userId: account.userId
  };
  showCreateModal.value = true;
};

const deleteAccount = async (accountId: number) => {
  if (!confirm('确定要删除这个账户吗？')) return;

  try {
    await accountsStore.deleteAccount(accountId);
    await fetchAccounts();
  } catch (err: any) {
    error.value = err.response?.data?.message || err.message || '删除账户失败';
  }
};

const fetchAccountTypes = async () => {
  try {
    accountTypes.value = await accountTypesService.getAccountTypes();
    console.log('获取到的账户类型列表:', accountTypes.value);
  } catch (err: any) {
    console.error('获取账户类型列表失败:', err);
    error.value = err.response?.data?.message || err.message || '获取账户类型列表失败';
  }
};

const handleSubmit = async () => {
  try {
    if (!currentUser.value?.userId) {
      throw new Error('用户未登录');
    }
    if (!/^\d+(\.\d{2})?$/.test(subAccountForm.value.balance)) {
      throw new Error('余额格式错误，必须为数字且最多两位小数');
    }

    const accountRequest: AccountRequest = {
      accountName: form.value.accountName,
      typeId: form.value.typeId,
      type: getAccountRequestType(getAccountTypeName(form.value.typeId)),
      userId: isAdmin.value ? selectedUsers.value[0] || currentUser.value.userId : currentUser.value.userId
    };

    const subAccountRequest: SubAccountRequest = {
      accountId: 0,
      accountName: subAccountForm.value.accountName,
      accountNumber: subAccountForm.value.accountNumber,
      cardType: subAccountForm.value.cardType,
      balance: subAccountForm.value.balance,
      userIds: isAdmin.value ? selectedUsers.value : [currentUser.value.userId]
    };

    if (editingAccount.value?.accountId) {
      await accountsStore.updateAccount(editingAccount.value.accountId, accountRequest);
      subAccountRequest.accountId = editingAccount.value.accountId;
      const subAccount = await subAccountsService.getSubAccounts(editingAccount.value.accountId);
      await subAccountsService.updateSubAccount(
        subAccount.subAccountId,
        subAccountRequest
      );
    } else {
      const newAccount = await accountsStore.createAccount(accountRequest);
      subAccountRequest.accountId = newAccount.accountId;
      await subAccountsService.createSubAccount(newAccount.accountId, subAccountRequest);
    }

    await fetchAccounts();
    closeModal();
  } catch (err: any) {
    console.error('保存账户失败:', err);
    error.value = err.response?.data?.message || err.message || '保存账户失败';
  }
};

const closeModal = () => {
  showCreateModal.value = false;
  editingAccount.value = null;
  form.value = {
    accountName: '',
    typeId: 1,
    type: AccountRequestTypeEnum.Bank,
    userId: 0
  };
  subAccountForm.value = {
    accountName: '',
    accountNumber: '',
    cardType: SubAccountRequestCardTypeEnum.Savings,
    balance: '0.00'
  };
  selectedUsers.value = [];
};

const viewSubAccountDetails = (subAccount: SubAccountDTO) => {
  router.push(`/sub-accounts/${subAccount.subAccountId}`);
};

const getAssociatedUsers = (subAccounts: SubAccountDTO[] | undefined): string => {
  if (!subAccounts?.length) return '无关联用户';

  const usernames = new Set<string>();
  for (const subAccount of subAccounts) {
    if (subAccount.users && subAccount.users.length > 0) {
      subAccount.users.forEach((user: { userId: number; username: string; isAdmin: boolean }) => {
        usernames.add(`${user.username} (${user.isAdmin ? '管理员' : '普通用户'})`);
      });
    }
  }

  return usernames.size > 0 ? Array.from(usernames).join(', ') : '无关联用户';
};

const debugUserAssociations = async () => {
  console.group('账户用户关联调试信息');

  console.log('所有账户:', accounts.value.map(account => ({
    accountId: account.accountId,
    accountName: account.accountName,
    userId: account.userId,
    username: users.value.find(u => u.userId === account.userId)?.username || '未指定用户'
  })));

  console.log('所有用户:', users.value.map(user => ({
    userId: user.userId,
    username: user.username,
    role: user.role
  })));

  console.log('子账户用户关联:');
  Object.entries(subAccounts.value).forEach(([accountId, subAccountsList]) => {
    console.group(`账户 ${accountId} 的子账户:`);
    subAccountsList.forEach(subAccount => {
      console.log(`子账户 ${subAccount.subAccountId}:`, {
        accountName: subAccount.accountName,
        accountNumber: subAccount.accountNumber,
        associatedUsers: subAccount.users?.map((user: { userId: number; username: string; isAdmin: boolean }) => ({
          userId: user.userId,
          username: user.username,
          isAdmin: user.isAdmin
        })) || '无关联用户'
      });
    });
    console.groupEnd();
  });

  const userStats = new Map<number, { username: string; role: string; subAccountCount: number }>();
  Object.values(subAccounts.value).flat().forEach(subAccount => {
    if (subAccount.users) {
      subAccount.users.forEach((user: { userId: number; username: string; isAdmin: boolean }) => {
        if (!userStats.has(user.userId)) {
          userStats.set(user.userId, {
            username: user.username,
            role: user.isAdmin ? 'ADMIN' : 'USER',
            subAccountCount: 0
          });
        }
        userStats.get(user.userId)!.subAccountCount++;
      });
    }
  });
  console.log('用户关联统计:', Object.fromEntries(userStats));

  console.groupEnd();
};

const filteredAccounts = computed(() => {
  if (selectedTypeId.value === 0) return accounts.value;
  return accounts.value.filter(account => account.typeId === selectedTypeId.value);
});

const editSubAccountUsers = (subAccount: SubAccountDTO) => {
  editingSubAccount.value = subAccount;
  selectedUsers.value = subAccount.users?.map(user => user.userId) || [];
  showEditUsersModal.value = true;
};

const saveSubAccountUsers = async () => {
  if (!editingSubAccount.value) return;

  try {
    const subAccountRequest: SubAccountRequest = {
      accountId: editingSubAccount.value.accountId,
      accountName: editingSubAccount.value.accountName,
      accountNumber: editingSubAccount.value.accountNumber,
      cardType: editingSubAccount.value.cardType,
      balance: editingSubAccount.value.balance,
      userIds: selectedUsers.value
    };

    await subAccountsService.updateSubAccount(
      editingSubAccount.value.subAccountId,
      subAccountRequest
    );

    await fetchAccounts();
    closeEditUsersModal();
  } catch (err: any) {
    console.error('更新子账户用户关联失败:', err);
    error.value = err.response?.data?.message || err.message || '更新子账户用户关联失败';
  }
};

const closeEditUsersModal = () => {
  showEditUsersModal.value = false;
  editingSubAccount.value = null;
  selectedUsers.value = [];
};

onMounted(async () => {
  try {
    loading.value = true;
    console.log('开始加载账户管理页面数据...');
    await Promise.all([
      fetchAccountTypes(),
      fetchAccounts()
    ]);
    console.log('账户管理页面数据加载完成:', {
      accounts: accounts.value,
      subAccounts: subAccounts.value,
      users: users.value,
      accountTypes: accountTypes.value
    });
  } catch (err: any) {
    error.value = err.response?.data?.message || err.message || '加载数据失败';
    console.error('账户管理页面加载失败:', err);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="accounts-view">
    <div class="header">
      <h1>账户管理</h1>
      <div class="header-actions">
        <button @click="showCreateModal = true" class="create-btn">创建账户</button>
        <button @click="debugUserAssociations" class="debug-btn">调试: 打印用户关联</button>
      </div>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else>
      <!-- 账户类型过滤 -->
      <div class="filter">
        <label for="type-filter">账户类型:</label>
        <select v-model="selectedTypeId" id="type-filter">
          <option :value="0">全部</option>
          <option v-for="type in accountTypes" :key="type.typeId" :value="type.typeId">
            {{ type.typeName }}
          </option>
        </select>
      </div>

      <!-- 账户列表 -->
      <div class="accounts-grid">
        <div v-for="account in filteredAccounts" :key="account.accountId" class="account-card">
          <div class="account-header">
            <h3>{{ account.accountName }}</h3>
            <div class="account-actions">
              <button @click="editAccount(account)" class="action-btn">编辑</button>
              <button @click="deleteAccount(account.accountId)" class="action-btn danger">删除</button>
            </div>
          </div>
          <div class="account-info">
            <p>类型: {{ getAccountTypeName(account.typeId) }}</p>
            <!-- 移除用户展示行 -->
            <div v-if="subAccounts[account.accountId]?.length">
              <p>子账户:</p>
              <ul>
                <li v-for="subAccount in subAccounts[account.accountId]" :key="subAccount.subAccountId">
                  {{ subAccount.accountName }} ({{ subAccount.accountNumber }})
                </li>
              </ul>
              <p>
                关联用户:
                <template v-if="subAccounts[account.accountId]?.length">
                  <template
                    v-for="(user, index) in subAccounts[account.accountId].flatMap(sa => sa.users || [])"
                    :key="user.userId"
                  >
                    <template v-if="isAdmin || user.userId === currentUser?.userId">
                      <a
                        href="#"
                        @click.prevent="goToUserTransactions(user.userId)"
                      >
                        {{ user.username }} ({{ user.isAdmin ? '管理员' : '普通用户' }})
                      </a>
                    </template>
                    <template v-else>
                      {{ user.username }} (普通用户)
                    </template>
                    <span v-if="index < subAccounts[account.accountId].flatMap(sa => sa.users || []).length - 1">, </span>
                  </template>
                </template>
                <template v-else>无关联用户</template>
              </p>
            </div>
            <p v-else>无子账户</p>
          </div>
        </div>
        <div v-if="filteredAccounts.length === 0" class="no-accounts">
          暂无账户
        </div>
      </div>
    </div>

    <!-- 创建/编辑账户模态框 -->
    <div v-if="showCreateModal" class="modal">
      <div class="modal-content">
        <h2>{{ editingAccount ? '编辑账户' : '创建账户' }}</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="accountName">账户名称</label>
            <input v-model="form.accountName" id="accountName" required />
          </div>
          <div class="form-group">
            <label for="typeId">账户类型</label>
            <select v-model="form.typeId" id="typeId" required>
              <option v-for="type in accountTypes" :key="type.typeId" :value="type.typeId">
                {{ type.typeName }}
              </option>
            </select>
          </div>
          <div v-if="isAdmin" class="form-group">
            <label for="userId">关联用户</label>
            <select v-model="selectedUsers" id="userId" multiple>
              <option v-for="user in users" :key="user.userId" :value="user.userId">
                {{ user.username }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="subAccountName">子账户名称</label>
            <input v-model="subAccountForm.accountName" id="subAccountName" required />
          </div>
          <div class="form-group">
            <label for="accountNumber">子账户号码</label>
            <input v-model="subAccountForm.accountNumber" id="accountNumber" required />
          </div>
          <div class="form-group">
            <label for="cardType">卡类型</label>
            <select v-model="subAccountForm.cardType" id="cardType" required>
              <option v-for="type in cardTypes" :key="type.value" :value="type.value">
                {{ type.label }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="balance">余额</label>
            <input v-model="subAccountForm.balance" id="balance" required pattern="\d+(\.\d{2})?" />
          </div>
          <div class="form-actions">
            <button type="submit" class="submit-btn">保存</button>
            <button type="button" @click="closeModal" class="cancel-btn">取消</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 编辑子账户用户模态框 -->
    <div v-if="showEditUsersModal" class="modal">
      <div class="modal-content">
        <h2>编辑子账户用户</h2>
        <form @submit.prevent="saveSubAccountUsers">
          <div class="form-group">
            <label for="users">关联用户</label>
            <select v-model="selectedUsers" id="users" multiple required>
              <option v-for="user in users" :key="user.userId" :value="user.userId">
                {{ user.username }}
              </option>
            </select>
          </div>
          <div class="form-actions">
            <button type="submit" class="submit-btn">保存</button>
            <button type="button" @click="closeEditUsersModal" class="cancel-btn">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.accounts-view {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.header h1 {
  margin: 0;
  color: #333;
}

.create-btn {
  background-color: #2196f3;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.create-btn:hover {
  background-color: #1976d2;
}

.debug-btn {
  background-color: #ff9800;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
}

.debug-btn:hover {
  background-color: #f57c00;
}

.filter {
  margin-bottom: 20px;
}

.filter label {
  margin-right: 10px;
}

.accounts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.account-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.account-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.account-header h3 {
  margin: 0;
  color: #333;
}

.account-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  background-color: #2196f3;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.action-btn:hover {
  background-color: #1976d2;
}

.action-btn.danger {
  background-color: #f44336;
}

.action-btn.danger:hover {
  background-color: #d32f2f;
}

.account-info p {
  margin: 5px 0;
  color: #666;
}

.account-info ul {
  margin: 10px 0;
  padding-left: 20px;
}

.account-info li {
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.no-accounts {
  text-align: center;
  color: #666;
  padding: 20px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  width: 100%;
  max-width: 500px;
}

.modal-content h2 {
  margin: 0 0 20px;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.submit-btn {
  background-color: #2196f3;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #1976d2;
}

.cancel-btn {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #d32f2f;
}

.loading {
  text-align: center;
  padding: 40px;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #2196f3;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error {
  color: #f44336;
  text-align: center;
  padding: 20px;
}
</style>