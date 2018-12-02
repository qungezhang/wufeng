INSERT INTO `sys_id_rule` VALUES ('8', 'W', '5', '12', '180800012');

alter table sys_point_log add column getWay int(11) DEFAULT NULL COMMENT '获取方式';    -- 获取方式
alter table sys_point_log add column pbid int(11) DEFAULT NULL COMMENT '能量表id';    -- 能量表id
alter table sys_point_log add column wid VARCHAR (50) DEFAULT NULL COMMENT '提现id';  -- 提现id

alter table sys_vipapply add column File2 VARCHAR (200) DEFAULT NULL COMMENT '身份证反面';    -- 身份证背面

alter table quo_quotation add column InvoiceTitle VARCHAR (100) DEFAULT NULL COMMENT '发票抬头';  -- 发票抬头
alter table quo_quotation add column IID int(11) DEFAULT NULL COMMENT '发票id';  -- 发票id

alter table sys_user add column OrderAmount decimal(10,2) DEFAULT 0 COMMENT '订单总金额';  -- 订单总金额
alter table sys_user add column ServiceNum int(11) DEFAULT 0 COMMENT '服务次数';  -- 服务次数

alter table sys_withdraw add column withdrawConsumeType int(11) DEFAULT 0 COMMENT '提现消耗类型：0订单金额；1任务次数';  -- 提现消耗类型：0订单金额；1任务次数
alter table sys_withdraw add column withdrawBeforeAmount decimal(10,2) DEFAULT 0 COMMENT '提现前的金额';  -- 提现前的金额

alter table sys_messagebatch add column Title VARCHAR (50) DEFAULT NULL COMMENT '消息标题';
alter table sys_messagebatch add column RType int(11) DEFAULT NULL COMMENT '接收人类型 1 个人，2 所有人';
alter table sys_messagebatch add column Sended int(11) DEFAULT 0 COMMENT '是否推送 ：0 未推送 1已推送';
alter table sys_messagebatch add column Receiver int(11) DEFAULT NULL COMMENT '接收者id';

alter table sys_message add column Deleted int(11) DEFAULT 0 COMMENT '是否删除 ：0 未删除 1已删除';

-- 新增的任务编号和提现编号
INSERT INTO `sys_id_rule` VALUES ('8', 'J', '5', '3', '180900001');
INSERT INTO `sys_id_rule` VALUES ('9', 'W', '5', '27', '180800001');

-- ----------------------------
-- Table structure for `sys_point_balloon`
-- ----------------------------
DROP TABLE IF EXISTS `sys_point_balloon`;
CREATE TABLE `sys_point_balloon` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '类型：1-品类，2-品牌，10-用户行为产生，如：注册送的',
  `createTime` time DEFAULT NULL COMMENT '创建时间',
  `endTime` datetime DEFAULT NULL COMMENT '截止/消失时间',
  `name` varchar(100) DEFAULT NULL COMMENT '产品、品牌名称或获取积分描述，如：注册',
  `icon` varchar(100) DEFAULT NULL COMMENT '产品图片或品牌logo',
  `oid` varchar(50) DEFAULT NULL COMMENT '订单id',
  `point` double(11,2) NULL DEFAULT NULL COMMENT  '所含积分',
  `getWay` int(11) DEFAULT NULL COMMENT '获取方式：1 产品；2 品牌；3 转发；4 邀请码被注册；5 交易 ；6 认证（申请VIP）；7 注册' ,
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `status` int(11) DEFAULT '0' COMMENT '状态，只针对用户行为产生的气球有用：0-未摘取，1-已摘取',
  `energyStatus` int(11) DEFAULT '0' COMMENT '产品，品牌能量状态：0,未产生  1,已产生',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='积分气球';


-- ----------------------------
-- Table structure for `sys_withdraw`
-- ----------------------------
DROP TABLE IF EXISTS `sys_withdraw`;
CREATE TABLE `sys_withdraw` (
  `wid` varchar(50) NOT NULL,
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `type` int(11) DEFAULT NULL COMMENT '转账类型：1支付宝；2微信',
  `account` varchar(50) NOT NULL COMMENT '交易账号',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提现金额',
  `exchangePoint` double(11,2) NULL DEFAULT  '0' COMMENT '兑换积分',
  `createTime` datetime DEFAULT NULL COMMENT '提现时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '提现状态：0-提交申请，1-打款成功，-1审核不通过',
  `transferTime` datetime DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`wid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `sys_job`
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `jid` varchar(50) NOT NULL COMMENT 'ID',
  `name` varchar(100) DEFAULT '' COMMENT '任务名称',
  `subhead` varchar(100) DEFAULT NULL COMMENT '副标题',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '截止时间',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `employerName` varchar(100) DEFAULT NULL COMMENT '雇主姓名',
  `employerMobile` varchar(100) DEFAULT NULL COMMENT '雇主联系方式',
  `tags` varchar(50) DEFAULT NULL COMMENT '标签',
  `description` longtext COMMENT '内容',
  `price` decimal(11,2) DEFAULT NULL COMMENT '价格',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0-默认，1',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `modifyTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`jid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';

-- ----------------------------
-- Table structure for `sys_user_job`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_job`;
CREATE TABLE `sys_user_job` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `jobId` varchar(11) DEFAULT NULL COMMENT '任务表ID',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `cardNo` varchar(20) DEFAULT NULL COMMENT '身份证',
  `createTime` datetime DEFAULT NULL COMMENT '接受任务时间',
  `status` int(11) DEFAULT '0' COMMENT '审核状态：0领取，1放弃',
  `loseTime` datetime DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `examineStatus` int(11) DEFAULT '0' COMMENT '审核状态：0待审核；1审核通过；2审核失败',
  `examineRemark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `examineTime` datetime DEFAULT NULL COMMENT '审核时间',
  `withdrawCount` int(11) DEFAULT '0' COMMENT '是否计入提现次数，0未计入，1已计入',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 消息模板表（v2.0）
DROP TABLE IF EXISTS `sys_messagebatch`;
CREATE TABLE `sys_messagebatch` (
  `MBID` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息模板表ID',

  `Message` varchar(200) DEFAULT NULL COMMENT '消息内容',
  `SendDate` datetime DEFAULT NULL COMMENT '发送日期',
  `MType` int(11) DEFAULT NULL COMMENT '1 系统 2 人工',

  `Status` int(11) DEFAULT NULL COMMENT '0 删除 1未删除',

  `Title` varchar(50) DEFAULT NULL COMMENT '消息标题',
`RType` int(11) DEFAULT NULL COMMENT '接收人类型 0 个人，1 所有人',
	`Sended` int(11) DEFAULT NULL COMMENT '是否推送 ：0 未推送 1已推送',
  PRIMARY KEY (`MBID`)
) ENGINE=InnoDB AUTO_INCREMENT=2017 DEFAULT CHARSET=utf8 COMMENT='消息模板表';