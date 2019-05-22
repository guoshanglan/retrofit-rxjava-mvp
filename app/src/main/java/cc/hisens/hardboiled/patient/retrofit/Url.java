package cc.hisens.hardboiled.patient.retrofit;

public class Url {


    //设置默认超时时间
    public static final int DEFAULT_TIME=10;

    public static String BaseUrl="https://www.hisens.cc";   //部署到服务器上的正式地址

    public static String BaseUrl2="http://10.0.0.200:8080";  //测试地址

    //患者用户登录接口
    public static String paientLogin="/api/v1/user/login";

    //患者User获取手机验证码
    public static String getVerificationCode="/api/v1/user/phone_code";

   //上传APP新版本信息
    public static String uploadVersion="/api/v1/device/version";

    //检查APP版本是否有更新
    public static String getAppInfo="/api/v1/device/version";


    //websocket连接地址
    public static final String WEB_SOCKET_URL = "http://10.0.0.200:8080/api/v1/ws";





}
