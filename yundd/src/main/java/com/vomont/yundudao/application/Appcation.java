package com.vomont.yundudao.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.socks.library.KLog;
import com.squareup.okhttp.Request;
import com.vomont.yundudao.bean.IPInfo;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;

@SuppressLint("HandlerLeak")
public class Appcation extends Application
{

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler
    private static ACache aCache;//缓存
    // private IPInfo ipInfo;
    // 118.244.236.67:8051 192.168.0.88:8050
    public static String BASE_URL = "";

    private Handler handler;

    public static final String HOST = "http://www.vomont.com/yundd/addr.php";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate()
    {
        super.onCreate();

        //对全局属性赋值
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
        aCache=ACache.get(mContext);
        getURL();
    }

    /**
     * 重启当前应用
     */
    public static void restart() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        Appcation.mContext = mContext;
    }

    public static ACache getaCache() {
        return aCache;
    }

    public static void setaCache(ACache aCache) {
        Appcation.aCache = aCache;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static void setMainThread(Thread mMainThread) {
        Appcation.mMainThread = mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mMainThreadId) {
        Appcation.mMainThreadId = mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainThreadLooper(Looper mMainLooper) {
        Appcation.mMainLooper = mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler mHandler) {
        Appcation.mHandler = mHandler;
    }
    

    
    private void getURL() {
        // 判断缓存里是否已经有ip地址 如果有就使用缓存的ip地址 如果没有就获取ip地址
        if (aCache.getAsString("ip") != null) {
            BASE_URL = "http://" + aCache.getAsString("ip");

            String[] str = BASE_URL.split(":");
            if (str.length != 3) {
                BASE_URL = BASE_URL + ":8080";
            }
        } else {
            new Thread(new Runnable() {

                public void run() {
                    KLog.e("======================");
                    getUrl();

                }
            }).start();
        }
    }

    private void getUrl() {
        String   response= null;
        try {
            response = OkHttpClientManager.getAsString(HOST);
            KLog.e(response);
            IPInfo ipInfo = new IPInfo();
            JSONObject json = null;
            json = new JSONObject(response);
            String ip = json.getString("ip");
            String port = json.getString("port");
            ipInfo.setVfilesvrip(ip);
            ipInfo.setVfilesvrport(Integer.parseInt(port));
            String new_url = "http://" + ipInfo.getVfilesvrip() + ":" + ipInfo.getVfilesvrport();
            if (!new_url.equals(BASE_URL))
            {
                aCache.put("ip", ipInfo.getVfilesvrip() + ":" + ipInfo.getVfilesvrport());
                BASE_URL = new_url;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        HttpClient httpclient = new DefaultHttpClient();
//        HttpGet httpgets = new HttpGet(HOST);
//        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 2000);
//        HttpConnectionParams.setSoTimeout(httpclient.getParams(), 2000);
//        HttpResponse response;
//        try
//        {
//            IPInfo ipInfo = new IPInfo();
//            response = httpclient.execute(httpgets);
//            String jsonStr = EntityUtils.toString(response.getEntity());
//            JSONObject json = new JSONObject(jsonStr);
//            String ip = json.getString("ip");
//            String port = json.getString("port");
//            ipInfo.setVfilesvrip(ip);
//            ipInfo.setVfilesvrport(Integer.parseInt(port));
//            Message message = new Message();
//            message.what = 100;
//            message.obj = ipInfo;
//            handler.sendMessage(message);
//        }
//        catch (ClientProtocolException e)
//        {
//            Message message = new Message();
//            message.what = 10;
//            message.obj = BASE_URL;
//            handler.sendMessage(message);
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            Message message = new Message();
//            message.what = 10;
//            message.obj = BASE_URL;
//            handler.sendMessage(message);
//            e.printStackTrace();
//        }
//        catch (JSONException e)
//        {
//            Message message = new Message();
//            message.what = 10;
//            message.obj = BASE_URL;
//            handler.sendMessage(message);
//            e.printStackTrace();
//        }
    }

}
