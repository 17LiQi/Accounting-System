import { mount } from '@vue/test-utils';
import AccountCard from '@/components/business/AccountCard.vue';
describe('AccountCard', () => {
    it('renders account name and balance', () => {
        const account = { id: '1', name: 'Test', balance: 100 };
        const wrapper = mount(AccountCard, { props: { account } });
        expect(wrapper.text()).toContain('Test');
        expect(wrapper.text()).toContain('100');
    });
});