### 一、Skill 说明
本文档用于约束 sense-x-server 项目的后端文件目录结构、文件命名、模块归属组织方式。
AI 在创建、修改或补充后端代码时，必须遵循本 Skill。

核心原则： 
类按应用和业务模块归类，禁止随意创建目录和文件。

### 二、项目整体目录结构
服务端代码放置在sense-x-server工程下，是一个多模块项目：
- sense-server/sense-base: 基础模块，主要是用户体系、基础管理功能。
- sense-server/sense-base/src/main/java/com/sense/app/core: 核心配置后端代码。
- sense-server/sense-base/src/main/java/com/sense/app/base: 基础功能后端代码。
- sense-server/sense-starter: 启动模块，主要是启动类及配置文件，一般无需改动。

### 三、Java类定义及命名规范
每个功能/表应按以下规则创建相关业务类，标准：
- 一个Controller类，用于暴露 API 接口，存放路径：sense-x-server/sense-应用名/src/main/java/com/sense/app/应用名/模块名/web/功能名Controller.java
- 一个Service类，用于业务逻辑处理，存放路径：sense-x-server/sense-应用名/src/main/java/com/sense/app/应用名/模块名/service/功能名Service.java
- 一个Mapper 类，用于与数据库交互，存放路径：sense-x-server/sense-应用名/src/main/java/com/sense/app/应用名/模块名/mapper/功能名Mapper.java
- 一个Domain 类，用于数据库映射为实体，存放路径：sense-x-server/sense-应用名/src/main/java/com/sense/app/应用名/模块名/domain/功能名.java

#### 3.1 Controller类
- 依赖注入注解 用 @Autowired
- 所有接口返回统一使用：`ResponseEntity<T>`
- 分页的返回泛型：`ResponseEntity<List<T>>`
- 成功：`return new ResponseEntity(data, HttpStatus.OK)`
- 失败：业务逻辑中抛出异常`throw new BizException(errorMsg)`
- 分页参数：`pageNum`、`pageSize`
- 删除参数：为ID数组，支持删除多个
- 查询时，禁止在Controller中将接收到的筛选参数合并为query对象，应直接全部挨个作为参数传入Service

#### 3.2 Service类
- 必须在类上添加事务注解: @Transactional(rollbackFor = Exception.class)
- 基础操作应直接MybatisPlus提供的baseMapper.selectPage()、getOne()、save()、updateById()、removeById()等等，直接操作业务，简单的逻辑处理不需要写SQL
- 简单的join操作，应该使用new MPJLambdaWrapper<主表>().selectAll(主表).selectAs(从表::字段, 主表::新字段名).leftJoin(从表, 从表::字段, 主表::字段)进行查询，避免进行内存中没必要的字段填充
- 复杂的join操作，允许将数据查询到 Service 层在内存中处理（生产环境 64GB 内存，可支撑），禁止在 SQL 中写过度复杂逻辑
- 只写一个Service类即可 不需要写接口，确保Controller直接调用Service类
- 方法上方编写方法注释
- **使用mybatis查询时，应尽量避免先独立写一行query = new LambdaQueryWrapper()，直接使用selectList(new LambdaQueryWrapper<T>().eq())这种直接写到方法括号内new，保持代码精简**

#### 3.3 Mapper类
- 每个功能/表应创建一个 Mapper 类，并继承 `MPJBaseMapper<T>`，用于使用 MybatisPlus-Join 的能力与数据库交互。
- 若有复杂查询，则应在 Mapper 类中，创建对应的方法，功能名 Mapper.xml 实现，存放路径：`sense-server/src/main/resources/mapper/应用名/模块名/功能名Mapper.xml`
- 满足以下任一情况，允许写 XML 自定义 SQL：
    - 多表关联后需要一次性返回大量字段，内存组装效率低；
    - 需要分组统计、sum/count/avg等聚合查询；
    - 关联查询条件复杂，Service 内存组装代码冗余；
    - 报表类、统计类、列表展示类查询
- 没有做特殊说明的表，均区分租户。不区分租户的表，在Mapper类上添加注解: @InterceptorIgnore(tenantLine = "true")

