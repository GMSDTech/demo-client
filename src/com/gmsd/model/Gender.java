/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.model;

/**
 * 性别的枚举类
 */
public enum Gender {
  // 特别注意：此处序数将存入数据库，所以请勿改变已有元素的顺序！
  UNKNOWN("保密"),
  MALE("男"),
  FEMALE("女");

  private String name;

  Gender(String name) {
    this.name = name;
  }

  public static Gender buildGender(String genderStr) {
    return
      MALE.name.equals(genderStr) ? MALE :
        FEMALE.name.equals(genderStr) ? FEMALE :
          UNKNOWN;
  }

  @Override
  public String toString() {
    return name;
  }
}
