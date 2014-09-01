package com.gmsd.model.request;

import com.gmsd.api.APISigner;
import org.springframework.util.StringUtils;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GongmingRequest {

  protected List<Parameter> parameters;

  public GongmingRequest() {
    parameters = new ArrayList<>();
  }

  public GongmingRequest addParameter(Parameter parameter) {
    parameters.add(parameter);

    return this;
  }

  public GongmingRequest addParameter(String key, String value) {
    parameters.add(new Parameter(key, value));

    return this;
  }

  public String toString() {
    return StringUtils.collectionToDelimitedString(parameters, "&");
  }

  /**
   * 功能和 toString() 一样，但是每个字段值进行了 URL 编码，并添加 signature 字段
   */
  public String toQuery(String path, String merchantSecret) throws InvalidKeyException {
    Collections.sort(parameters);
    String signature = APISigner.sign(path + "?" + this.toString(), merchantSecret);

    this.addParameter(new Parameter("signature", signature));

    String joined = "";
    for (Parameter parameter : parameters) {
      if (!joined.isEmpty()) {
        joined += "&";
      }

      joined += parameter.toQuery();
    }

    return joined;
  }
}
