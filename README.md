# 🍜 校园食堂外卖点餐平台

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MyBatis-2.2.0-blue" alt="MyBatis">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-5.0+-red" alt="Redis">
  <img src="https://img.shields.io/badge/Vue-2.x-4FC08D" alt="Vue">
  <img src="https://img.shields.io/badge/微信小程序-原生-green" alt="WeChat Mini Program">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</p>

---

## 📖 项目简介

本项目是一套 **前后端分离** 的校园食堂线上点餐全栈系统，分为 **「学生微信小程序点餐端」** 与 **「食堂商家 Web 管理后台」**，适配高校食堂运营场景，解决线下排队拥堵、订单管理繁琐、经营数据无统计等核心痛点。

- 🧑‍🎓 **学生端**：微信小程序浏览菜品、加购下单、微信支付、订单追踪、再来一单
- 👨‍🍳 **商家端**：Vue + ElementUI 管理后台，菜品/套餐管理、订单全流程处理、WebSocket 实时来单播报、数据可视化报表导出、员工权限管控

---

## 🏗 系统架构

```
┌─────────────────────┐     ┌─────────────────────┐
│  微信小程序 (学生端)  │     │  Vue Admin (商家端)   │
│  Native Mini Program │     │  Vue 2.x + ElementUI │
└──────────┬──────────┘     └──────────┬──────────┘
           │ HTTP/HTTPS                │ HTTP/HTTPS
           │ JWT Token                 │ JWT Token
           ▼                           ▼
┌─────────────────────────────────────────────────┐
│                   Nginx (反向代理)                │
│           静态资源托管 · 接口转发 · 负载均衡        │
└──────────────────────┬──────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────┐
│           SpringBoot 2.7.x (后端服务)            │
│  ┌──────────────────────────────────────────┐   │
│  │         Controller Layer (REST API)       │   │
│  │   ┌─────────────┐  ┌──────────────────┐  │   │
│  │   │ admin/       │  │ user/            │  │   │
│  │   │ (管理端接口)  │  │ (小程序端接口)    │  │   │
│  │   └─────────────┘  └──────────────────┘  │   │
│  ├──────────────────────────────────────────┤   │
│  │         Interceptor Layer                │   │
│  │   JwtTokenAdminInterceptor /              │   │
│  │   JwtTokenUserInterceptor (双端JWT鉴权)   │   │
│  ├──────────────────────────────────────────┤   │
│  │         Service Layer (业务逻辑)          │   │
│  │   OrderService · DishService · ...       │   │
│  ├──────────────────────────────────────────┤   │
│  │         Mapper Layer (数据访问)           │   │
│  │   MyBatis XML + PageHelper 分页           │   │
│  ├──────────────────────────────────────────┤   │
│  │  AOP · WebSocket · Task · Exception      │   │
│  │  自动填充 · 实时推送 · 定时任务 · 全局异常  │   │
│  └──────────────────────────────────────────┘   │
└──────┬──────────────┬──────────────┬────────────┘
       │              │              │
       ▼              ▼              ▼
┌──────────┐  ┌────────────┐  ┌──────────────┐
│  MySQL   │  │   Redis    │  │ 阿里云 OSS    │
│  8.0     │  │   缓存/锁   │  │  菜品图片存储  │
└──────────┘  └────────────┘  └──────────────┘
```

---

## 📁 项目结构

> **📦 仓库说明**：本仓库为 **后端 Java 服务** 代码（Maven 多模块）。前端项目位于独立仓库 — Vue 管理后台 (`canteen-admin-vue`) 与微信小程序 (`canteen-miniapp`)。

