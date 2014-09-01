/*
 * Copyright 2014-present GMSD tech inc. All Rights Reserved.
 */

package com.gmsd.democlient;

import com.gmsd.model.exception.GongmingApplicationException;
import com.gmsd.model.exception.GongmingConnectionException;
import com.gmsd.model.request.GongmingRequest;
import com.gmsd.model.response.*;
import com.gmsd.sdk.Gongming;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class Application {

  private static Logger logger = Logger.getLogger("application");

  private static Long merchantId = 1L;
  private static String merchantSecret = "ABCDEFG1234ABCDEFG1234";

  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static final Long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000L;

  public Application() {
  }

  public static void main(String args[]) {
    Gongming gongming = new Gongming(merchantId, merchantSecret);

    try {
      String userIdentityNumber = "1" + String.format("%017d", new Random().nextInt(1000000000));

      logger.info("创建理财账户");
      GongmingRequest createUserRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userPhoneNumber", "18680008888")               // 手机号
          .addParameter("userIdentityNumber", userIdentityNumber)       // 身份证号
          .addParameter("userName", "ManagerOfMoney");                  // 真实姓名
      CreateUserResponse createUserResponse = gongming.createUser(createUserRequest);
      logger.info("创建理财账户成功，理财账户号" + createUserResponse.userId);

      Long userId = createUserResponse.userId;
      String bankCardNumber = "6226" + String.format("%012d", new Random().nextInt(1000000000));

      logger.info("支付平台绑卡");
      GongmingRequest bindBankCardRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userId", userId.toString())                    // 用户ID
          .addParameter("bankCode", "B007")                             // 银行编号 B007=招商银行
          .addParameter("bankAccountNumber", bankCardNumber)            // 银行卡号
          .addParameter("bankPhoneNumber", "18680008888");              // 银行预留手机号
      BindBankCardResponse bindBankCardResponse = gongming.bindBankCard(bindBankCardRequest);
      logger.info("绑卡成功");

      logger.info("更新用户信息");
      GongmingRequest updateUserRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userId", userId.toString())                    // 用户ID
          .addParameter("email", "somebody@somewhere.com")              // 用户 email 地址
          .addParameter("nickname", "Nick")                             // 用户昵称
          .addParameter("gender", "男")                                 // 用户性别
          .addParameter("adderss", "北京市朝阳区朝外SOHO")                // 用户地址
          .addParameter("educationLevel", "本科");                      // 用户学历
      UpdateUserResponse updateUserResponse = gongming.updateUser(updateUserRequest);
      logger.info("更新用户信息成功");

      logger.info("根据身份证号查询理财账户");
      GongmingRequest queryUserByIdentityNumberRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userIdentityNumber", userIdentityNumber);      // 身份证号
      QueryUserByIdentityNumberResponse queryUserByIdentityNumberResponse =
          gongming.queryUserByIdentityNumber(queryUserByIdentityNumberRequest);
      logger.info("根据身份证号查询理财账户成功，理财账户号为" + queryUserByIdentityNumberResponse.user.id);

      logger.info("查询理财计划");
      GongmingRequest queryPlansRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString());           // 商户号
      QueryPlansResponse queryPlansResponse = gongming.queryPlans(queryPlansRequest);
      logger.info("查询理财计划成功，返回理财计划" + queryPlansResponse.plans.size() + "个");

      if (queryPlansResponse.plans.size() == 0) {
        logger.info("当前没有可购买的理财计划，退出");
        return;
      }

      Long planId = queryPlansResponse.plans.get(0).id;
      String orderAmount = queryPlansResponse.plans.get(0).minAmount;
      String merchantRequestId = String.format("%015d", new Random().nextInt(1000000000));
      String today = simpleDateFormat.format(new Date());

      logger.info("理财计划申购");
      GongmingRequest orderWithPaymentRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userId", userId.toString())                    // 理财账户号
          .addParameter("orderAmount", orderAmount)                     // 申购金额
          .addParameter("merchantRequestId", merchantRequestId)         // 商户请求ID
          .addParameter("planId", planId.toString())                    // 计划ID
          .addParameter("orderDate", today);                            // 订单日期
      OrderWithPaymentResponse orderWithPaymentResponse = gongming.orderWithPayment(orderWithPaymentRequest);
      logger.info("理财计划申购成功，订单号" + orderWithPaymentResponse.orderId + "，" +
          "成功申购" + orderWithPaymentResponse.successAmount + "元");

      Long orderId = orderWithPaymentResponse.orderId;

      // 若上一个申购操作成功，查询订单状态
      logger.info("查询订单");
      GongmingRequest queryOrderRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("orderId", orderId.toString());                 // 订单号
      QueryOrderResponse queryOrderResponse = gongming.queryOrder(queryOrderRequest);
      logger.info("订单查询成功，状态为" + queryOrderResponse.order.status.toString() + "");


      String startDate = simpleDateFormat.format(new Date());
      String endDate = simpleDateFormat.format(new Date(System.currentTimeMillis() + 1 * MILLISECONDS_PER_DAY));

      logger.info("按日期查询订单，查询日期区间： " + startDate + " 到 " + endDate);
      GongmingRequest queryOrderByDateRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("startDate", startDate)                         // 范围开始日期
          .addParameter("endDate", endDate);                            // 范围结束日期
      QueryOrdersByDateResponse queryOrdersByDateResponse = gongming.queryOrderByDate(queryOrderByDateRequest);
      logger.info("按日期查询订单成功，共有" + queryOrdersByDateResponse.orders.size() + "个订单");

      logger.info("查询订单收益");
      GongmingRequest queryOrderInterestRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("orderId", orderId.toString());                 // 订单号
      QueryOrderInterestResponse queryOrderInterestResponse = gongming.queryOrderInterest(queryOrderInterestRequest);
      logger.info("查询订单收益成功，当日收益" + queryOrderInterestResponse.dailyInterest + "元，" +
          "累计收益" + queryOrderInterestResponse.totalInterest + "元");

      logger.info("申请赎回");
      GongmingRequest redeemRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("orderId", orderId.toString());                 // 订单号
      RedeemResponse redeemResponse = gongming.redeem(redeemRequest);
      logger.info("赎回申请已收到");

    } catch (GongmingConnectionException e) {
      logger.info("连接失败：" + e.toString());
    } catch (GongmingApplicationException e) {
      logger.info("后台处理异常：" + e.toString());
    }
  }
}
