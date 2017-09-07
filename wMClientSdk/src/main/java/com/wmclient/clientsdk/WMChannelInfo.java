package com.wmclient.clientsdk;

import java.io.Serializable;

public class WMChannelInfo implements Serializable
{
    private int channelId;
    private String channelName;
    private int channelPrivilege;
   
	/**
	*获取通道Id编号
	*@return 通道Id编号
    */    
    public int getChannelId() 
    {
        return channelId;
    }

	/**
	*设置通道Id编号
	*@param channelId 通道Id编号
    */     
    public void setChannelId(int channelId) 
    {
        this.channelId = channelId;
    }
 
	/**
	*获取通道名称
	*@return 通道名称
    */     
    public String getChannelName() 
    {
        return channelName;
    }

	/**
	*设置通道名称
	*@param channelName 通道名称
    */      
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }    

	/**
	*获取通道访问权限
	*@return 通道访问权限
    */      
    public int getChannelPrivilege() 
    {
        return channelPrivilege;
    }

	/**
	*设置通道访问权限
	*@param channelPrivilege 通道访问权限
    */    
    public void setChannelPrivilege(int channelPrivilege) 
    {
        this.channelPrivilege = channelPrivilege;
    }
}