```
campus-canteen-takeout/  (本仓库 - 后端服务)
│
├── sky-common/                    # 公共模块
│   └── src/main/java/com/sky/
│       ├── constant/              # 常量定义 (JWT声明、消息、状态等)
│       ├── context/               # ThreadLocal 登录上下文 (BaseContext)
│       ├── enumeration/           # 枚举类 (操作类型)
│       ├── exception/             # 自定义业务异常 (11种异常类型)
│       ├── json/                  # Jackson 序列化配置
│       ├── properties/            # 配置属性类 (JWT/OSS/微信支付)
│       ├── result/                # 统一响应模型 (Result / PageResult)
│       └── utils/                 # 工具类 (JWT/OSS/HTTP/微信支付)
│
├── sky-pojo/                      # 实体与DTO模块
│   └── src/main/java/com/sky/
│       ├── dto/                   # 数据传输对象 (20+ DTO)
│       ├── entity/                # 数据库实体 (11张表)
│       └── vo/                    # 视图对象 (17种 VO)
│
├── sky-server/                    # 服务端主模块
│   └── src/main/java/com/sky/
│       ├── SkyApplication.java    # 启动类 (@SpringBootApplication)
│       ├── annotation/            # 自定义注解 (@AutoFill)
│       ├── aspect/                # AOP 切面 (自动填充公共字段)
│       ├── config/                # 配置类 (OSS/Redis/WebMVC/WebSocket)
│       ├── controller/
│       │   ├── admin/             # 管理端接口 (10个Controller)
│       │   └── user/              # 用户端接口 (8个Controller)
│       ├── handler/               # 全局异常处理器
│       ├── interceptor/           # JWT 拦截器 (管理端/用户端)
│       ├── mapper/                # MyBatis Mapper 接口 (11个)
│       ├── service/
│       │   ├── impl/              # 业务逻辑实现 (10个Service)
│       │   └── *.java             # 业务接口定义
│       ├── task/                  # 定时任务 (超时订单处理)
│       └── websocket/             # WebSocket 服务 (订单实时推送)
│
│   └── src/main/resources/
│       ├── application.yml        # 主配置文件
│       ├── application-dev.yml    # 开发环境配置
│       └── mapper/                # MyBatis XML 映射文件 (11个)
│
├── pom.xml                        # Maven 父 POM
└── .gitignore
```

---

## ✨ 核心功能模块

### 🧑‍🎓 学生微信小程序端

| 模块 | 功能描述 |
|------|----------|
| 🏠 **首页** | 菜品分类浏览、热销菜品推荐、食堂公告展示 |
| 🛒 **购物车** | 菜品/套餐添加、数量增减、规格选择、批量结算 |
| 📍 **地址管理** | 自提地址新增/编辑/删除，多地址管理 |
| 💰 **下单支付** | 微信支付 V3 预下单、支付异步回调、下单幂等校验 |
| 📋 **订单中心** | 订单状态实时查看、历史订单分页、订单详情追踪 |
| ↩️ **订单操作** | 取消订单、申请退款、再来一单、催单提醒 |
| 👤 **个人中心** | 微信授权登录、个人信息、消费记录 |

### 👨‍🍳 食堂管理后台 (Vue + ElementUI)

| 模块 | 功能描述 |
|------|----------|
| 🔐 **权限管理** | 员工账号管理、角色分级权限、JWT 双端独立鉴权 |
| 🍗 **菜品管理** | 菜品分类维护、菜品 CRUD、规格口味管理、起售/停售、图片上传阿里云 OSS |
| 📦 **套餐管理** | 套餐组合配置、价格管理、上下架控制 |
| 🔔 **订单看板** | WebSocket 实时来单弹窗 + 语音播报提醒，无需手动刷新 |
| 📋 **订单处理** | 全流程处理：接单 → 出餐 → 完成，拒单/退款审核 |
| 📊 **数据大盘** | 今日营收/订单数概览、日/月营业额统计、订单统计 |
| 🏆 **销量排行** | 菜品销量 Top10 统计图表 |
| 📥 **报表导出** | Apache POI 导出营收报表、订单明细 Excel |
| ⚙️ **系统管理** | 系统参数配置、Redis 缓存清理、操作日志查看 |

---

## 🛠 技术栈

### 后端核心技术

