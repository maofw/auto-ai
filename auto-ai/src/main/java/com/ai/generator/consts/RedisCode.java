package com.ai.generator.consts;

/**
 * @author Administrator
 */
public class RedisCode {

    //登录缓存时间秒 默认一个月时间
    public static final long LOGIN_TIMES = 2592000L;
    //未读消息缓存时间1个月
    public static final long UNREADTIMEOUT = 30 * 24 * 60 * 60 * 1000L;
    /**
     * 聊天列表
     */
    private static final String IM_LIST = "im_list";
    /**
     * 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private static final String PREPAY_ID = "prepay_id";
    /**
     * 头
     */
    private static final String PROJECT_NAME = "ONE_STAND";
    /**
     * 聊天记录key
     */
    private static final String SOCKET_MESSAGE = "message";
    /**
     * 通道key
     */
    private static final String SOCKET_NAME = "socket";
    private static final String SYMBOL = ":";
    /**
     * token key
     */
    private static final String TOKEN_NAME = "login_token";
    /**
     * 未读消息
     */
    private static final String UNREAD_MESSAGE = "unread_message";
    private static final String UNREAD_NUM = "unread_num";
    /**
     * 支付订单
     */
    private static final String USER_ORDER = "user_order";
    /**
     * 退款订单
     */
    private static final String REFUDN_ORDER = "refund_order";
    /**
     * 大头
     */
    private static final String USER_PROJECT = "USER";
    /**
     * 名片
     */
    private static final String CARD_POSTER = "card_poster";
    /**
     * 分布式请求锁
     */
    private static final String REQUEST_LOCK = "REQUEST_LOCK";
    /**
     * 微信模板token
     */
    private static final String WX_ACCESS_TOKEN = "wx_access_token";
    /**
     * 微信商户支付key值
     */
    private static final String WX_KEY = "wx_key";

    /**
     * 总后台登录
     */
    private static final String ADMIN_TOKEN = "admin_token";

    private static final String COMPONENT_TOKEN = "component_token";
    private static final String TICKET = "ticket";
    private static final String authorizerRefreshToken = "AuthorizerRefreshToken";
    private static final String getAuthorizerToken = "getAuthorizerToken";


    /**
     * SMS验证码5分钟内有效
     */
    private static final String SMS_CODE = "sms_code";
    /**
     * SMS验证码验证
     */
    private static final String SMS_CODE_VERIFY = "sms_code_verify";

    /**
     * 预约订单签到的KEY
     */
    private static final String REGISTRATION_ORDER_SING = "registration_order_sign";

    /**
     * 专科服务订单某日期、某套餐已约数量
     */
    private static final String PROFESSION_ORDER_NUM = "profession_order_num";

    /**
     * 用户登录防止并发redis lock key
     */
    private static final String LOGIN_USER_LOCKKEY = "login_user_lockkey";

