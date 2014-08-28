/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.model;

/**
 * 订单状态。
 */
public enum OrderStatus {
  CREATING(0, "下单中"),
  REQUESTED(1, "已下单"), // NOTE: 此时plan和note都被预扣金额
  FAILED(2, "下单失败"),
  PAID(3, "已支付"),
  FULFILLED(4, "已投标"),
  DELIVERED(5, "已放款"),
  FINISHED(6, "已还款结束"),
  CANCELED(7, "订单取消"),
  BIDED(8, "已部分投标"),
  REFUND(9, "已收到还款"),
  NOTE_CANCELED(10, "债权取消");

  public int index;
  public String name;

  @Override
  public String toString() {
    return this.name;
  }

  OrderStatus(int index, String name) {
    this.index = index;
    this.name = name;
  }
}