| 技术 | 版本/说明 |
|------|----------|
| Spring Boot | 2.7.3 (核心框架) |
| MyBatis | 2.2.0 (ORM 持久层) |
| MySQL | 8.0 (关系型数据库) |
| Redis | 5.0+ (缓存 + 分布式锁) |
| Spring Cache | 声明式缓存管理 |
| Druid | 1.2.1 (数据库连接池) |
| JWT (jjwt) | 0.9.1 (双端无状态鉴权) |
| WebSocket | 原生 JSR 356 (实时订单推送) |
| Spring AOP | 切面自动填充公共字段 |
| Knife4j | 3.0.2 (在线 API 接口文档) |
| PageHelper | 1.3.0 (MyBatis 分页插件) |
| Apache POI | 3.16 (Excel 报表导出) |
| FastJSON | 1.2.76 (JSON 序列化) |
| Lombok | 1.18.36 (简化代码) |
| 微信支付 V3 | wechatpay-apache-httpclient 0.4.8 |

### 前端技术

| 端 | 技术栈 |
|----|--------|
| 管理后台 | Vue 2.x · Element UI · Axios |
| 学生端 | 原生微信小程序 · WeUI |

### 第三方服务 & 中间件

| 服务 | 用途 |
|------|------|
| 阿里云 OSS | 菜品/套餐图片云存储 |
| Nginx | 反向代理 · 静态资源托管 · 负载均衡 |
| 微信支付 API V3 | 支付下单 · 异步回调 · 退款 |

### 运维部署

| 环境 | 说明 |
|------|------|
| 操作系统 | Linux / CentOS 7+ |
| 容器化 | Docker (可选) |
| Web 服务器 | Nginx (反向代理 + 动静分离) |

---

## 🔥 核心技术亮点

### 1. 🔐 双端 JWT 无状态鉴权体系

区分 **食堂管理员** 与 **小程序学生用户** 两套独立的 Token 签发逻辑：

- `JwtTokenAdminInterceptor` — 拦截 `/admin/**` 路径，从请求头提取 `token`，校验 `admin-secret-key` 签发的 JWT
- `JwtTokenUserInterceptor` — 拦截 `/user/**` 路径，从请求头提取 `authentication`，校验 `user-secret-key` 签发的 JWT
- `BaseContext` 基于 `ThreadLocal` 存储当前登录用户 ID，线程隔离，安全可靠
- 未登录返回 `401`，越权访问自动拦截

### 2. ⚡ Redis + SpringCache 缓存高并发优化

- 声明式缓存 (`@CacheEvict`) 加速菜品列表、分类、热销榜单等高频查询
- 菜品变更时主动清理 `dish_*` 相关的 Redis 缓存，保证数据一致性
- 合理设置过期淘汰策略，防止缓存穿透和雪崩
- Setmeal 模块使用 SpringCache 注解，Dish 模块手动管理 Redis 缓存

### 3. 🔔 WebSocket 实时订单推送播报

- 后台管理端与服务器建立 WebSocket 长连接 `/ws/{sid}`
- 用户支付成功后自动推送 JSON 消息至所有已连接管理端
- 前端解析 `type` 字段：`1` = 来单提醒 → 弹窗 + 语音播报；`2` = 催单提醒
- 无需轮询刷新页面，大幅提升食堂接单效率

### 4. 💳 微信支付 V3 完整业务闭环

- 预下单 → 支付异步回调 → 订单状态更新 → 退款申请
- 回调请求签名校验，防止伪造回调
- 订单状态幂等更新，避免重复扣款和数据错乱
- **个人开发者模式**：不配置微信支付密钥时，前端直接跳转支付成功页面，后端 `catch` 退款异常后继续更新订单状态，保证业务流程完整可演示

### 5. 🧩 AOP 切面 + 自定义注解自动填充

- 自定义 `@AutoFill` 注解标记 Mapper 方法，指定 `INSERT` 或 `UPDATE` 操作类型
- `AutoFillAspect` 切面通过反射自动为 `createTime` / `updateTime` / `createUser` / `updateUser` 赋值
- 消除业务层重复赋值代码，代码更简洁、可维护性更高

### 6. 🔒 并发安全控制

- **Redis 分布式锁**：下单流程控制并发，防止同一用户重复提交
- **数据库乐观锁**：订单状态流转增加版本号校验，防止并发更新覆盖
- **订单状态机**：严格校验状态流转路径（待付款→待接单→已接单→派送中→已完成/已取消）

