/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.model.response;

/**
 * 共鸣返回代码
 */
public enum GMResponseCode {

  COMMON_SUCCESS(                       0,         "成功"),
  COMMON_ILLEGAL_PARAM(                 1,         "参数异常"),
  COMMON_SERVER_ERROR(                  2,         "系统异常"),
  COMMON_INVALID_STATUS(                3,         "状态异常"),
  COMMON_NOT_IMPLEMENTED(               4,         "方法没有实现"),
  COMMON_IO_ERROR(                      5,         "文件读写发生错误"),

  USER_COMMON(                          10001,     "用户异常"),
  USER_NOT_EXISTS(                      10002,     "用户不存在"),
  USER_INVALID_STATUS(                  10003,     "用户状态错误"),
  USER_ALREADY_EXISTS(                  10004,     "用户已存在"),
  USER_NOT_MATCH(                       10005,     "用户身份证与姓名不匹配"),

  ORDER_COMMON(                         11001,     "订单异常"),
  ORDER_ALREADY_EXISTS(                 11002,     "订单已存在"),
  ORDER_NOT_EXISTS(                     11003,     "订单不存在"),
  ORDER_EXPIRED(                        11004,     "订单已失效"),  // E.g. 理财周期已结束
  ORDER_INVALID_STATUS(                 11005,     "订单状态错误"),
  ORDER_MATCH_ERROR(                    11006,     "订单匹配错误"),
  ORDER_INCOMPLETE(                     11007,     "订单未结束"),
  ORDER_BANKACCOUNT_INSUFFICIENT_FUNDS( 11008,     "订单所指定的银行卡余额不足或者超过限额"),

  PLAN_COMMON(                          12001,     "计划异常"),
  PLAN_NOT_EXISTS(                      12002,     "计划不存在"),
  PLAN_INVALID_STATUS(                  12003,     "计划状态错误"),
  PLAN_SOLD_OUT(                        12004,     "计划已售完"),

  PAYMENT_COMMON(                       13001,     "支付异常"),
  PAYMENT_EXPIRED(                      13002,     "支付超时"),
  PAYMENT_ALREADY_CONFIRMED(            13003,     "支付已确认过"),

  PRODUCT_COMMON(                       14001,     "产品异常"),
  PRODUCT_NOT_EXISTS(                   14002,     "产品不存在"),
  PRODUCT_INVALID_STATUS(               14003,     "产品状态错误"),

  CONTRACT_COMMON(                      15001,     "合同异常"),
  CONTRACT_NOT_EXISTS(                  15002,     "合同不存在"),
  CONTRACT_INVALID_STATUS(              15003,     "合同状态错误"),
  CONTRACT_IO_ERROR(                    15004,     "合同文件读写错误"),
  CONTRACT_TEMPLATE_ERROR(              15005,     "合同文件模板错误"),
  CONTRACT_CONTEXT_ERROR(               15006,     "合同上下文生成出错"),

  DB_PERSISTENCE(                       16001,     "数据库异常"),

  MERCHANT_COMMON(                      17001,     "商户异常"),
  MERCHANT_NOT_EXISTS(                  17002,     "商户不存在"),
  MERCHANT_INVALID_STATUS(              17003,     "商户状态错误"),
  MERCHANT_NOT_MATCH(                   17004,     "商户信息不匹配"),
  MERCHANT_INVALID_OPERATION(           17005,     "商户操作非法"),

  NOTE_COMMON(                          18001,     "债权异常"),
  NOTE_NOT_EXISTS(                      18002,     "债权不存在"),
  NOTE_INVALID_STATUS(                  18003,     "债权状态错误"),

  BANK_COMMON(                          19001,     "银行信息异常"),
  BANK_NOT_EXISTS(                      19002,     "银行不存在"),
  BANK_ALREADY_EXISTS(                  19003,     "银行已经存在"),
  BANK_INVALID_STATUS(                  19004,     "银行状态错误"),
  BANK_NOT_MATCH(                       19005,     "银行不匹配"),

  LOANER_COMMON(                        20001,     "债权人异常"),
  LOANER_NOT_EXISTS(                    20002,     "债权人不存在"),
  LOANER_INVALID_STATUS(                20003,     "债权人状态错误"),

  P2P_COMMON(                           21001,     "P2P异常"),
  P2P_INVALID_STATUS(                   21002,     "P2P状态异常"),
  P2P_NOTE_MATCH_ORDER_ERROR(           21003,     "P2P债权订单匹配异常"),
  P2P_ORDER_REMAIN_BASE_ILLEGAL(        21004,     "P2P订单剩余本金不正确"),
  P2P_ADD_NOTE_FAILED(                  21006,     "P2P添加债权失败"),
  P2P_CANCEL_NOTE_FAILED(               21005,     "P2P取消债权失败"),

  BANK_ACCOUNT_COMMON(                  22001,     "银行账户异常"),
  BANK_ACCOUNT_NOT_BIND(                22002,     "银行账户未绑定"),

  TEST_COMMON(                          1000001,   "测试异常"),
  TEST_NOT_EXISTS(                      1000002,   "测试目标不存在"),
  TEST_INVALID_STATUS(                  1000003,   "测试目标状态异常"),
  TEST_DB_PERSISTENCE(                  1000004,   "测试数据库异常");

  private int value;
  private String info;

  private GMResponseCode(int value, String info) {
    this.value = value;
    this.info = info;
  }

  public String GetInfo() {
    return info;
  }

  @Override
  public String toString() {
    return "[" + Integer.toString(this.value) + "] " + info;
  }

  @com.fasterxml.jackson.annotation.JsonValue
  public int value() {
    return this.value;
  }

  @com.fasterxml.jackson.annotation.JsonCreator
  public static GMResponseCode fromInt(int value) {
    for (GMResponseCode type : GMResponseCode.values()) {
      if (type.value == value) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid Status type code: " + value);
  }

  @org.codehaus.jackson.annotate.JsonCreator
  public static GMResponseCode fromInt2(int value) {
    for (GMResponseCode type : GMResponseCode.values()) {
      if (type.value == value) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid Status type code: " + value);
  }
}

