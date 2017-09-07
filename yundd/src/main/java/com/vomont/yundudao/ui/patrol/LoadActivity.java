package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.patrol.adapter.LoadingAdapter;
import com.vomont.yundudao.ui.patrol.adapter.LoadingAdapter.CheckListener;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoFragmentHelper;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.upload.VideoUpService;
import com.vomont.yundudao.upload.VideoUpService.OnLoadListener;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.view.ios.AlertDialog;

@SuppressLint("HandlerLeak")
public class LoadActivity extends Activity implements OnClickListener
{
    
    private ImageView load_go_back;
    
    private ImageView load_delete;
    
    private ListView video_load_list;
    
    private VideoFragmentHelper helper;
    
    private VideoHelpter videoHelpter;
    
    private List<VideoBean> mlist;
    
    private List<VideoBean> load_mlist;
    
    private VideoUpService videoUpService;
    
    private LoadingAdapter adapter;
    
    private boolean isDelete;
    
    private List<DVPostion> delete_position;
    
    private int removeI;
    
    // private boolean isPull;
    
    private ACache aCache;
    
    private VideoBean no_bean;
    
    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 10:
                    if (videoUpService != null)
                    {
                        if (load_mlist != null && load_mlist.size() != 0)
                            for (int i = 0; i < load_mlist.size(); i++)
                            {
                                if (no_bean != null)
                                {
                                    if (no_bean.getName() != load_mlist.get(i).getName())
                                    {
                                        if (videoHelpter.getPathLoading(load_mlist.get(i).getName()) == 1)
                                        {
                                            load_mlist.get(i).setLoadstate(1);
                                        }
                                        else if (videoHelpter.getPathLoading(load_mlist.get(i).getName()) == 0)
                                        {
                                            load_mlist.get(i).setLoadstate(0);
                                        }
                                        else if (videoHelpter.getPathLoading(load_mlist.get(i).getName()) == 2)
                                        {
                                            load_mlist.remove(i);
                                        }
                                    }
                                }
                                else
                                {
                                    if (videoHelpter.getPathLoading(load_mlist.get(i).getName()) == 1)
                                    {
                                        load_mlist.get(i).setLoadstate(1);
                                    }
                                    else if (videoHelpter.getPathLoading(load_mlist.get(i).getName()) == 0)
                                    {
                                        load_mlist.get(i).setLoadstate(0);
                                    }
                                    else if (videoHelpter.getPathLoading(load_mlist.get(i).getName()) == 2)
                                    {
                                        load_mlist.remove(i);
                                    }
                                }
                            }
                        getNowPos();
                    }
                    break;
                default:
                    break;
            }
        };
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        initView();
        initListener();
        initData();
        
    }
    
    private void getNowPos()
    {
        if (load_mlist != null && load_mlist.size() != 0)
        {
            for (VideoBean bean : load_mlist)
            {
                int pos = 0;
                int maxPos = helper.selectByPath(this, VideoManager.path + "/" + bean.getName() + ".mp4");
                int noPos = helper.getNoUpdataContext(this, VideoManager.path + "/" + bean.getName() + ".mp4");
                if (maxPos == (maxPos - noPos))
                {
                    pos = 99;
                    if (!helper.getNoPackContext(this, VideoManager.path + "/" + bean.getName() + ".mp4"))
                    {
                        pos = 100;
                    }
                }
                else
                {
                    pos = (maxPos - noPos) * 99 / maxPos;
                }
                bean.setPos(pos);
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    private boolean contains(DVPostion dvPostion)
    {
        boolean result = false;
        if (delete_position != null && delete_position.size() != 0)
        {
            for (int i = 0; i < delete_position.size(); i++)
            {
                if (delete_position.get(i).position == dvPostion.position)
                {
                    result = true;
                    removeI = i;
                }
            }
        }
        return result;
    }
    
    private void initData()
    {
        aCache = ACache.get(this);
        Intent intent = new Intent(this, VideoUpService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        helper = new VideoFragmentHelper(this);
        videoHelpter = new VideoHelpter(this);
        delete_position = new ArrayList<DVPostion>();
        mlist = new ArrayList<VideoBean>();
        load_mlist = new ArrayList<VideoBean>();
        if (videoHelpter.selectAll() != null)
        {
            mlist.addAll(videoHelpter.selectAll());
        }
        // 查询未上传的
        if (mlist != null && mlist.size() != 0)
        {
            for (int i = 0; i < mlist.size(); i++)
            {
                if (mlist.get(i).getIsPack() == 1)
                {
                    mlist.remove(i);
                }
                else
                {
                    mlist.get(i).setLoadstate(videoHelpter.getPathLoading(mlist.get(i).getName()));
                }
            }
        }
        if (mlist != null && mlist.size() != 0)
        {
            for (int i = 0; i < mlist.size(); i++)
            {
                if (helper.getNoPackContext(this, mlist.get(i).getVideoPath() + "/" + mlist.get(i).getName() + ".mp4"))
                {
                    load_mlist.add(mlist.get(i));
                }
            }
        }
        pullData();
        
        adapter = new LoadingAdapter(this, load_mlist);
        adapter.setCheckBoxListener(new CheckListener()
        {
            @Override
            public void deletePosition(int i)
            {
                DVPostion dvPostion = new DVPostion();
                dvPostion.position = i;
                if (contains(dvPostion))
                {
                    delete_position.remove(removeI);
                }
            }
            
            @Override
            public void addPosition(int i)
            {
                DVPostion dvPostion = new DVPostion();
                dvPostion.position = i;
                if (!contains(dvPostion))
                {
                    delete_position.add(dvPostion);
                }
            }
        });
        video_load_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
        video_load_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if (!isDelete)
                {
                    if (load_mlist.get(position).getLoadstate() == 0)
                    {
                        aCache.put(load_mlist.get(position).getDesp(), "0");
                        load_mlist.get(position).setLoadstate(1);
                        videoUpService.upDataVideo(load_mlist.get(position));
                        no_bean = null;
                    }
                    else
                    {
                        aCache.put(load_mlist.get(position).getDesp(), "1");
                        load_mlist.get(position).setLoadstate(0);
                        no_bean = load_mlist.get(position);
                    }
                    adapter.notifyDataSetChanged();
                }
                
            }
        });
    }
    
    private void pullData()
    {
        handler.sendEmptyMessage(10);
    }
    
    private void initListener()
    {
        load_go_back.setOnClickListener(this);
        load_delete.setOnClickListener(this);
    }
    
    private void initView()
    {
        load_go_back = (ImageView)findViewById(R.id.load_go_back);
        load_delete = (ImageView)findViewById(R.id.load_delete);
        video_load_list = (ListView)findViewById(R.id.video_load_list);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.load_go_back:
                finish();
                break;
            case R.id.load_delete:
                // videoUpService.isStop_Load = true;
                for (int i = 0; i < load_mlist.size(); i++)
                {
                    load_mlist.get(i).setLoadstate(0);
                    aCache.put(load_mlist.get(i).getDesp(), "1");
                }
                // isPull = false;
                if (!isDelete)
                {
                    load_delete.setImageResource(R.drawable.spotsupervision_delete);
                    if (load_mlist != null)
                    {
                        for (VideoBean bean : load_mlist)
                        {
                            bean.setDelete(1);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if (delete_position != null && delete_position.size() != 0)
                    {
                        new AlertDialog(LoadActivity.this).builder().setMsg("确定删除视频").setNegativeButton("确定", new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                List<VideoBean> deleteList = new ArrayList<VideoBean>();
                                for (int i = 0; i < delete_position.size(); i++)
                                {
                                    deleteList.add(load_mlist.get(delete_position.get(i).position));
                                    File file = new File(load_mlist.get(delete_position.get(i).position).getVideoPath()+ "/" + load_mlist.get(delete_position.get(i).position).getName() + ".mp4");
                                    if (file.exists())
                                    {
                                        file.delete();
                                    }
                                    videoHelpter.deleteVideo(load_mlist.get(delete_position.get(i).position).getName());
                                    helper.deleteName(LoadActivity.this, load_mlist.get(delete_position.get(i).position).getVideoPath() + "/" + load_mlist.get(delete_position.get(i).position).getName() + ".mp4");
                                    // load_mlist.remove(delete_position.get(i).position);
                                    
                                }
                                load_mlist.removeAll(deleteList);
                                
                                if (load_mlist != null && load_mlist.size() != 0)
                                {
                                    for (VideoBean bean : load_mlist)
                                    {
                                        bean.setDelete(0);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                delete_position.removeAll(delete_position);
                                load_delete.setImageResource(R.drawable.spotsupervision_picmanag);
                                pullData();
                            }
                        }).setPositiveButton("取消", new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (load_mlist != null && load_mlist.size() != 0)
                                {
                                    for (VideoBean bean : load_mlist)
                                    {
                                        bean.setDelete(0);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                isDelete = false;
                                load_delete.setImageResource(R.drawable.spotsupervision_picmanag);
                                pullData();
                            }
                        }).show();
                    }
                    else
                    {
                        if (load_mlist != null && load_mlist.size() != 0)
                        {
                            for (VideoBean bean : load_mlist)
                            {
                                bean.setDelete(0);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        load_delete.setImageResource(R.drawable.spotsupervision_picmanag);
                    }
                }
                isDelete = !isDelete;
                
                break;
            default:
                break;
        }
    }
    
    ServiceConnection conn = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            videoUpService = ((VideoUpService.VideoBinder)service).getService();
            if (videoUpService != null)
            {
                videoUpService.setOnLoadListener(new OnLoadListener()
                {
                    
                    @Override
                    public void needRerpull()
                    {
                        handler.sendEmptyMessage(10);
                    }
                    
                    @Override
                    public void Sucess()
                    {
                        
                    }
                    
                    @Override
                    public void Load()
                    {
                        
                    }
                });
                
            }
        }
    };
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(conn);
        // isPull = false;
    }
    
    class DVPostion
    {
        int position;
    }
}