    /**
     * 短信验证码缓存key
     */
    public static String getSmsCode(String phone) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(SMS_CODE).append(SYMBOL).append(phone);
        return stringBuffer.toString();
    }

    /**
     * 短信验证码缓存key
     */
    public static String getSmsCodeVerify(String phone) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(SMS_CODE_VERIFY).append(SYMBOL).append(phone);
        return stringBuffer.toString();
    }


    /**
     * 总后台登录 token
     */
    public static String getAdminToken(String userId) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(ADMIN_TOKEN).append(SYMBOL).append(userId);
        return stringBuffer.toString();
    }

    /**
     * 获取授权方小程序信息
     *
     * @param appid
     * @return
     */
    public static String getAuthorizerRefreshTokenKey(String appid) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(authorizerRefreshToken).append(SYMBOL).append(appid);
        return stringBuffer.toString();
    }

    public static String getAuthorizerToken(String appid) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(getAuthorizerToken).append(SYMBOL).append(appid);
        return stringBuffer.toString();
    }

    /**
     * 微信第三方平台推送的ticket
     *
     * @return
     */
    public static String getTicket(String appId) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(TICKET).append(SYMBOL).append(appId);
        return stringBuffer.toString();
    }

    /**
     * 获取第三方平台token
     *
     * @param appid
     * @return
     */
    public static String getComponentTokenKey(String appid) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(COMPONENT_TOKEN).append(SYMBOL).append(appid);
        return stringBuffer.toString();
    }


    /**
     * 获取微信token
     *
     * @param appId
     * @return
     */
    public static String getWxAccessToken(String appId) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(WX_ACCESS_TOKEN).append(SYMBOL).append(appId);
        return stringBuffer.toString();
    }


    /**
     * 获取好友列表
     *
     * @return
     */
    public static String getImList(Long id, String loginProgramName) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(IM_LIST).append(SYMBOL).append(loginProgramName).append(SYMBOL).append(id);
        return stringBuffer.toString();
    }

    /**
     * 请求锁
     *
     * @param key
     * @return
     */
    public static String requestLockCode(String key) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(REQUEST_LOCK).append(SYMBOL).append(key).append(SYMBOL);
        return stringBuffer.toString();
    }

    /**
     * 微信生成的预支付会话标识，用于后续接口调用中使用(微信消息模板推送)，该值有效期为2小时
     *
     * @param prepayId
     * @return
     */
    public static String getPrepayId(String prepayId) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(PREPAY_ID).append(SYMBOL).append(prepayId);
        return stringBuffer.toString();
    }

    /**
     * 未读消息rediskey
     *
     * @param myId     发送者id
     * @param targetId 消息接受人id
     * @param type 接收人类型 0-用户 1-医生
     * @return
     */
    public static String getUnreadMessage(Long myId, Long targetId, Integer type) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(UNREAD_MESSAGE).append(SYMBOL).append(type).append(SYMBOL).append(myId).append("-").append(targetId);
        return stringBuffer.toString();
    }

    /**
     * 登陆token
     *
     * @return
     */
    public static String getUserLoginToken(String token) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(TOKEN_NAME).append(SYMBOL).append(token);
        return stringBuffer.toString();
    }

    private static StringBuffer index(String po) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(PROJECT_NAME).append(SYMBOL).append(po).append(SYMBOL);
        return stringBuffer;
    }

    /**
     * 获取订单redisKye
     *
     * @param orderNo
     * @return
     */
    public static String getUserOrderKey(String orderNo) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(USER_ORDER).append(SYMBOL).append(orderNo);
        return stringBuffer.toString();
    }

    /**
     * 获取退款订单redisKye
     *
     * @param refundNo
     * @return
     */
    public static String getRefundOrderKey(String refundNo) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(REFUDN_ORDER).append(SYMBOL).append(refundNo);
        return stringBuffer.toString();
    }

    /**
     * 海报
     *
     * @param cardId
     * @return
     */
    public static String getCardPoster(Integer cardId) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(CARD_POSTER).append(SYMBOL).append(cardId);
        return stringBuffer.toString();
    }

    /**
     * 用户socket 通道存储
     * @param id  id
     * @param loginType 登录用户类型  用户|医生
     * @return
     */
    public static String getUserWebSocket(Long id, Integer loginType) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(SOCKET_NAME).append(SYMBOL).append(loginType).append(SYMBOL).append(id);
        return stringBuffer.toString();
    }

    /**
     * 聊天记录key
     *
     * @param sendId   发送人id
     * @param targetId 接受人id
     * @return
     */
    public static String getUserWebSocketMessageRecord(Long sendId, Long targetId, String loginProgramName) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(SOCKET_MESSAGE).append(SYMBOL).append(loginProgramName).append(SYMBOL).append(sendId).append("-").append(targetId);
        return stringBuffer.toString();
    }

    /**
     * 微信商户支付key值
     *
     * @param nonce_str 随机数
     * @return
     */
    public static String getWxKey(String nonce_str) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(WX_KEY).append(SYMBOL).append(nonce_str);
        return stringBuffer.toString();
    }

    public static String getUserWebSocketMessageRecord(String sendId, String targetId, String loginProgramName) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(SOCKET_MESSAGE).append(SYMBOL).append(loginProgramName).append(SYMBOL).append(sendId).append("-").append(targetId);
        return stringBuffer.toString();
    }

    /**
     * 获取未读消息数 key
     * @param
     * @return
     */
    public static String getUnreadNum(Long id, Integer loginType) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(UNREAD_NUM).append(SYMBOL).append(loginType).append(SYMBOL).append(id);
        return stringBuffer.toString();
    }

    /**
     * 预约订单签到序号的key
     *
     * @return
     */
    public static String getRegistrationOrderSignNum(String hospitalId, String deptId, String yyhDate) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(REGISTRATION_ORDER_SING).append(SYMBOL).append(hospitalId).append(SYMBOL).append(deptId).append(SYMBOL).append(yyhDate);
        return stringBuffer.toString();
    }

    /**
     * 专科服务订单某日期、某套餐已约数量
     *
     * @return
     */
    public static String getProfessionOrdeNum(String tjTime, String pkgId) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(PROFESSION_ORDER_NUM).append(SYMBOL).append(tjTime).append(SYMBOL).append(pkgId);
        return stringBuffer.toString();
    }

    public static String getLoginUserLockkey(String phone) {
        StringBuffer stringBuffer = new StringBuffer(index(USER_PROJECT));
        stringBuffer.append(LOGIN_USER_LOCKKEY).append(SYMBOL).append(phone);
        return stringBuffer.toString();
    }


    public static final String REDIS_DB_CACHE = "redis_db_cache";

}
