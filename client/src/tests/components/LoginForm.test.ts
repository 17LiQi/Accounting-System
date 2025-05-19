import { mount } from '@vue/test-utils';
import { nextTick } from 'vue';
import LoginForm from '@/components/auth/LoginForm.vue';
import { authService } from '@/api/services/auth';

jest.mock('@/api/services/auth');
jest.mock('vue-router', () => ({
  useRouter: () => ({
    push: jest.fn()
  })
}));

describe('LoginForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('应该正确渲染登录表单', () => {
    const wrapper = mount(LoginForm);
    
    expect(wrapper.find('input[type="text"]').exists()).toBe(true);
    expect(wrapper.find('input[type="password"]').exists()).toBe(true);
    expect(wrapper.find('button[type="submit"]').exists()).toBe(true);
  });

  it('登录成功时应该保存token并跳转', async () => {
    const mockResponse = {
      token: 'test-token',
      role: 'ADMIN'
    };
    
    (authService.login as jest.Mock).mockResolvedValue(mockResponse);
    
    const wrapper = mount(LoginForm);
    
    await wrapper.find('input[type="text"]').setValue('admin');
    await wrapper.find('input[type="password"]').setValue('password');
    await wrapper.find('form').trigger('submit');
    
    await nextTick();
    
    expect(authService.login).toHaveBeenCalledWith({
      username: 'admin',
      password: 'password'
    });
    expect(localStorage.getItem('token')).toBe('test-token');
    expect(localStorage.getItem('userRole')).toBe('ADMIN');
  });

  it('登录失败时应该显示错误信息', async () => {
    const errorMessage = '登录失败';
    (authService.login as jest.Mock).mockRejectedValue({
      response: {
        data: {
          message: errorMessage
        }
      }
    });
    
    const wrapper = mount(LoginForm);
    
    await wrapper.find('input[type="text"]').setValue('wrong');
    await wrapper.find('input[type="password"]').setValue('wrong');
    await wrapper.find('form').trigger('submit');
    
    await nextTick();
    
    expect(wrapper.find('.error-message').text()).toBe(errorMessage);
  });
}); 