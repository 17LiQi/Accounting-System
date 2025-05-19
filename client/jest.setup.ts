import { config } from '@vue/test-utils';
import { createPinia } from 'pinia';

// 配置全局插件
config.global.plugins = [createPinia()];

// 配置全局组件
config.global.components = {};

// 配置全局 mocks
config.global.mocks = {};

// 配置全局 stubs
config.global.stubs = {};

// 配置全局 directives
config.global.directives = {};

// 配置全局 provide
config.global.provide = {};

// 配置全局 mixins
config.global.mixins = []; 