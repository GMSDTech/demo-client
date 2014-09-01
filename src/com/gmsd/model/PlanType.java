/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.model;

/**
 * 产品计划类型
 */
public enum PlanType {
  LOAN("小贷"),
  FACTORING("保理"),
  P2P("P2P");

  public String name;

  private PlanType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
