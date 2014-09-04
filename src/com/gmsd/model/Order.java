/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.model;

/**
 * 理财计划购买订单。
 */
public class Order {
  /**
   * Dummy constructor to enable Bean to construct an instance
   * from a JSON string. Otherwise, with only the constructor
   * below (有参数构造方法) as a default constructor, Bean would
   * fail to construct an instance from a JSON string.
   */
  public Order() {
  }

  /**
   * 有参数构造方法
   */
  public Order(Long merchantRequestId,
               Long userId,
               Long planId,
               String orderAmount,
               String successAmount,
               String merchantOrderDate,
               String effectiveDate,
               String endDate,
               OrderStatus status) {
    this.merchantRequestId = merchantRequestId;
    this.userId = userId;
    this.planId = planId;
    this.orderAmount = orderAmount;
    this.successAmount = successAmount;
    this.merchantOrderDate = merchantOrderDate;
    this.effectiveDate = effectiveDate;
    this.endDate = endDate;
    this.status = status;
  }

  /**
   * 商户订单号
   */
  public Long merchantRequestId;

  /**
   * 下单用户id
   */
  public Long userId;

  /**
   * 所购买的理财计划id
   */
  public Long planId;

  /**
   * 下单金额
   */
  public String orderAmount;

  /**
   * 实际扣款金额
   */
  public String successAmount;

  /**
   * 商户订单日期 yyyy-mm-dd
   */
  public String merchantOrderDate;

  /**
   * 订单生效日期 yyyy-mm-dd HH:mm:ss Z
   */
  public String effectiveDate;

  /**
   * 预期还款结束日期 yyyy-mm-dd
   */
  public String endDate;

  /**
   * 订单状态
   */
  public OrderStatus status;
}
