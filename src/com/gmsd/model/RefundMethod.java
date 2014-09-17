/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 还款方式
 */
public enum RefundMethod {
  // 特别注意：此处序数将存入数据库，所以请勿改变已有元素的顺序！
  EQUAL_INTEREST_BASE(0, "等额本息"),
  MONTH_INTEREST(1, "按月付息，到期还本"),
  RETURN_INTEREST_BASE(2, "到期还本付息");

  public final int value;
  public final String description;

  RefundMethod(int value, String description) {
    this.value = value;
    this.description = description;
  }

  public static Map<String, String> options() {
    LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
    for (RefundMethod type : RefundMethod.values()) {
      vals.put(type.value + "", type.description);
    }
    return vals;
  }

  @JsonValue
  public int value() {
    return this.value;
  }

  @JsonCreator
  public static RefundMethod fromInt(int value) {
    for (RefundMethod type : RefundMethod.values()) {
      if (type.value == value) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid Status type code: " + value);
  }
}
