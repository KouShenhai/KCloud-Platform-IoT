package io.laokou.common.constant;

/**
 * 常量
 * @author Kou Shenhai
 */
public interface Constant {

    String BEARER = "Bearer ";

    String PERMISSIONS = "permissions";

    String EXPIRES_IN = "expires_in";

    String APP_KEY = "appId";

    String ACCESS_TOKEN = "access_token";

    String OUT_TRADE_NO = "out_trade_no";

    String TOTAL_AMOUNT = "total_amount";

    String SUBJECT = "subject";

    String URI = "uri";

    String METHOD = "method";

    Integer START_VERSION = 1;

    String TYPE = "type";
    /**
     * index
     */
    String INDEX = "index";

    String UUID = "uuid";
    /**
     * data
     */
    String DATA = "data";
    /**
     * 是否删除
     */
    String IS_DEL = "isDel";
    /**
     * Authorization header
     */
    String AUTHORIZATION_HEADER = "Authorization";
    /**
     * basic
     */
    String BASIC = "Basic ";
    /**
     * 成功
     */
    Integer SUCCESS = 1;
    /**
     * 失败
     */
    Integer FAIL = 0;
    /**
     * header
     */
    String HEADER = "header";
    /**
     * ticket
     */
    String TICKET = "ticket";
    /**
     * OK
     */
    String OK = "OK";
    /**
     * 用户标识
     */
    String USER_KEY_HEAD = "userId";
    /**
     * 用户名
     */
    String USERNAME_HEAD = "username";
    /**
     * 当前页码
     */
    String PAGE = "page";
    /**
     * 每页显示记录数
     */
    String LIMIT = "limit";
    /**
     *  升序
     */
    String ASC = "asc";
    /**
     * 降序
     */
    String DESC = "desc";
    /**
     * 排序字段
     */
    String ORDER_FIELD = "orderField";
    /**
     * 排序方式
     */
    String ORDER = "order";
    /**
     * 1是
     */
    Integer YES = 1;
    /**
     * 0否
     */
    Integer NO = 0;
    /**
     * 逗号
     */
    String COMMA = ",";
    /**
     * 空格
     */
    String SPACE = " ";
    /**
     *
     */
    String POINT = ".";
    /**
     * 左括号
     */
    String LEFT = "(";
    /**
     * 由括号
     */
    String RIGHT = ")";
    /**
     * 等于
     */
    String EQUAL = "=";
    String AND = "&";
    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sqlFilter";
    /**
     * 自定义分词器
     */
    Integer IK_INDEX = 3;
    /**
     * 无分词器
     */
    Integer NOT_ANALYZED = 0;
}
