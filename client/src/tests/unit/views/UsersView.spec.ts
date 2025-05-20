import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import UsersView from '@/views/UsersView.vue';
import { createTestingPinia } from '@pinia/testing';
import { useUsersStore } from '@/store/modules/users';
import type { UserDTO } from '@/api/models/user/user-dto';
// Mock users data
const mockUsers: UserDTO[] = [
  {
    userId: 1,
    username: 'admin',
    role: 'ADMIN'
  },
  {
    userId: 2,
    username: 'user',
    role: 'USER'
  }
];

describe('UsersView', () => {
  let wrapper: any;
  let usersStore: any;

  beforeEach(() => {
    const pinia = createTestingPinia({
      createSpy: vi.fn,
      initialState: {
        users: {
          users: [],
          loading: false,
          error: null
        }
      }
    });

    usersStore = useUsersStore(pinia);
    usersStore.fetchUsers = vi.fn().mockResolvedValue(mockUsers);
    usersStore.deleteUser = vi.fn().mockResolvedValue(undefined);
    usersStore.updateUser = vi.fn().mockResolvedValue(undefined);
    usersStore.createUser = vi.fn().mockResolvedValue(undefined);

    wrapper = mount(UsersView, {
      global: {
        plugins: [pinia]
      }
    });
  });

  it('should render users list', async () => {
    usersStore.users = mockUsers;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.users-list').exists()).toBe(true);
  });

  it('should fetch users on mount', async () => {
    await wrapper.vm.$nextTick();
    expect(usersStore.fetchUsers).toHaveBeenCalled();
  });

  it('renders user list correctly', async () => {
    usersStore.users = mockUsers;
    await wrapper.vm.$nextTick();
    expect(wrapper.find('h1').text()).toBe('用户管理');
    const userCards = wrapper.findAll('.user-card');
    expect(userCards.length).toBe(2);
    expect(userCards[0].find('h3').text()).toBe('admin');
    expect(userCards[1].find('h3').text()).toBe('user');
  });

  it('shows edit dialog when clicking edit button', async () => {
    usersStore.users = mockUsers;
    await wrapper.vm.$nextTick();
    const editButtons = wrapper.findAll('.user-card button.edit');
    expect(editButtons.length).toBeGreaterThan(0);
    await editButtons[0].trigger('click');
    await wrapper.vm.$nextTick();
    expect(wrapper.find('.dialog').exists()).toBe(true);
    expect(wrapper.find('.dialog h3').text()).toBe('编辑用户');
  });

  it('deletes user when clicking delete button', async () => {
    usersStore.users = mockUsers;
    await wrapper.vm.$nextTick();
    const deleteButtons = wrapper.findAll('.user-card button.delete');
    expect(deleteButtons.length).toBeGreaterThan(0);
    window.confirm = vi.fn(() => true);
    await deleteButtons[0].trigger('click');
    expect(usersStore.deleteUser).toHaveBeenCalledWith(1);
  });

  it('handles error state correctly', async () => {
    usersStore.error = '加载失败';
    await wrapper.vm.$nextTick();
    const errorElement = wrapper.find('.error-message');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toBe('加载失败');
  });

  it('displays user roles correctly', async () => {
    usersStore.users = mockUsers;
    await wrapper.vm.$nextTick();
    const roleElements = wrapper.findAll('.user-card p');
    expect(roleElements[0].text()).toBe('角色: ADMIN');
    expect(roleElements[1].text()).toBe('角色: USER');
  });

  it('validates user form', async () => {
    usersStore.users = mockUsers;
    await wrapper.vm.$nextTick();

    const editButton = wrapper.find('.user-card button.edit');
    await editButton.trigger('click');
    await wrapper.vm.$nextTick();

    const usernameInput = wrapper.find('input');
    await usernameInput.setValue('');

    const form = wrapper.find('form');
    await form.trigger('submit.prevent');
    await wrapper.vm.$nextTick();

    const errorElement = wrapper.find('.error-message');
    expect(errorElement.exists()).toBe(true);
    expect(errorElement.text()).toBe('用户名不能为空');
  });
});