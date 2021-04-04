-- auto Generated on 2021-04-04
-- DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
	user_id INT (11) UNIQUE NOT NULL AUTO_INCREMENT COMMENT '用户id',
	user_name VARCHAR (50) NOT NULL DEFAULT '' COMMENT '用户名称',
	user_password VARCHAR (50) NOT NULL DEFAULT '' COMMENT '用户密码',
	is_administration TINYINT (3) NOT NULL DEFAULT 0 COMMENT '是否是管理员 false为用户 true为管理员',
	PRIMARY KEY (user_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'user';
