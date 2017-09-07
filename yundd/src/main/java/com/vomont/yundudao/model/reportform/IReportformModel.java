package com.vomont.yundudao.model.reportform;

import java.io.IOException;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.OkHttpClientManager;
import com.vomont.yundudao.utils.OkHttpClientManager.Param;

public class IReportformModel
{
    
    public void getFormList(String userid, long starttime, long endtime, final OnHttpListener onHttpListener)
    {
        // HTTP_TYPE.type1
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type22);
        reParams.add(HTTP_PAMRS.userid, userid);
        reParams.add("starttime", starttime + "");
        reParams.add("endtime", endtime + "");
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                
                Gson gson = new Gson();
                FormBean formBean = gson.fromJson(responseBody, FormBean.class);
                onHttpListener.onSucess(formBean);
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
    }
    
    public String getFormByType(String userid, long starttime, long endtime, String typeid, String subfactoryid)
    {
        // RequestParams reParams = new RequestParams();
        // reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type23);
        // reParams.add(HTTP_PAMRS.userid, userid);
        // reParams.add("starttime", starttime+"");
        // reParams.add("endtime", endtime+"");
        // reParams.add("typeid", typeid+"");
        // reParams.add("subfactoryid", subfactoryid+"");
        
        Param param1 = new Param(HTTP_PAMRS.msgid, HTTP_TYPE.type23);
        Param param2 = new Param(HTTP_PAMRS.userid, userid);
        Param param3 = new Param("starttime", starttime + "");
        Param param4 = new Param("endtime", endtime + "");
        Param param5 = new Param("typeid", typeid + "");
        Param param6 = new Param("subfactoryid", subfactoryid + "");
        // Param[] params;
        // if (params == null)
        // {
        // params = new Param[6];
        // }
        //
        try
        {
            String result = OkHttpClientManager.postAsString(Appcation.BASE_URL, param1, param2, param3, param4, param5, param6);
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        
        // HttpUtil.post(reParams, new TextHttpResponseHandler() {
        // @Override
        // public void onSuccess(int statusCode, Header[] headers,
        // String responseBody) {
        // super.onSuccess(statusCode, headers, responseBody);
        // Gson gson = new Gson();
        // FormBean formBean=gson.fromJson(responseBody, FormBean.class);
        // onHttpListener.onSucess(formBean);
        // }
        //
        // @Override
        // public void onFailure(String responseBody, Throwable error) {
        // super.onFailure(responseBody, error);
        // onHttpListener.onFail();
        // }
        // });
    }
    
}