### 7. ⏰ 定时任务自动化处理

- **每分钟**：扫描超时 15 分钟未支付的订单 → 自动取消
- **每天凌晨 1 点**：处理长期处于「派送中」状态的订单 → 自动标记完成

### 8. 🚦 全局异常统一处理

- `GlobalExceptionHandler` 配合 `@RestControllerAdvice` 统一拦截所有业务异常
- 自定义 **11 种业务异常**（登录失败、账号锁定、订单异常、购物车异常等）
- `SQLIntegrityConstraintViolationException` 特殊处理，解析唯一约束冲突返回友好提示

### 9. 🌐 Nginx 前后端分离线上部署

- 静态资源长期缓存（JS/CSS/图片设置强缓存）
- 后端接口反向代理，消除跨域问题
- 支持多服务实例负载均衡
- 动静分离，提升页面访问速度

---

## 📊 订单状态流转

```
用户下单
  │
  ▼
┌──────────────┐     超时15分钟      ┌──────────┐
│  1. 待付款    │ ─────────────────▶ │  6. 已取消 │
└──────┬───────┘                    └──────────┘
       │ 支付成功
       ▼
┌──────────────┐     商家拒单        ┌──────────┐
│  2. 待接单    │ ─────────────────▶ │  6. 已取消 │
└──────┬───────┘    (自动退款)       └──────────┘
       │ 商家接单
       ▼
┌──────────────┐     用户取消        ┌──────────┐
│  3. 已接单    │ ─────────────────▶ │  6. 已取消 │
└──────┬───────┘    (自动退款)       └──────────┘
       │ 商家派送
       ▼
┌──────────────┐     凌晨1点自动      ┌──────────┐
│  4. 派送中    │ ─────────────────▶ │  5. 已完成 │
└──────┬───────┘                    └──────────┘
       │ 商家确认完成
       ▼
┌──────────────┐
│  5. 已完成    │
└──────────────┘
```

---

## 🗄 数据库 ER 图

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────┐
│   Employee   │     │      Orders       │     │    User     │
│  (员工表)    │     │     (订单表)       │     │  (用户表)   │
├──────────────┤     ├───────────────────┤     ├─────────────┤
│ id           │     │ id                │     │ id          │
│ name         │     │ number (订单号)    │     │ openid      │
│ username     │     │ status (订单状态)  │     │ name        │
│ password     │     │ pay_status (支付)  │     │ phone       │
│ phone        │     │ amount (金额)      │     │ sex         │
│ sex          │     │ user_id (FK)      │     │ avatar      │
│ id_number    │     │ address_book_id   │     │ create_time │
│ status       │     │ order_time        │     └─────────────┘
│ create_time  │     │ checkout_time           ┌──────────────────┐
│ update_time  │     │ cancel_time       ┌─────│  ShoppingCart     │
│ create_user  │     │ delivery_time     │     │  (购物车表)       │
│ update_user  │     │ ...               │     ├──────────────────┤
└──────────────┘     └────────┬─────────┘     │ id               │
                              │                │ user_id          │
                              │ 1:N            │ dish_id / setmeal│
┌──────────────┐     ┌───────┴──────────┐     │ dish_flavor      │
│  OrderDetail │     │                  │     │ number           │
│ (订单明细表) │     │                  │     │ amount           │
├──────────────┤     │                  │     │ create_time      │
│ id           │     │                  │     └──────────────────┘
│ order_id(FK) │     │                  │
│ name         │     │                  │     ┌──────────────────┐
│ image        │     │                  │     │  AddressBook     │
│ dish_id      │     │                  │     │  (地址簿表)       │
│ setmeal_id   │     │                  │     ├──────────────────┤
│ dish_flavor  │     │                  │     │ id               │
│ number       │     │                  │     │ user_id          │
│ amount       │     │                  │     │ consignee (收货人)│
└──────────────┘     │                  │     │ phone            │
                     │                  │     │ detail (地址)    │
