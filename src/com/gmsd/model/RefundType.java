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
public enum RefundType {
  // 特别注意：此处序数将存入数据库，所以请勿改变已有元素的顺序！
  FIX_DATE(0, "固定还款日"),
  FLEXIBLE_DATE(1, "非固定还款日");

  public final int value;
  public final String description;

  RefundType(int value, String description) {
    this.value = value;
    this.description = description;
  }

  public static Map<String, String> options() {
    LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
    for (RefundType type : RefundType.values()) {
      vals.put(type.value + "", type.description);
    }
    return vals;
  }

  @JsonValue
  public int value() {
    return this.value;
  }

  @JsonCreator
  public static RefundType fromInt(int value) {
    for (RefundType type : RefundType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid Status type code: " + value);
  }
}
