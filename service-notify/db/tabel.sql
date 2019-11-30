CREATE TABLE `t_bbc_order_aftersale` (
  `forder_aftersale_id` varchar(32) NOT NULL COMMENT '售后单号',
  `fuid` bigint(20) DEFAULT NULL COMMENT '用户id',
  `fdeal_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '处理对象类型 1后台发起售后 2前台发起售后',
  `forder_id` varchar(32) NOT NULL DEFAULT '' COMMENT '订单号',
  `fsupplier_order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '供应商采购单号',
  `ftransport_order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '发货单号',
  `fbatch_id` varchar(32) NOT NULL DEFAULT '' COMMENT '批次号',
  `faftersale_total_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '售后总金额',
  `faftersale_type` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '售后类型 1 退款 2 退款退货',
  `fsku_code` varchar(32) NOT NULL DEFAULT '' COMMENT 'SKU编码',
  `faftersale_num` int(8) NOT NULL DEFAULT '0' COMMENT '售后数量',
  `faftersale_reason` tinyint(4) DEFAULT '1' COMMENT '售后原因类型 1客户申请 2供应商无法发货 3供应商漏发 4供应商延期发货 5供应商商品发错 6商品质量问题 7商品运输破损',
  `funit_price` bigint(20) DEFAULT '0' COMMENT 'sku单价--下单时的单价',
  `frecoup_amount` bigint(20) DEFAULT '0' COMMENT '售后补偿金额',
  `frecoup_type` tinyint(4) DEFAULT '1' COMMENT '售后补偿类型 1运费补偿',
  `fdeduction_amount` bigint(20) DEFAULT '0' COMMENT '售后扣减金额',
  `fdeduction_type` tinyint(4) DEFAULT '1' COMMENT '售后扣减类型 1承担运费 2其他费用',
  `faftersalet_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '售后状态1待客服审核 2待采购审核 3待仓库审核 4待财务审核 5已拒绝 6待退货 7待退款 8已成功 9已撤销',
  `fuser_remark` varchar(255) DEFAULT '' COMMENT '客户备注',
  `fadmin_id` bigint(20) DEFAULT NULL COMMENT '售后发起人--后台运营id',
  `fadmin_remark` varchar(255) DEFAULT '' COMMENT '后台售后备注',
  `fsupplier_id` bigint(20) DEFAULT NULL COMMENT '供应商id',
  `fsupplier_remark` varchar(255) DEFAULT '' COMMENT '供应商审核意见',
  `fsupplier_opinion` varchar(255) DEFAULT '' COMMENT '供应商意见 1 同意退货退款 2拒绝退货退款',
  `fcherk_id` varchar(255) DEFAULT '' COMMENT '财务审核人',
  `fcherk_remark` varchar(255) DEFAULT '' COMMENT '财务审核意见',
  `fback_status` tinyint(4) DEFAULT '1' COMMENT '回寄状态 1 未签收 2已签收',
  `fis_back` tinyint(4) DEFAULT '1' COMMENT '是否寄回售后地址 1否 2是',
  `fdelivery_name` varchar(32) NOT NULL DEFAULT '' COMMENT '回寄收件人姓名',
  `fdelivery_province` varchar(64) DEFAULT '' COMMENT '回寄地址省',
  `fdelivery_city` varchar(64) DEFAULT '' COMMENT '回寄地址市',
  `fdelivery_area` varchar(64) DEFAULT '' COMMENT '回寄地址区',
  `fdelivery_addr` varchar(255) NOT NULL DEFAULT '' COMMENT '回寄详细地址',
  `fback_name` varchar(32) DEFAULT '' COMMENT '回寄填写人',
  `fback_logistics_order` varchar(255) NOT NULL DEFAULT '' COMMENT '回寄物流单号',
  `flogistics_company_id` bigint(20) DEFAULT NULL COMMENT '回寄物流公司id',
  `fdelivery_mobile` varchar(32) NOT NULL DEFAULT '' COMMENT '收货人联系手机',
  `fsupplier_back_address` varchar(255) DEFAULT '' COMMENT '供应商售后地址',
  `fcreate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fmodify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`forder_aftersale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='售后基础信息表';


CREATE TABLE `t_bbc_user_account` (
  `fuid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `fbalance` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '可用余额',
  `fcredit_status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '信用账户状态：1未激活，2激活，3冻结',
  `fcredit_recharge` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '信用额度，额度人工定',
  `fcredit_balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '信用额度余额',
  `ffreeze_amount` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '信用额度支付中冻结金额',
  `fcredit_pay_amount` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '信用支付金额',
  `frecharge` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '充值金额',
  `fwithdraw` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '提现金额',
  `ffreeze_withdraw` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '提现冻结金额',
  `frefund` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '退款金额',
  `fpay` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '支付金额',
  `ffreeze_pay` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '支付中冻结金额',
  `foperate_remark` varchar(64) NOT NULL DEFAULT '' COMMENT '操作备注',
  `fcreate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fmodify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`fuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表';