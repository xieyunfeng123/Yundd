package com.vomont.yundudao.api;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public interface ApiService {

    public static String  TOP_HTTP="http://";

    public static String  HTTP_IP="192.168.0.185:8051";

    /**
     * 获取ip
     * @return ip地址
     */
    @FormUrlEncoded
    @POST("/")
    Observable<String> getLoginInfo();

}
