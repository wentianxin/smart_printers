CREATE DATABASE `smpt` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `order_time` datetime NOT NULL,
  `order_content` text,
  `order_remark` varchar(500) NOT NULL,
  `order_meal_fee` int(11) NOT NULL,
  `order_pay_status` char(1) NOT NULL,
  `order_dis_fee` int(11) NOT NULL,
  `order_pre_amount` int(11) NOT NULL,
  `order_sum` int(11) NOT NULL,
  `order_status` varchar(5) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_address` varchar(100) NOT NULL,
  `user_telephone` varchar(100) NOT NULL,
  `client_name` varchar(100) NOT NULL,
  `client_address` varchar(100) NOT NULL,
  `client_telephone` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `printer` (
  `id` int(11) NOT NULL,
  `printer_status` varchar(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_account` varchar(100) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_printers` int(11) NOT NULL,
  `user_logo` varchar(200) NOT NULL DEFAULT 'logo',
  `user_qrcode` varchar(200) NOT NULL DEFAULT '二维码',
  `user_store` varchar(100) NOT NULL,
  `user_address` varchar(100) NOT NULL,
  `user_phone` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_order` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_printer` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `printer_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
