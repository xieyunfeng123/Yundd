package com.vomont.yundudao.upload;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VideoHelpter extends SQLiteOpenHelper
{
    private String tb_name = "tb_yundd";
    
    private static String Tbname = "yundd.db";
    
    private Context context;
    
    // //loadstate 上传的状态 0暂停 1正在上传 2上传完成
    public VideoHelpter(Context context)
    {
        super(context, Tbname, null, 1);
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS "
            + tb_name
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,img VARCHAR,name  VARCHAR,subname  VARCHAR,subfatoryid INTEGER,creattime LONG,desp VARCHAR,looker VARCHAR,lookername VARCHAR,isPack INTEGER,loadstate INTEGER,videoid  INTEGER,videoPath VARCHAR )");
        db.close();
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 之前建国数据库了 这个方法不会走的 一个Android项目 最好只建立一个继承自SQLiteOpenHelper的管理类
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//            + tb_name
//            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,img VARCHAR,name  VARCHAR,subname  VARCHAR,subfatoryid INTEGER,creattime LONG,desp VARCHAR,looker VARCHAR,lookername VARCHAR,isPack INTEGER,loadstate INTEGER,videoid  INTEGER)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }
    
    public void addData(VideoBean videoBean)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO " + tb_name + " VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {videoBean.getImg(), videoBean.getName(), videoBean.getSubname(), videoBean.getSubfatoryid(),
            videoBean.getCreattime(), videoBean.getDesp(), videoBean.getLooker(), videoBean.getLookername(), videoBean.getIsPack(), 0, videoBean.getVideoid(),videoBean.getVideoPath()});
        db.close();
    }
    
    public VideoBean selectByName(String name)
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns = {"img", "name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "isPack", "loadstate", "videoid","videoPath"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  name=? ";
        String[] selectionArgs = {name};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(1));
            videoBean.setSubname(cursor.getString(2));
            videoBean.setSubfatoryid(cursor.getInt(3));
            videoBean.setCreattime(cursor.getLong(4));
            videoBean.setDesp(cursor.getString(5));
            videoBean.setLooker(cursor.getString(6));
            videoBean.setLookername(cursor.getString(7));
            videoBean.setIsPack(cursor.getInt(8));
            videoBean.setLoadstate(cursor.getInt(9));
            videoBean.setVideoid(cursor.getInt(10));
            videoBean.setVideoPath(cursor.getString(11));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist.get(0);
        }
        return null;
    }
    
    public List<VideoBean> selectAll()
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns = {"img", "name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "isPack", "loadstate","videoPath","videoid"};
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        cursor = db.query(tb_name, columns, null, null, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(1));
            videoBean.setSubname(cursor.getString(2));
            videoBean.setSubfatoryid(cursor.getInt(3));
            videoBean.setCreattime(cursor.getLong(4));
            videoBean.setDesp(cursor.getString(5));
            videoBean.setLooker(cursor.getString(6));
            videoBean.setLookername(cursor.getString(7));
            videoBean.setIsPack(cursor.getInt(8));
            videoBean.setLoadstate(cursor.getInt(9));
            videoBean.setVideoPath(cursor.getString(10));
            videoBean.setVideoid(cursor.getInt(11));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist;
        }
        return null;
    }
    
    
    
    public List<VideoBean> selectNoPack()
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns = {"img", "name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "isPack", "loadstate","videoPath","videoid"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  isPack=? ";
        String[] selectionArgs = {0+""};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(1));
            videoBean.setSubname(cursor.getString(2));
            videoBean.setSubfatoryid(cursor.getInt(3));
            videoBean.setCreattime(cursor.getLong(4));
            videoBean.setDesp(cursor.getString(5));
            videoBean.setLooker(cursor.getString(6));
            videoBean.setLookername(cursor.getString(7));
            videoBean.setIsPack(cursor.getInt(8));
            videoBean.setLoadstate(cursor.getInt(9));
            videoBean.setVideoPath(cursor.getString(10));
            videoBean.setVideoid(cursor.getInt(11));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist;
        }
        return null;
    }
    
    
    public List<VideoBean> selectAllPack()
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns = {"img", "name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "isPack", "loadstate","videoPath","videoid"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  isPack=? ";
        String[] selectionArgs = {1+""};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(1));
            videoBean.setSubname(cursor.getString(2));
            videoBean.setSubfatoryid(cursor.getInt(3));
            videoBean.setCreattime(cursor.getLong(4));
            videoBean.setDesp(cursor.getString(5));
            videoBean.setLooker(cursor.getString(6));
            videoBean.setLookername(cursor.getString(7));
            videoBean.setIsPack(cursor.getInt(8));
            videoBean.setLoadstate(cursor.getInt(9));
            videoBean.setVideoPath(cursor.getString(10));
            videoBean.setVideoid(cursor.getInt(11));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist;
        }
        return null;
    }
    
    public void updatePack(String name)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("isPack", 1);
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
        
    }
    
    public void updateBean(String name, VideoBean bean)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("subname", bean.getSubname());
        values.put("subfatoryid", bean.getSubfatoryid());
        values.put("desp", bean.getDesp());
        values.put("looker", bean.getLooker());
        values.put("lookername", bean.getLookername());
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
    }
    
    public void updateLoadState(String name, int state, int videoid)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("loadstate", state);
        values.put("videoid", videoid);
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
    }
    
    public int getPathLoading(String name)
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns = {"loadstate"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "name=? ";
        String[] selectionArgs = {name};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setLoadstate(cursor.getInt(0));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist.get(0).getLoadstate();
        }
        return 0;
    }
    
    
    
    
    public void updataLoad()
    {
        List<VideoBean> mlist = selectAll();
        if (mlist != null && mlist.size() != 0)
            for (VideoBean bean : mlist)
            {
                SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("loadstate", 0);
                db.update(tb_name, values, "name= ? and isPack=?", new String[] {bean.getName(), "0"});
                db.close();
            }
    }
    
    public void deleteVideo(String name)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        String[] selectionArgs = {name};
        String selection = "  name=? ";
        db.delete(tb_name, selection, selectionArgs);
        db.close();
    }
    
}
