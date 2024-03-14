# Frontend 前端

# Backend 后端

## 配置

./backend/src/main/resources/application.yml 

### MySQL 8.0

User 用户表

```mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `salt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `status` tinyint NULL DEFAULT 1 COMMENT '账号状态：0 禁用 1 正常',
  `token` int NULL DEFAULT 0 COMMENT 'token数量',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```

```yaml
datasource:
  driver-class-name: com.mysql.cj.jdbc.Driver
  url: jdbc:mysql://localhost:3306/chat
  username: root
  password: <your password>
```

### Redis

```yaml

redis:
   # 连接地址
   host: <your host>
   # 端口
   port: 6379
   # 数据库
   database: 0
   # 连接超时
   connect-timeout: 5s
   # 读超时
   timeout: 5s
   # Lettuce 客户端的配置
   lettuce:
   # 连接池配置
       pool:
         # 最小空闲连接
          min-idle: 0
          # 最大空闲连接
          max-idle: 8
          # 最大活跃连接
          max-active: 8
          # 从连接池获取连接 最大超时时间，小于等于0则表示不会超时
          max-wait: -1ms
```

### Email

```yaml
mail:
  host: <your host>
  port: <your port>
  protocol: <your protocol>
  username: <your username>
  password: <your password>
  default-encoding: UTF-8
```

### 星火模型

```yaml
spark:
  # version 1.1 2.1 3.1 3.5
  hostUrl: https://spark-api.xf-yun.com/v1.1/chat
  appid: <your appid>
  apiSecret: <your apiSecret>
  apiKey: <your apiKey>
```

