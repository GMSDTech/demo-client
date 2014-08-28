/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.sdk;

import com.gmsd.model.exception.GongmingApplicationException;
import com.gmsd.model.exception.GongmingConnectionException;
import com.gmsd.model.request.*;
import com.gmsd.model.response.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;

public class Gongming {

  private static final String PROTOCOL = "https";
  private static final String HOST = "admin-test.gongmingkeji.com";
  private static final Integer PORT = null;

  private Long merchantId;
  private String merchantSecret;

  private static final String CREATE_USER = "/v1/createUser";
  private static final String QUERY_USER_BY_IDENTITY_NUMBER = "/v1/queryUserByIdentityNumber";

  private static final String QUERY_PLANS = "/v1/queryPlans";

  private static final String ORDER_WITHOUT_PAYMENT = "/v1/orderWithoutPayment";
  private static final String QUERY_ORDER = "/v1/queryOrder";
  private static final String QUERY_ORDERS_BY_DATE = "/v1/queryOrdersByDate";
  private static final String QUERY_ORDER_INTEREST = "/v1/queryOrderInterest";

  private static final String CONFIRM_PAYMENT = "/v1/confirmPayment";

  /**
   * 初始化共鸣接口
   *
   * @param merchantId     商户id
   * @param merchantSecret 商户密钥
   */
  public Gongming(Long merchantId, String merchantSecret) {
    this.merchantId = merchantId;
    this.merchantSecret = merchantSecret;
  }

  /**
   * 理财账户创建
   *
   * @param request 创建理财账户请求
   * @return CreateUserResponse 创建理财账户结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public CreateUserResponse createUser(CreateUserRequest request)
      throws GongmingApplicationException, GongmingConnectionException {

    URL path = getPath(CREATE_USER);
    CreateUserResponse response = POST(path, request, CreateUserResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 根据身份证号的理财账户查询
   *
   * @param request 理财账户查询请求
   * @return QueryUserByIdentityNumberResponse 理财账户查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryUserByIdentityNumberResponse queryUserByIdentityNumber(QueryUserByIdentityNumberRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(QUERY_USER_BY_IDENTITY_NUMBER);
    QueryUserByIdentityNumberResponse response = GET(path, request, QueryUserByIdentityNumberResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 理财计划查询
   *
   * @param request 理财计划查询请求
   * @return QueryPlansResponse 理财计划查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryPlansResponse queryPlans(QueryPlansRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(QUERY_PLANS);
    QueryPlansResponse response = GET(path, request, QueryPlansResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 理财计划申购
   *
   * @param request 理财计划申购请求
   * @return OrderResponse 理财计划申购结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public OrderResponse orderWithoutPayment(OrderRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(ORDER_WITHOUT_PAYMENT);
    OrderResponse response = POST(path, request, OrderResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 订单查询
   *
   * @param request 订单查询请求
   * @return QueryOrderResponse 订单查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryOrderResponse queryOrder(QueryOrderRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(QUERY_ORDER);
    QueryOrderResponse response = GET(path, request, QueryOrderResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 按日期查询订单
   *
   * @param request 按日期查询订单请求
   * @return QueryOrdersByDateResponse 按日期查询订单结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryOrdersByDateResponse queryOrderByDate(QueryOrdersByDateRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(QUERY_ORDERS_BY_DATE);
    QueryOrdersByDateResponse response = GET(path, request, QueryOrdersByDateResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 订单收益查询
   *
   * @param request 订单收益查询请求
   * @return QueryOrderInterestResponse 订单收益查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryOrderInterestResponse queryOrderInterest(QueryOrdersByDateRequest.QueryOrderInterestRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(QUERY_ORDER_INTEREST);
    QueryOrderInterestResponse response = GET(path, request, QueryOrderInterestResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  /**
   * 确认支付结果
   *
   * @param request 确认支付结果请求
   * @return ConfirmPaymentResponse 确认支付结果反馈
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public ConfirmPaymentResponse confirmPayment(ConfirmPaymentRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    URL path = getPath(CONFIRM_PAYMENT);
    ConfirmPaymentResponse response = POST(path, request, ConfirmPaymentResponse.class);

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }
    return response;
  }

  private URL getPath(String path) throws GongmingConnectionException {
    URL queryURL = null;
    try {
      if (PORT != null) {
        queryURL = new URL(PROTOCOL, HOST, PORT, path);
      } else {
        queryURL = new URL(PROTOCOL, HOST, path);
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return queryURL;
  }

  private <T extends GMResponseBase> T GET(URL url, GMRequestBase request, Class<T> responseType)
      throws GongmingConnectionException {

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<T> responseEntity;
    T response;

    try {
      responseEntity = restTemplate.getForEntity(
          url.toString() + "?" + request.toQuery(url.getPath(), merchantSecret), responseType);
      response = responseEntity.getBody();
    } catch (HttpStatusCodeException e) {
      // This exception has status code, e.g. 404 Not Found
      throw new GongmingConnectionException(e.getStatusCode(), e);
    } catch (InvalidKeyException e) {
      throw new GongmingConnectionException("商户密钥不合法", e);
    }
    return response;
  }

  private <T extends GMResponseBase> T POST(URL url, GMRequestBase request, Class<T> responseType)
      throws GongmingConnectionException {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<T> responseEntity;
    T response;

    try {
      HttpEntity<String> entity = new HttpEntity<>(request.toQuery(url.getPath(), merchantSecret), headers);
      responseEntity = restTemplate.postForEntity(url.toString(), entity, responseType);
      response = responseEntity.getBody();
    } catch (HttpStatusCodeException e) {
      // This exception has status code, e.g. 404 Not Found
      throw new GongmingConnectionException(e.getStatusCode(), e);
    } catch (InvalidKeyException e) {
      throw new GongmingConnectionException("商户密钥不合法", e);
    }
    return response;
  }

}
