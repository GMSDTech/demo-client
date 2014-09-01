package com.gmsd.model.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Parameter implements Comparable<Parameter> {
  protected String key;
  protected String value;

  public Parameter(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public String toString() {
    return key + "=" + value;
  }

  public String toQuery() {
    String query = null;
    try {
      query = key + "=" + URLEncoder.encode(value, "UTF8");
    } catch (UnsupportedEncodingException e) {
      // This should never happen.
    }

    return query;
  }

  @Override
  public int compareTo(Parameter other) {
    int compareKey = this.key.compareTo(other.key);
    int compareValue = this.value.compareTo(other.value);
    return compareKey != 0 ? compareKey : compareValue;
  }

}