#### 3.4 Domain类
- 所有的Domain类都应该继承BaseDomain.java，即所有表都有以下字段: create_user、create_date、update_user、update_date
- 每个功能/表应创建一个 Domain 类，用于数据库映射为实体
- 使用 Lombok 简化代码（@Builder/@NoArgsConstructor/@AllArgsConstructor/@Data 等），统一用法

### 四、方法命名约束
- 分页查询: pageData(分页参数, 筛选参数1, 筛选参数2)
- 不分页查询: queryData(筛选参数1, 筛选参数2)
- 树形查询: queryTree(筛选参数1, 筛选参数2)
- 查询详情: queryDetail(ID)
- 提交数据: submitData(data)
- 删除数据: deleteData(id集合)
- 导入数据: importData()
- 导出数据: exportData()

### 五、API命名约束
编写Controller接口时，接口的命名遵循以下规则：
- 分页查询: GET-/api/应用名/模块名/功能名/page
- 不分页查询: GET-/api/应用名/模块名/功能名/query
- 树形查询: GET-/api/应用名/模块名/功能名/tree
- 查询详情: GET-/api/应用名/模块名/功能名/detail
- 提交数据: POST-/api/应用名/模块名/功能名/submit
- 删除数据: POST-/api/应用名/模块名/功能名/delete
- 导入数据: POST-/api/应用名/模块名/功能名/import
- 导出数据: GET-/api/应用名/模块名/功能名/export

### 六、类注释与方法注释
编写类及方法时，应在上方添加注释，标准注释格式：
```js
/**
 * 应用名-模块名-功能名-描述
 * @author AI生成
 * @date 当前时间
 */
```
- author: 标明你是哪个AI，不超过6个字
- date: 标明当前时间，格式为yyyy-MM-dd


### 七、一个基础功能示例
#### Domain
````java
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("core_sys_role")
public class CoreSysRole extends BaseDomain {
    // ID
    @TableId
    private Long id;

    // 编码
    @TableField
    private String roleCode;

    // 名称
    @TableField
    private String roleName;

    // 租户ID
    @TableField
    private Long tenantId;
}
````

#### Mapper
```java
@Mapper
public interface CoreSysRoleMapper extends MPJLambdaWrapper<CoreSysRole> {
}
```

#### Service
```java
@Service
public class CoreSysRoleService extends ServiceImpl<CoreSysRoleMapper, CoreSysRole> {

  @Autowired
  private CoreSysUserRoleMapper userRoleMapper;

  @Autowired
  private CoreSysUserMapper userMapper;

  @Autowired
  private CoreSysUserService userService;

  /**
   * queryRoles : 查询角色信息
   */
  public List<SysRole> pageData(Page mybatisPage){
    Page<SysRole> results = page(mybatisPage,new QueryWrapper<SysRole>());
    return results.getRecords();
  }

  /**
   * submitRole : 提交角色信息
   */
  public void submitData(SysRole data){
    saveOrUpdate(data);
  }

  /**
   * deleteRoles : 删除角色信息
   */
  public void deleteData(List<Long> ids){
    // 删除角色-用户分配信息
    userRoleMapper.delete(new QueryWrapper<SysUserRole>().in("role_id", ids));

    // 删除角色信息
    removeByIds(ids);
  }
}
```

#### Controller

```java
@RestController
@RequestMapping("/api/core/sys/role")
public class SysRoleController {

  @Autowired
  private CoreSysRoleService service;

  /**
   * 分页查询角色信息
   */
  @GetMapping("/page")
  public ResponseEntity<List<SysRole>> pageData(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
    Page mybatisPage = PageUtil.getPage(page, pageSize);
    return new ResponseEntity(service.pageData(mybatisPage), PageUtil.getTotalHeader(mybatisPage),HttpStatus.OK);
  }

  /**
   * 提交角色信息
   */
  @PostMapping("/submit")
  public ResponseEntity submitData(@RequestBody SysRole data){
    service.submitData(data);
    return ResponseEntity.ok(true);
  }

  /**
   * 删除角色信息
   */
  @PostMapping("/delete")
  public ResponseEntity deleteData(@RequestBody List<Long> ids){
    service.deleteData(ids);
    return ResponseEntity.ok(true);
  }
}
```