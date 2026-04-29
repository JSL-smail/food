-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `code` varchar(64) NOT NULL COMMENT '角色编码',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `code` varchar(64) NOT NULL COMMENT '权限编码',
  `type` tinyint(1) DEFAULT '1' COMMENT '类型(1:菜单,2:按钮)',
  `url` varchar(256) DEFAULT NULL COMMENT '接口路径',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方式',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色中间表';

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限中间表';

-- ----------------------------
-- Table structure for category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食品分类表';

-- ----------------------------
-- Table structure for food
-- ----------------------------
CREATE TABLE IF NOT EXISTS `food` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `name` varchar(128) NOT NULL COMMENT '食物名称',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `description` text COMMENT '描述',
  `image` varchar(256) DEFAULT NULL COMMENT '图片URL',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0:下架,1:上架)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物表';

-- ----------------------------
-- Table structure for orders
-- ----------------------------
CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单编号',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态(0:待支付,1:已支付,2:已发货,3:已完成,4:已取消)',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `food_id` bigint(20) NOT NULL COMMENT '食物ID',
  `food_name` varchar(128) NOT NULL COMMENT '食物名称',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `quantity` int(11) NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';
-- ----------------------------
-- Initial Data for sys_user (password: 123456)
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `status`) VALUES 
(1, 'admin', '$2a$10$76/3F/xX7m5pXmY.vO1JGe6XvS1X3z4PZf1q.R1x7X.7X.7X.7X.', '管理员', 1),
(2, 'user', '$2a$10$76/3F/xX7m5pXmY.vO1JGe6XvS1X3z4PZf1q.R1x7X.7X.7X.7X.', '普通用户', 1);

-- ----------------------------
-- Initial Data for sys_role
-- ----------------------------
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`) VALUES 
(1, '管理员', 'ROLE_ADMIN', '系统最高管理员'),
(2, '普通用户', 'ROLE_USER', '普通注册用户');

-- ----------------------------
-- Initial Data for sys_permission
-- ----------------------------
INSERT INTO `sys_permission` (`id`, `name`, `code`, `type`) VALUES 
-- 系统管理
(1, '用户查询', 'sys:user:view', 2),
(2, '用户新增', 'sys:user:add', 2),
(3, '用户修改', 'sys:user:edit', 2),
(4, '用户删除', 'sys:user:delete', 2),
(5, '角色查询', 'sys:role:view', 2),
(6, '角色新增', 'sys:role:add', 2),
(7, '角色修改', 'sys:role:edit', 2),
(8, '角色删除', 'sys:role:delete', 2),
(9, '权限查询', 'sys:permission:view', 2),
-- 食品分类
(10, '分类新增', 'category:add', 2),
(11, '分类修改', 'category:edit', 2),
(12, '分类删除', 'category:delete', 2),
-- 食物管理
(13, '食物新增', 'food:add', 2),
(14, '食物修改', 'food:edit', 2),
(15, '食物删除', 'food:delete', 2),
-- 订单管理
(16, '订单查询', 'orders:view', 2),
(17, '订单修改', 'orders:edit', 2),
(18, '订单删除', 'orders:delete', 2);

-- ----------------------------
-- Initial Data for sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1), (2, 2);

-- ----------------------------
-- Initial Data for sys_role_permission
-- ----------------------------
-- 管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) 
SELECT 1, id FROM sys_permission;

-- 普通用户仅拥有订单查询权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES (2, 16);

-- ----------------------------
-- Initial Data for category
-- ----------------------------
INSERT INTO `category` (`id`, `name`, `description`, `sort`) VALUES 
(1, '主食系列', '汉堡、披萨、面条等', 1),
(2, '精选小吃', '薯条、鸡翅、洋葱圈', 2),
(3, '甜品饮品', '奶茶、咖啡、蛋糕', 3),
(4, '健康轻食', '沙拉、果拼', 4);

-- ----------------------------
-- Initial Data for food
-- ----------------------------
INSERT INTO `food` (`category_id`, `name`, `price`, `description`, `stock`, `status`) VALUES 
(1, '经典芝士牛肉堡', 32.00, '多汁牛肉配香浓芝士', 100, 1),
(1, '意式培根披萨', 58.00, '传统意式风味，料足味美', 50, 1),
(2, '黄金脆薯条', 12.00, '外酥里嫩，金黄诱人', 200, 1),
(2, '奥尔良烤鸡翅', 18.00, '经典奥尔良风味', 150, 1),
(3, '珍珠奶茶', 15.00, 'Q弹珍珠，醇厚奶香', 300, 1);

-- ----------------------------
-- Table structure for kb_knowledge_base
-- ----------------------------
CREATE TABLE IF NOT EXISTS `kb_knowledge_base` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) NOT NULL COMMENT '知识库名称',
  `description` varchar(512) DEFAULT NULL COMMENT '描述',
  `vector_store_type` varchar(32) DEFAULT 'milvus' COMMENT '向量存储类型',
  `embedding_model` varchar(64) DEFAULT 'qwen3-embedding:4b' COMMENT 'Embedding模型',
  `document_count` int(11) DEFAULT '0' COMMENT '文档数量',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库表';

-- ----------------------------
-- Table structure for kb_document
-- ----------------------------
CREATE TABLE IF NOT EXISTS `kb_document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(256) NOT NULL COMMENT '文档标题',
  `content` text COMMENT '文档内容',
  `file_path` varchar(512) DEFAULT NULL COMMENT '文件路径',
  `file_type` varchar(32) DEFAULT NULL COMMENT '文件类型',
  `kb_id` bigint(20) NOT NULL COMMENT '知识库ID',
  `chunk_count` int(11) DEFAULT '0' COMMENT '切片数量',
  `status` varchar(16) DEFAULT 'pending' COMMENT '状态(pending/processing/processed)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_kb_id` (`kb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库文档表';

