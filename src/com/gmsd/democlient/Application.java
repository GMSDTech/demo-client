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

  private static Long merchantId = 0L;             // 在此处输入商户ID
  private static String merchantSecret = "";       // 在此处写入商户密钥

  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static final Long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000L;
  private static final Integer ID_PRE_CHECK_BIT_SIZE = 17;

  public Application() {
  }

  public static void main(String args[]) {
    Gongming gongming = new Gongming(merchantId, merchantSecret);

    try {
      String userIdentityNumber = generateIdCardNumber();

      logger.info("创建理财账户");
      GongmingRequest createUserRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userPhoneNumber", "18680008888")               // 手机号
          .addParameter("userIdentityNumber", userIdentityNumber)       // 身份证号
          .addParameter("userName", "ManagerOfMoney")                   // 真实姓名
          .addParameter("email", "somebody@somewhere.com")              // email 地址，可选
          .addParameter("nickname", "Nickname")                         // 昵称，可选
          .addParameter("gender", "Male")                               // 性别，可选
          .addParameter("address", "Beijing")                           // 地址，可选
          .addParameter("educationLevel", "Bachelor");                  // 学历，可选
      CreateUserResponse createUserResponse = gongming.createUser(createUserRequest);
      logger.info("创建理财账户成功，理财账户号" + createUserResponse.userId);

      Long userId = createUserResponse.userId;
      String bankCardNumber = "6226" + String.format("%012d", new Random().nextInt(1000000000));

      logger.info("支付平台绑卡");
      GongmingRequest bindBankCardRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userId", userId.toString())                    // 用户ID
          .addParameter("bankCode", "CMB")                              // 银行编号 CMB=招商银行
          .addParameter("bankAccountNumber", bankCardNumber)            // 银行卡号
          .addParameter("bankPhoneNumber", "18680008888")               // 银行预留手机号
          .addParameter("bankProvince", "北京市")                        // 银行省份
          .addParameter("bankCity", "北京市")                            // 银行市
          .addParameter("bankBranchName", "朝阳门支行");                  // 支行名称
      BindBankCardResponse bindBankCardResponse = gongming.bindBankCard(bindBankCardRequest);
      logger.info("绑卡成功");

      logger.info("更新用户信息");
      GongmingRequest updateUserRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号，必填
          .addParameter("userId", userId.toString())                    // 用户ID，必填
          .addParameter("userPhoneNumber", "18680009999")               // 手机号码，可选
          .addParameter("email", "somebody@somewhereelse.com")          // 用户 email 地址，可选
          .addParameter("nickname", "Nickname")                         // 用户昵称，可选
          .addParameter("gender", "男")                                 // 用户性别，可选
          .addParameter("address", "北京市朝阳区朝外SOHO")                // 用户地址，可选
          .addParameter("educationLevel", "本科");                      // 用户学历，可选
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
          .addParameter("merchantRequestId", merchantRequestId)         // 商户订单号
          .addParameter("planId", planId.toString())                    // 计划ID
          .addParameter("merchantOrderDate", today)                     // 订单日期
          .addParameter("bankAccountNumber", bankCardNumber);           // 银行卡号
      OrderWithPaymentResponse orderWithPaymentResponse = gongming.orderWithPayment(orderWithPaymentRequest);
      logger.info("理财计划申购成功，成功申购" + orderWithPaymentResponse.successAmount + "元");

      // 若上一个申购操作成功，查询订单状态
      logger.info("查询订单");
      GongmingRequest queryOrderRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("merchantRequestId", merchantRequestId);        // 订单号
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
          .addParameter("merchantRequestId", merchantRequestId);        // 订单号
      QueryOrderInterestResponse queryOrderInterestResponse = gongming.queryOrderInterest(queryOrderInterestRequest);
      logger.info("查询订单收益成功，当日收益" + queryOrderInterestResponse.dailyInterest + "元，" +
          "累计收益" + queryOrderInterestResponse.totalInterest + "元");

      logger.info("申请赎回");
      GongmingRequest redeemRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("merchantRequestId", merchantRequestId);        // 商户订单号
      RedeemResponse redeemResponse = gongming.redeem(redeemRequest);
      logger.info("赎回申请已收到");

      logger.info("解除银行卡绑定");
      GongmingRequest unbindBankCardRequest = new GongmingRequest()
          .addParameter("merchantId", merchantId.toString())            // 商户号
          .addParameter("userId", userId.toString())                    // 用户ID
          .addParameter("bankAccountNumber", bankCardNumber);           // 银行卡号
      UnbindBankCardResponse unbindBankCardResponse = gongming.unbindBankCard(unbindBankCardRequest);
      logger.info("解除银行卡绑定成功: " + unbindBankCardResponse);

    } catch (GongmingConnectionException e) {
      logger.info("连接失败：" + e.toString());
    } catch (GongmingApplicationException e) {
      logger.info("后台处理异常：" + e.toString());
    }
  }

  /*
   * <p>
   * 随机生成 18 位身份证号。此方法非必要，只需使用真实身份证号即可。
   * </p>
   * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，
   * 由十七位数字本体码和一位数字校验码组成。 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
   * <p>
   * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
   * </p>
   * <p>
   * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
   * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
   * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
   * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性 。校检码可以是0~9的数字，有时也用x表示。
   * </p>
   * <p>
   * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
   * 2 1 6 3 7 9 10 5 8 4 2
   * </p>
   * <p>
   * 2.将这17位数字和系数相乘的结果相加。
   * </p>
   * <p>
   * 3.用加出来和除以11，看余数是多少？
   * </p>
   * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
   * 2。
   * <p>
   * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
   * </p>
   */
  private static String generateIdCardNumber() {
    // 1-6 位，北京市朝阳区
    String idCardDistrict = "110105";

    // 7-14 位，1970-01-01 后约 20 年内的随机日期，不考虑闰年因素
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyyMMdd");
    String idCardBirthday = birthdayFormat.format(new Date(MILLISECONDS_PER_DAY * (new Random().nextInt(365 * 20))));

    // 15-17 位，顺序码，使用奇数（男性）
    Integer sequence = new Random().nextInt(1000);
    if ((sequence % 2 == 0)) {
      sequence = (sequence + 1) % 1000;
    }
    String idCardSequence = String.format("%03d", sequence);

    int idCardBits[] = getIdCardBits(idCardDistrict + idCardBirthday + idCardSequence);

    // 18 位，检验码，根据前 17 位数字生成
    String idCardCheckCode = getCheckCode(idCardBits);

    return idCardDistrict + idCardBirthday + idCardSequence + idCardCheckCode;
  }

  /*
   * 把身份证号前 17 位转成数字数组
   */
  private static int[] getIdCardBits(String idCard17) {
    assert(idCard17.length() == ID_PRE_CHECK_BIT_SIZE);

    int idCardBits[] = new int[ID_PRE_CHECK_BIT_SIZE];
    for (int i = 0; i < ID_PRE_CHECK_BIT_SIZE; i++) {
      idCardBits[i] = Integer.parseInt(idCard17.substring(i, i + 1));
    }
    return idCardBits;
  }

  /*
   * 生成身份证号第 18 位检验码
   * 步骤：
   * 1. 生成身份证前 17 位的加权和
   * 2. 加权和余 11 后得到的 0 ~ 10 数字，对应一个校验数字或字母'X'
   */
  private static String getCheckCode(int[] idCardBits) {
    final int POWER[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    final char CHECK_CODES[] = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    final int CHECK_NUMERATOR = 11;

    assert(idCardBits.length == ID_PRE_CHECK_BIT_SIZE);

    Integer powerSum = 0;
    for (int i = 0; i < ID_PRE_CHECK_BIT_SIZE; i++) {
      powerSum += idCardBits[i] * POWER[i];
    }

    char checkCode = CHECK_CODES[powerSum % CHECK_NUMERATOR];
    return String.valueOf(checkCode);
  }
}
