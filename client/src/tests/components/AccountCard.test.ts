// tests/components/AccountCard.test.ts
import { mount } from '@vue/test-utils';
import AccountCard from '@/components/business/AccountCard.vue';
describe('AccountCard', () => {
    it('渲染账户名称和类型', () => {
        const account = {
            accountId: 1,
            accountName: '银行账户',
            accountType: { typeId: 1, typeName: 'BANK' },
            balance: 1000
        };
        const wrapper = mount(AccountCard, { props: { account } });
        expect(wrapper.text()).toContain('银行账户');
        expect(wrapper.text()).toContain('BANK');
    });
});