-- ----------------------------
-- Table structure for kb_document_chunk
-- ----------------------------
CREATE TABLE IF NOT EXISTS `kb_document_chunk` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `document_id` bigint(20) NOT NULL COMMENT '文档ID',
  `content` text NOT NULL COMMENT '切片内容',
  `chunk_index` int(11) NOT NULL COMMENT '切片索引',
  `metadata` varchar(1024) DEFAULT NULL COMMENT '元数据(JSON)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_document_id` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档切片表';

-- ----------------------------
-- Table structure for kb_chat_session
-- ----------------------------
CREATE TABLE IF NOT EXISTS `kb_chat_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `session_id` varchar(64) NOT NULL COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(128) DEFAULT '新对话' COMMENT '会话标题',
  `model` varchar(64) DEFAULT 'qwen2.5:7b' COMMENT '使用的模型',
  `knowledge_base_id` bigint(20) DEFAULT NULL COMMENT '关联知识库ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_id` (`session_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天会话表';

-- ----------------------------
-- Table structure for kb_chat_message
-- ----------------------------
CREATE TABLE IF NOT EXISTS `kb_chat_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `session_id` varchar(64) NOT NULL COMMENT '会话ID',
  `role` varchar(16) NOT NULL COMMENT '角色(user/assistant)',
  `content` text NOT NULL COMMENT '消息内容',
  `model` varchar(64) DEFAULT NULL COMMENT '使用的模型',
  `token_count` int(11) DEFAULT '0' COMMENT 'Token数量',
  `context_docs` text COMMENT '引用的上下文文档(JSON)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- ----------------------------
-- Initial Data for knowledge base
-- ----------------------------
INSERT INTO `kb_knowledge_base` (`id`, `name`, `description`, `vector_store_type`, `embedding_model`) VALUES
(1, '食品知识库', '食品相关知识问答库', 'milvus', 'qwen3-embedding:4b'),
(2, '健康饮食指南', '营养搭配与健康饮食知识', 'milvus', 'qwen3-embedding:4b');

-- ----------------------------
-- Initial Data for kb_document
-- ----------------------------
INSERT INTO `kb_document` (`title`, `content`, `file_type`, `kb_id`, `chunk_count`, `status`) VALUES
('食品营养指南', '均衡营养是健康的基础。我们每天需要摄入适量的蛋白质、碳水化合物、脂肪、维生素和矿物质。蛋白质有助于修复和构建身体组织，碳水化合物是主要的能量来源。', 'txt', 1, 2, 'processed'),
('健康饮食建议', '建议每天摄入五种不同颜色的蔬菜和水果，以获得全面的营养。减少加工食品的摄入，选择新鲜、天然的食材。适量饮水，每天建议8杯水。', 'txt', 1, 2, 'processed');

-- ----------------------------
-- Initial Data for kb_document_chunk
-- ----------------------------
INSERT INTO `kb_document_chunk` (`document_id`, `content`, `chunk_index`, `metadata`) VALUES
(1, '均衡营养是健康的基础。我们每天需要摄入适量的蛋白质、碳水化合物、脂肪、维生素和矿物质。蛋白质有助于修复和构建身体组织，碳水化合物是主要的能量来源。', 0, '{"kb_id":1}'),
(2, '建议每天摄入五种不同颜色的蔬菜和水果，以获得全面的营养。减少加工食品的摄入，选择新鲜、天然的食材。适量饮水，每天建议8杯水。', 0, '{"kb_id":1}');

-- ----------------------------
-- Initial Data for chat sessions and messages
-- ----------------------------
INSERT INTO `kb_chat_session` (`session_id`, `user_id`, `title`, `model`, `knowledge_base_id`) VALUES
('sess_001', 1, '食品营养咨询', 'qwen2.5:7b', 1);

INSERT INTO `kb_chat_message` (`session_id`, `role`, `content`, `model`) VALUES
('sess_001', 'user', '什么是均衡营养？', 'qwen2.5:7b'),
('sess_001', 'assistant', '均衡营养是指每天摄入适量的蛋白质、碳水化合物、脂肪、维生素和矿物质。这些营养素共同作用，维持身体正常运转。', 'qwen2.5:7b');
