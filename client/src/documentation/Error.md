# 使用openapi构建命令问题
- 不推荐使用如下类似的命令
```bash
openapi-generator-cli generate \
  -i ../your-api-docs.yaml \
  -g typescript-axios \
  -c openapi-config.yaml
```
- 容易出现:
  - 无法解析api,生成的api会集中在一个文件中
  - 生成models异常
- 建议编写openapi配置,如your-openapi-config.yaml:
```yaml
generatorName: typescript-axios
outputDir: ./src/api # 可自定义输出路径
inputSpec: ../your-api-docs.yaml
additionalProperties:
  withSeparateModelsAndApi: true
  apiPackage: apis
  modelPackage: models
  useAxios: true

  basePath: http://localhost:8080
```
- 再运行该配置:
```bash
openapi-generator-cli generate -c your-openapi-config.yaml
```

# 环境变量类型声明异常
- 问题:
```bash
pnpm tsc -noEmit
node_modules/.pnpm/vite@6.3.5_@types+node@22.15.18/node_modules/vite/dist/node/module-runner.d.ts:149:11 - error TS2430: Interface 'ModuleRunnerImportMeta' incorrectly extends interface 'ImportMeta'.
  Types of property 'env' are incompatible.
    Type 'ImportMetaEnv' is missing the following properties from type 'ImportMetaEnv': VITE_API_BASE_URL, VITE_MOCK_ENABLED

149 interface ModuleRunnerImportMeta extends ImportMeta {
              ~~~~~~~~~~~~~~~~~~~~~~


Found 1 error in node_modules/.pnpm/vite@6.3.5_@types+node@22.15.18/node_modules/vite/dist/node/module-runner.d.ts:149
```
- 解决:
  - 删除现有.env重新创建
  - 在env.d.ts添加 Vite 模块运行器的类型声明
```ts
declare module 'vite' {
  interface ModuleRunnerImportMeta extends ImportMeta {
    env: ImportMetaEnv;
  }
}
```

# 配置文件问题
- vue的配置文件非常复杂,远在springboot的maven和pom之上
- 稍后整理配置文件,考虑单独写一个说明文档