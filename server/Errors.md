# @Data注解在多对多关系中的异常调用

- @Data注解会自动生成@ToString @EqualsAndHashCode,在多对多关系时,
  会出现交叉引用的情况,造成了 @ToString 和 @EqualsAndHashCode
  导致的循环引用（如 toString() 导致的 StackOverflowError)
- 因此将 @Data 改为 @Getter 和 @Setter ,必要时手动@Override toString()

# JPA中的@ManyToMany 注解定义双向关系不会自动同步内存中的双向关系状态问题

- JPA 仅管理数据库层面的关联，不会自动同步内存中的双向关系。Hibernate（或其他 JPA 实现）不会隐式维护双向关联的内存状态
- 为了确保内存中的双向关系一致，应在实体类中定义同步方法，显式更新双方的关联集合。这是实体状态管理的一部分，符合领域驱动设计（DDD）的原则
- 即使在实体类中定义同步方法,双向关系依旧存在极大的问题
    - 将h2测试环境转为MySQL实际测试环境---无效,排除h2问题
    - 分析业务逻辑和表结构,尝试取消双向关系,只在 SubAccount 维护 users，移除 User.subAccounts
      --- 有效,已成功解决问题

# Mapper字段不匹配

- 在建表时,一些字段命名为默认方式,如id,name,但在实体类和DTO,需要非常注意字段的映射关系,
  整体可以采取特定的命名规范如 表名_字段名 避免重复,不清晰的字段属性导致后端需要额外规范,特别是现
  有的表使用了混合的字段命名方式,如type_id 和 user_id 等字段的命名则没有统一规则,容易产生歧义
- 尝试重构表,调整相关代码函数问题

# 自动生成的MapperImpl注解缺失

- 通过@Mapper注解自动生成的MapperImpl缺失注解@Component

# 使用Lombok和MapStruct注解的异常问题

- 同时引入了lombok和mapstruct时, 会出现No property named “xxx” exists in source parameter(s)错误
- 解决办法为在annotationProcessorPaths 中，配置lombok

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>${java.version}</source>
        <target>${java.version}</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.24</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

# 全局响应与Jwt权限管理异常

- 如果遇到权限问题,异常会直接在Jwt过滤器链中抛出而不是是进入全局响应
- 为 AccessDeniedException 设置全局处理器并添加到SecurityConfig中

# 枚举类的序列化和反序列化

- Hibernate 无法直接处理枚举类型, 在service中需要使用string类型并用String.valueOf()封装字段
- 新增JacksonConfig配置类
- 添加枚举类中的序列化方法

```java
private final String value;

Period(String value) {
    this.value = value;
}

@JsonValue
public String getValue() {
    return value;
}

@JsonCreator
public static Period fromValue(String value) {
    log.debug("Deserializing Period from value: {}", value);
    if (value == null) {
        log.error("Period value is null");
        throw new IllegalArgumentException("Period cannot be null");
    }
    try {
        return valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
        log.error("Invalid period value: {}", value, e);
        throw new IllegalArgumentException("Invalid period value: " + value, e);
    }
}
```

- 考虑为其他枚举类规范化
- 考虑提取[AccountDTO.java](src/main/java/com/as/server/dto/accounts/AccountDTO.java)中的枚举
- 提取CardType注解,统一放在enums中,导入CardType枚举类到DTO和request中统一管理

# @MockBean与实际真实业务逻辑不一致
- 具体问题: AccountControllerTest中的createAccount_invalidTypeId_throwsBadRequest
试图获取一个无效的typeId,是否抛出异常,但实际没有抛出,正常进入创建逻辑
- 问题描述:UserControllerTest 在使用 @MockBean EntityMapper
  的情况下可以通过，而 AccountControllerTest 在类似配置下（使用
  @MockBean EntityMapper）无法通过
  createAccount_invalidTypeId_throwsBadRequest 测试
- 原因:在 AccountControllerTest 中，@MockBean EntityMapper
  导致 EntityMapperImpl 的 accountType 方法未被调用测试未为 when(entityMapper.toAccount(...)) 配置具体行为，Mockito 默认返回
  null
  或无效对象，绕过了 typeId = 999 的验证逻辑（accountTypeRepository.findById(999)）,
  这使得 AccountService.create 被调用，返回 HTTP 201，而不是抛出 IllegalArgumentException 并返回 HTTP 400
- 解决方法:移除 @MockBean EntityMapper 后，真实 EntityMapperImpl 执行 accountType 方法，正确抛出异常，测试通过
- 其他测试类EntityMapper使用方式与AccountControllerTest不一致使得其他测试可以通过
- 原因是其他测试如User中,使用mapper的toUser 和 toUserDTO 是简单的数据映射，不涉及外部依赖（如
  AccountTypeRepository）或复杂验证逻辑。
  它们不抛出异常，仅执行字段赋值

# OffsetDateTime 序列化失败
- Jackson 默认不支持 java.time.OffsetDateTime
- 在ObjectMapper 注册 JavaTimeModule
```java
@Bean
public ObjectMapper objectMapper() {
  return JsonMapper.builder()
          .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS) // 启用枚举不区分大小写
          .addModules(new JavaTimeModule()) // 注册 JavaTimeModule，用于处理日期和时间
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 禁用默认的将日期时间序列化为时间戳
          .defaultTimeZone(TimeZone.getTimeZone("UTC"))
          .build();
}
```

# springdoc-openapi-ui 403问题:
- 不能单独引入springdoc-openapi-starter-webmvc-ui,引入完整的springdoc-openapi-ui