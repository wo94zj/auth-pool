## 三方授权

#### 简介

对接第三方工具包。

##### thirdparty-auth

支持微信、QQ、支付宝绑定登录工具包。



#### 库

##### 三方绑定信息表

| 属性        | 类型    | 备注             |
| ----------- | ------- | ---------------- |
| id          |         |                  |
| appname     |         |                  |
| third_type  | enum    |                  |
| user_id     |         |                  |
| open_id     |         |                  |
| nickname    |         |                  |
| sex         |         |                  |
| img         |         |                  |
| status      | tinyint | 0：绑定；1：解绑 |
| update_time |         |                  |
| create_time |         |                  |

```sql
DROP TABLE IF EXISTS `mc_user_third_bind`;
CREATE TABLE `mc_user_third_bind`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appname` enum('xwk') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `third_type` enum('wx','ali','qq') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '0：绑定；1：解绑',
  `update_time` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_app_type_user`(`appname`, `third_type`, `user_id`) USING BTREE,
  UNIQUE INDEX `index_app_type_open`(`appname`, `third_type`, `open_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic STORAGE DISK;
```

