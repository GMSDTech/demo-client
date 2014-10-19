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
  FAILED(2, "订单失效"),
  PAID(3, "已支付"),
  FULFILLED(4, "已投标"),
  BIDDING(5, "投标中"),
  NOTE_TRANSFERRING(6, "债权转让中"),
  REDEEMED(7, "已赎回"),
  WITHDRAWN(8, "已提现"),
  REFUNDING(9, "待退款"),
  REFUNDED(10, "已退款");

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
