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