DROP TABLE user;
CREATE TABLE user ( id bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID', userId varchar(9) COMMENT '用户ID', userHead varchar(16) COMMENT '用户头像', createTime timestamp NULL COMMENT '创建时间', updateTime timestamp NULL COMMENT '更新时间', userName varchar(64), PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into user (id, userId, userHead, createTime, updateTime, userName) values (1, '10001', '1_04', '2022-04-13 00:00:00', '2022-04-13 00:00:00', '小傅哥');
