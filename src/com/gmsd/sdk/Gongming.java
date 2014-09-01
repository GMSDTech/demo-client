/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.sdk;

import com.gmsd.model.exception.GongmingApplicationException;
import com.gmsd.model.exception.GongmingConnectionException;
import com.gmsd.model.request.GongmingRequest;
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

  private static final String QUERY_PLANS = "/v1/queryPlans";

  private static final String CREATE_USER = "/v1/createUser";
  private static final String BIND_BANK_CARD = "/v1/bindBankCard";
  private static final String UPDATE_USER = "/v1/updateUser";
  private static final String QUERY_USER_BY_IDENTITY_NUMBER = "/v1/queryUserByIdentityNumber";

  private static final String ORDER_WITH_PAYMENT = "/v1/orderWithPayment";
  private static final String REDEEM = "/v1/redeem";

  private static final String QUERY_ORDER = "/v1/queryOrder";
  private static final String QUERY_ORDERS_BY_DATE = "/v1/queryOrdersByDate";
  private static final String QUERY_ORDER_INTEREST = "/v1/queryOrderInterest";

  /**
   * 理财计划查询
   *
   * @param request 理财计划查询请求
   * @return QueryPlansResponse 理财计划查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryPlansResponse queryPlans(GongmingRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    return call(QUERY_PLANS, false, request, QueryPlansResponse.class);
  }

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
  public CreateUserResponse createUser(GongmingRequest request)
      throws GongmingApplicationException, GongmingConnectionException {

    return call(CREATE_USER, true, request, CreateUserResponse.class);
  }

  /**
   * 支付平台绑卡
   *
   * @param request 绑卡请求
   * @return 请求结果
   * @throws GongmingApplicationException
   * @throws GongmingConnectionException
   */
  public BindBankCardResponse bindBankCard(GongmingRequest request)
      throws GongmingApplicationException, GongmingConnectionException {

    return call(BIND_BANK_CARD, true, request, BindBankCardResponse.class);
  }

  /**
   * 更新用户信息
   *
   * @param request 更新用户信息请求
   * @return UdateUserResponse 更新结果
   * @throws GongmingApplicationException
   * @throws GongmingConnectionException
   */
  public UpdateUserResponse updateUser(GongmingRequest request)
      throws GongmingApplicationException, GongmingConnectionException {

    return call(UPDATE_USER, true, request, UpdateUserResponse.class);
  }

  /**
   * 根据身份证号的理财账户查询
   *
   * @param request 理财账户查询请求
   * @return QueryUserByIdentityNumberResponse 理财账户查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryUserByIdentityNumberResponse queryUserByIdentityNumber(GongmingRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    return call(QUERY_USER_BY_IDENTITY_NUMBER, false, request, QueryUserByIdentityNumberResponse.class);
  }

  /**
   * 理财计划申购
   *
   * @param request 申购请求
   * @return OrderWithPaymentResponse 申购结果
   * @throws GongmingApplicationException
   * @throws GongmingConnectionException
   */
  public OrderWithPaymentResponse orderWithPayment(GongmingRequest request)
      throws GongmingApplicationException, GongmingConnectionException {

    return call(ORDER_WITH_PAYMENT, true, request, OrderWithPaymentResponse.class);
  }

  /**
   * 赎回并体现
   *
   * @param request 赎回请求
   * @return 赎回请求结果
   * @throws GongmingApplicationException
   * @throws GongmingConnectionException
   */
  public RedeemResponse redeem(GongmingRequest request)
      throws GongmingApplicationException, GongmingConnectionException {

    return call(REDEEM, true, request, RedeemResponse.class);
  }

  /**
   * 订单查询
   *
   * @param request 订单查询请求
   * @return QueryOrderResponse 订单查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryOrderResponse queryOrder(GongmingRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    return call(QUERY_ORDER, false, request, QueryOrderResponse.class);
  }

  /**
   * 按日期查询订单
   *
   * @param request 按日期查询订单请求
   * @return QueryOrdersByDateResponse 按日期查询订单结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryOrdersByDateResponse queryOrderByDate(GongmingRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    return call(QUERY_ORDERS_BY_DATE, false, request, QueryOrdersByDateResponse.class);
  }

  /**
   * 订单收益查询
   *
   * @param request 订单收益查询请求
   * @return QueryOrderInterestResponse 订单收益查询结果
   * @throws GongmingConnectionException  连接异常
   * @throws GongmingApplicationException 后台异常
   */
  public QueryOrderInterestResponse queryOrderInterest(GongmingRequest request)
      throws GongmingConnectionException, GongmingApplicationException {

    return call(QUERY_ORDER_INTEREST, false, request, QueryOrderInterestResponse.class);
  }

  private <T extends GMResponseBase> T call(String path, Boolean isPost, GongmingRequest request, Class<T> responseType)
      throws GongmingApplicationException, GongmingConnectionException {

    URL url = getPath(path);
    T response;

    if (isPost) {
      response = POST(url, request, responseType);
    } else {
      response = GET(url, request, responseType);
    }

    if (response.code != GMResponseCode.COMMON_SUCCESS) {
      throw new GongmingApplicationException(response.code.toString(), response.errorMessage);
    }

    return response;
  }

  private URL getPath(String path) throws GongmingConnectionException {
    URL queryURL = null;
    try {
      queryURL = (PORT != null ?
          new URL(PROTOCOL, HOST, PORT, path) :
          new URL(PROTOCOL, HOST, path));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return queryURL;
  }

  private <T extends GMResponseBase> T GET(URL url, GongmingRequest request, Class<T> responseType)
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
      throw new GongmingConnectionException(e.getStatusCode(), e.getResponseBodyAsString(), e);
    } catch (InvalidKeyException e) {
      throw new GongmingConnectionException("商户密钥不合法", e);
    }

    return response;
  }

  private <T extends GMResponseBase> T POST(URL url, GongmingRequest request, Class<T> responseType)
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
      throw new GongmingConnectionException(e.getStatusCode(), e.getResponseBodyAsString(), e);
    } catch (InvalidKeyException e) {
      throw new GongmingConnectionException("商户密钥不合法", e);
    }

    return response;
  }

}
