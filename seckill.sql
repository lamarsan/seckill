# Host: 127.0.0.1  (Version 5.7.24-log)
# Date: 2020-01-27 19:58:31
# Generator: MySQL-Front 6.1  (Build 1.26)


#
# Structure for table "item"
#

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL DEFAULT '',
  `price` decimal(50,6) NOT NULL DEFAULT '0.000000',
  `description` varchar(500) NOT NULL DEFAULT '',
  `sales` bigint(20) NOT NULL DEFAULT '0',
  `img_url` varchar(255) NOT NULL DEFAULT '',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "item"
#

INSERT INTO `item` VALUES (1,'iphone',8000.000000,'这是一个苹果手机',123,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578568047528&di=b62a1c9933701ec8183fbecdd6ea219a&imgtype=0&src=http%3A%2F%2Fimage.hxyxt.com%2Fmedias%2Fsys_master%2Fimages%2Fimages%2Fh79%2Fh11%2F9401117704222.jpg',0,'2020-01-09 16:28:29','2020-01-21 23:00:18'),(2,'redmi',1000.000000,'这是一个红米手机',131,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578571314685&di=84d9a62421b1736b0423d7fa9c2f610d&imgtype=0&src=http%3A%2F%2Fcdn.178hui.com%2Fupload%2F2019%2F0729%2F12341469807.jpg',0,'2020-01-09 17:14:07','2020-01-27 19:51:16');

#
# Structure for table "item_stock"
#

DROP TABLE IF EXISTS `item_stock`;
CREATE TABLE `item_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stock` bigint(20) NOT NULL DEFAULT '0',
  `item_id` bigint(20) NOT NULL DEFAULT '0',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "item_stock"
#

INSERT INTO `item_stock` VALUES (1,100,1,0,'2020-01-09 16:29:59','2020-01-27 19:58:17'),(2,100,2,0,'2020-01-09 17:14:07','2020-01-27 19:58:20');

#
# Structure for table "order_info"
#

DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `promo_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `item_id` bigint(20) NOT NULL DEFAULT '0',
  `item_price` decimal(50,6) NOT NULL DEFAULT '0.000000',
  `amount` int(11) NOT NULL DEFAULT '0',
  `order_price` decimal(50,6) NOT NULL DEFAULT '0.000000' COMMENT '总价',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "order_info"
#


#
# Structure for table "promo_info"
#

DROP TABLE IF EXISTS `promo_info`;
CREATE TABLE `promo_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(64) NOT NULL DEFAULT '',
  `start_date` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `end_date` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
  `item_id` bigint(20) NOT NULL DEFAULT '0',
  `promo_item_price` decimal(50,6) NOT NULL DEFAULT '0.000000',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

#
# Data for table "promo_info"
#

INSERT INTO `promo_info` VALUES (1,'iphone抢购活动','2020-01-10 20:34:00','2020-01-20 00:00:00',1,5000.000000,0,'2020-01-10 18:57:52','2020-01-10 20:33:44');

#
# Structure for table "sequence_info"
#

DROP TABLE IF EXISTS `sequence_info`;
CREATE TABLE `sequence_info` (
  `name` varchar(255) NOT NULL DEFAULT '',
  `current_value` bigint(20) NOT NULL DEFAULT '0',
  `step` int(11) NOT NULL DEFAULT '0',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "sequence_info"
#

INSERT INTO `sequence_info` VALUES ('order_info',68,1,0,'2020-01-09 19:26:49','2020-01-09 19:26:49');

#
# Structure for table "stock_log"
#

DROP TABLE IF EXISTS `stock_log`;
CREATE TABLE `stock_log` (
  `stock_log_id` varchar(64) NOT NULL DEFAULT '',
  `item_id` bigint(20) NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '// 1表示初始状态，2表示下单扣减库存成功，3表示下单回滚',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stock_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "stock_log"
#


#
# Structure for table "user_info"
#

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  `gender` int(1) NOT NULL DEFAULT '0',
  `age` int(3) NOT NULL DEFAULT '0',
  `telphone` varchar(11) NOT NULL DEFAULT '',
  `register_mode` varchar(64) NOT NULL DEFAULT '' COMMENT 'by phone,by wechat,by alipay',
  `third_party_id` varchar(64) NOT NULL DEFAULT '',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 normal，1 delete',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `telphone_unique_index` (`telphone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "user_info"
#


#
# Structure for table "user_password"
#

DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `encrpt_password` varchar(128) NOT NULL DEFAULT '',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "user_password"
#