┌──────────────┐     │                  │     │ is_default       │
│    Dish      │     │                  │     └──────────────────┘
│  (菜品表)    │     │                  │
├──────────────┤     │                  │     ┌──────────────────┐
│ id           │     │                  │     │    Setmeal       │
│ name         │     │                  │     │   (套餐表)        │
│ category_id  │     │                  │     ├──────────────────┤
│ price        │◀────┘                  │     │ id               │
│ image        │                        │     │ name             │
│ status       │     ┌──────────────┐   │     │ category_id      │
│ create_time  │     │  DishFlavor  │   │     │ price            │
│ update_time  │     │ (菜品口味表)  │   │     │ image            │
└──────┬───────┘     ├──────────────┤   │     │ status           │
       │ 1:N         │ id           │   │     └────────┬─────────┘
       ▼             │ dish_id (FK) │   │              │ 1:N
┌──────────────┐     │ name         │   │              ▼
│  DishFlavor  │     │ value        │   │     ┌──────────────────┐
│  (菜品口味表) │     └──────────────┘   │     │  SetmealDish     │
└──────────────┘                        │     │ (套餐菜品关联表)  │
                                        │     ├──────────────────┤
┌──────────────┐                        │     │ id               │
│   Category   │                        │     │ setmeal_id (FK)  │
│  (分类表)    │                        │     │ dish_id          │
├──────────────┤                        │     │ name             │
│ id           │                        │     │ price            │
│ name         │                        │     │ copies           │
│ type (1菜品  │                        │     └──────────────────┘
│  2套餐)      │
│ sort         │
│ status       │
└──────────────┘
```

**共 11 张数据表**：`employee` · `user` · `category` · `dish` · `dish_flavor` · `setmeal` · `setmeal_dish` · `orders` · `order_detail` · `shopping_cart` · `address_book`

---

## 📡 API 接口一览

### 管理端接口 (`/admin/**`) — JWT 管理员鉴权

| Controller | 路径 | 功能 |
|------------|------|------|
| EmployeeController | `/admin/employee` | 员工登录/登出、增删改查、状态管理、密码修改 |
| CategoryController | `/admin/category` | 菜品/套餐分类 CRUD、分页查询、状态控制 |
| DishController | `/admin/dish` | 菜品 CRUD、口味管理、起售停售、分页查询 |
| SetmealController | `/admin/setmeal` | 套餐 CRUD、关联菜品配置、起售停售 |
| CommonController | `/admin/common` | 文件上传、阿里云 OSS 图片存储 |
| OrderController | `/admin/order` | 订单搜索、状态统计、接单/拒单/取消/派送/完成 |
| ReportController | `/admin/report` | 营业额/用户/订单统计、Top10 销量、Excel 导出 |
| WorkSpaceController | `/admin/workspace` | 今日营收概览、订单/菜品/套餐总览 |
| ShopController | `/admin/shop` | 店铺状态设置 |
| WebSocketServer | `/ws/{sid}` | WebSocket 实时订单推送 |

### 用户端接口 (`/user/**`) — JWT 用户鉴权

| Controller | 路径 | 功能 |
|------------|------|------|
| UserController | `/user/user` | 微信授权登录 |
| CategoryController | `/user/category` | 按类型查询分类列表 |
| DishController | `/user/dish` | 按分类查询菜品、口味信息 |
| SetmealController | `/user/setmeal` | 按分类查询套餐、套餐详情 |
| ShoppingCartController | `/user/shoppingCart` | 购物车增删改查、批量清空 |
| AddressBookController | `/user/addressBook` | 收货地址 CRUD、默认地址设置 |
| OrderController | `/user/order` | 提交订单、微信支付、历史订单、取消、再来一单、催单 |
| ShopController | `/user/shop` | 查询店铺营业状态 |

> 💡 完整 API 文档请启动项目后访问 Knife4j 在线文档：`http://localhost:8080/doc.html`

---

## 🚀 本地快速启动

### 环境前置准备

| 依赖 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8+ | 后端运行环境 |
| MySQL | 8.0 | 数据库 |
| Redis | 5.0+ | 缓存与分布式锁 |
| Maven | 3.6+ | 项目构建 |
| Node.js | 14+ | Vue 管理后台运行 |
| 微信开发者工具 | 最新版 | 小程序调试 |

### 1. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS sky_take_out DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

根据项目中的实体类（`sky-pojo/.../entity/`）创建对应的 11 张数据表，表结构参考上方 [数据库 ER 图](#-数据库-er-图) 章节。

> 💡 提示：可联系开发者获取完整的 SQL 初始化脚本。

### 2. 后端服务启动

1. 修改 `sky-server/src/main/resources/application-dev.yml` 中的配置：

```yaml
sky:
  datasource:
    host: localhost          # MySQL 地址
    port: 3306               # MySQL 端口
    database: sky_take_out   # 数据库名
    username: root           # 数据库用户名
    password: your_password  # 数据库密码
  redis:
    host: localhost          # Redis 地址
    port: 6379               # Redis 端口
    password:                # Redis 密码 (无则不填)
  alioss:
    access-key-id: your_key       # 阿里云 AccessKey (可选，影响图片上传)
    access-key-secret: your_secret
  wechat:
    appid: your_appid       # 微信小程序 AppID (可选，影响微信登录)
    secret: your_secret     # 微信小程序 Secret
```

2. 运行启动类：

```bash
# 使用 Maven 启动
mvn spring-boot:run

# 或者在 IDE 中直接运行
# sky-server/src/main/java/com/sky/SkyApplication.java
```

3. 访问 Knife4j 在线接口文档：

```
http://localhost:8080/doc.html
```

### 3. 前端项目启动（暂不上传）

> 📌 **说明**：本仓库仅包含后端 Java 服务代码。前端项目（Vue 管理后台 / 微信小程序）位于独立仓库，请分别获取后启动。

**Vue 管理后台：**

```bash
# 进入后台目录
cd canteen-admin-vue

# 安装依赖
npm install

# 启动本地开发服务器
npm run dev
```

**微信小程序调试（暂不上传）：**

1. 下载并安装 [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
2. 导入小程序项目目录
3. 配置 AppID（可使用测试号）
4. 编译预览

---

## 🐳 Docker 部署 (可选)

```bash
# 1. 构建后端镜像
cd campus-canteen-takeout
mvn clean package -DskipTests
docker build -t campus-canteen:latest .

# 2. 启动 MySQL + Redis + 应用
docker-compose up -d
```

---

## 📸 项目截图

> 以下为项目功能截图展示区域（实际截图暂不上传）

| 小程序首页 | 菜品详情 | 购物车 | 订单列表 |
|:---:|:---:|:---:|:---:|
| ![首页](./screenshots/miniapp-home.png) | ![菜品](./screenshots/miniapp-dish.png) | ![购物车](./screenshots/miniapp-cart.png) | ![订单](./screenshots/miniapp-order.png) |

| 管理后台登录 | 菜品管理 | 订单看板 | 数据报表 |
|:---:|:---:|:---:|:---:|
| ![登录](./screenshots/admin-login.png) | ![菜品](./screenshots/admin-dish.png) | ![订单](./screenshots/admin-order.png) | ![报表](./screenshots/admin-report.png) |

---

## 🤝 贡献指南

本项目为校园食堂外卖点餐全栈项目，适用于：

- 🎓 **毕业设计参考**：完整的 SpringBoot + Vue + 微信小程序全栈项目
- 📚 **课程设计实战**：涵盖前后端分离、支付集成、实时通信等企业级技术
- 🏫 **高校食堂数字化改造**：可直接部署使用的食堂外卖解决方案

欢迎提交 Issue 和 Pull Request！

---

## 📄 License

本项目基于 MIT License 开源，可自由使用、修改和分发。

---

## 👨‍💻 开发者

- GitHub: [@qzk11234](https://github.com/qzk11234)
- 技术栈：SpringBoot · MyBatis · Vue · 微信小程序

---

<p align="center">
  <b>如果这个项目对你有帮助，请给一个 ⭐ Star 支持一下！</b>
</p>
