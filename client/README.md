# 会计系统前端

这是一个基于 Vue 3 + TypeScript + Vite 构建的会计系统前端项目。

## 功能特性

- 账户管理
- 交易记录
- 数据可视化
- 响应式设计

## 技术栈

- Vue 3
- TypeScript
- Vite
- Vue Router
- Pinia
- Axios

## 开发环境要求

- Node.js >= 16.0.0
- pnpm >= 7.0.0

## 安装

```bash
# 安装依赖
pnpm install
```

## 开发

```bash
# 启动开发服务器
pnpm run dev
```

## 构建

```bash
# 构建生产版本
pnpm run build
```

## 预览

```bash
# 预览生产构建
pnpm run preview
```

## 测试

```bash
# 运行单元测试
pnpm run test
```

## 代码检查

```bash
# 运行 ESLint
pnpm run lint
```

## 项目结构

```
src/
  ├── api/          # API 服务
  ├── components/   # 组件
  ├── models/       # 数据模型
  ├── router/       # 路由配置
  ├── views/        # 页面视图
  ├── App.vue       # 根组件
  └── main.ts       # 入口文件
```

## 环境变量

创建 `.env` 文件并设置以下变量：

```
VITE_API_BASE_URL=http://localhost:3000
```


