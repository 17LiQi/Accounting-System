// src/env.d.ts
/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string;
  readonly VITE_MOCK_ENABLED: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}

// 添加 Vite 模块运行器的类型声明
declare module 'vite' {
  interface ModuleRunnerImportMeta extends ImportMeta {
    env: ImportMetaEnv;
  }
}