// eslint.config.js
import vue from 'eslint-plugin-vue';
import typescriptEslint from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';

export default [
  {
    files: ['**/*.{js,ts,vue}'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 2021,
        sourceType: 'module',
      },
    },
    plugins: {
      vue,
      '@typescript-eslint': typescriptEslint,
    },
    rules: {
      'indent': ['error', 2],
      'semi': ['error', 'always'],
      'vue/multi-word-component-names': 'off',
      '@typescript-eslint/no-unused-vars': 'error',
    },
  },
  vue.configs['vue3-essential'], // 引入 Vue 3 推荐规则
];