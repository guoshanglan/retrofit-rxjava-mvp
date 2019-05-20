package cc.hisens.hardboiled.patient.retrofit;

public class Url {


    //设置默认超时时间
    public static final int DEFAULT_TIME=10;

    public static String BaseUrl="https://www.hisens.cc";   //部署到服务器上的正式地址

    public static String BaseUrl2="http://10.0.0.200:8080";  //测试地址

    //患者用户登录接口
    public static String paientLogin="/v1/user/login";

    //患者User获取手机验证码
    public static String getVerificationCode="/v1/user/phone_code";


    //检查APP版本是否有更新
    public static String getAppInfo="/v1/config/version";


    //websocket连接测试地址
    public static final String WEB_SOCKET_URL = "ws://echo.websocket.org";





}
