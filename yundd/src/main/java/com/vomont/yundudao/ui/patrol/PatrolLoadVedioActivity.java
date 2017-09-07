package com.vomont.yundudao.ui.patrol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.factory.adapter.MyFragmentPagerAdapter;
import com.vomont.yundudao.ui.patrol.fragment.AllLoadFragment;
import com.vomont.yundudao.ui.patrol.fragment.NoLoadFragment;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoFragmentHelper;
import com.vomont.yundudao.upload.VideoFragmentInfo;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.upload.VideoUpService;
import com.vomont.yundudao.upload.VideoUpService.OnLoadListener;
import com.vomont.yundudao.utils.NetWorkSpeedUtils;

@SuppressLint("HandlerLeak")
public class PatrolLoadVedioActivity extends FragmentActivity implements OnClickListener
{
    
    private ImageView load_vedio_back;
    
    private TextView tv_unload_vedio;
    
    private TextView tv_all_vedio;
    
    private TextView vedio_cursor;
    
    private ViewPager load_vedio_viewpager;
    
    private Context context;
    
    private int currIndex;// 当前页卡编号
    
    private ArrayList<Fragment> fragmentList;
    
    private VideoHelpter helpter;
    
    private List<VideoBean> unUpmlist;
    
    private List<VideoBean> upmlist;
    
    private ImageView action_delete;
    
    private LinearLayout to_loadingactivity;
    
    private VideoUpService videoUpService;
    
    private VideoFragmentHelper videoFragmentHelper;
    
    private List<VideoBean> mlist;
    
    private List<VideoBean> load_mlist;
    
    private TextView updata_ks;
    
    private TextView now_updata_size;
    
    private TextView updata_size;
    
    private boolean isPlaying;
    
    private NoLoadFragment noLoadFragment;
    
    private AllLoadFragment allLoadFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_requeset_vedio);
        Intent intent = new Intent(this, VideoUpService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        initView();
        InitTextBar();
        InitViewPager();
        initListener();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        isPlaying = true;
        initData();
        if (videoUpService != null)
        {
            videoUpService.setOnLoadListener(new OnLoadListener()
            {
                @Override
                public void Sucess()
                {
                    handler.sendEmptyMessage(200);
                }
                
                @Override
                public void Load()
                {
                }
                
                @Override
                public void needRerpull()
                {
                }
            });
        }
    }
    
    private void initData()
    {
        showView();
        showIsUpData();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        isPlaying = false;
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(conn);
        videoUpService = null;
    }
    
    private void showIsUpData()
    {
        videoFragmentHelper = new VideoFragmentHelper(this);
        new Thread(new Runnable()
        {
            public void run()
            {
                while (isPlaying)
                {
                    handler.sendEmptyMessage(10);
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 10:
                    if (helpter != null && helpter.selectAll() != null)
                    {
                        mlist = new ArrayList<VideoBean>();
                        load_mlist = new ArrayList<VideoBean>();
                        mlist.addAll(helpter.selectAll());
                        // 查询未上传的
                        if (mlist != null && mlist.size() != 0)
                        {
                            for (int i = 0; i < mlist.size(); i++)
                            {
                                if (mlist.get(i).getIsPack() == 1)
                                {
                                    mlist.remove(i);
                                }
                            }
                        }
                        if (mlist != null && mlist.size() != 0)
                        {
                            for (int i = 0; i < mlist.size(); i++)
                            {
                                if (videoFragmentHelper.getNoPackContext(PatrolLoadVedioActivity.this, mlist.get(i).getVideoPath() + "/" + mlist.get(i).getName() + ".mp4"))
                                {
                                    load_mlist.add(mlist.get(i));
                                }
                            }
                        }
                        if (load_mlist != null && load_mlist.size() != 0)
                        {
                            if (to_loadingactivity != null)
                            {
                                to_loadingactivity.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            if (to_loadingactivity != null)
                            {
                                to_loadingactivity.setVisibility(View.GONE);
                            }
                        }
                        if (mlist != null && mlist.size() != 0)
                        {
                            List<VideoBean> loadlist = new ArrayList<VideoBean>();
                            int allSize = 0;
                            int noSize = 0;
                            int nopage = 0;
                            for (int i = 0; i < mlist.size(); i++)
                            {
                                if (videoFragmentHelper.getNoPackContext(PatrolLoadVedioActivity.this, mlist.get(i).getVideoPath() + "/" + mlist.get(i).getName() + ".mp4"))
                                {
                                    loadlist.add(mlist.get(i));
                                }
                            }
                            for (int i = 0; i < loadlist.size(); i++)
                            {
                                List<VideoFragmentInfo> fraglist =
                                    videoFragmentHelper.getByPath(PatrolLoadVedioActivity.this, loadlist.get(i).getVideoPath() + "/" + loadlist.get(i).getName() + ".mp4");
                                int noUpdataSize = videoFragmentHelper.getNoUpdataContext(PatrolLoadVedioActivity.this, loadlist.get(i).getVideoPath() + "/" + loadlist.get(i).getName() + ".mp4");
                                boolean is = videoFragmentHelper.getNoPackContext(PatrolLoadVedioActivity.this, loadlist.get(i).getVideoPath() + "/" + loadlist.get(i).getName() + ".mp4");
                                if (fraglist != null && noUpdataSize != 0 && is)
                                {
                                    allSize = allSize + fraglist.size();
                                }
                                else
                                {
                                    nopage = nopage + 1;
                                }
                                noSize = noSize + noUpdataSize;
                            }
                            
                            if (allSize != 0)
                            {
                                int all = allSize - noSize;
                                if (all <= 0)
                                {
                                    all = 0;
                                }
                                updata_size.setText((all) * 99 / allSize + nopage / mlist.size() / allSize + "%");
                            }
                            now_updata_size.setText("(" + loadlist.size() + ")");
                        }
                    }
                    else
                    {
                        if (to_loadingactivity != null)
                        {
                            to_loadingactivity.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 100:
                    updata_ks.setText(msg.obj.toString());
                    break;
                case 200:
                    showView();
                    noLoadFragment.showView();
                    allLoadFragment.showView();
                    break;
                default:
                    break;
            }
        };
    };
    
    /**
     * 获取网络的时时网速，使用方法是：
     * 每隔一段时间读取一次总流量，然后用本次和前一次的差除以间隔时间来获取平均速度，再换算为 K/s M/s
     * 等单位，显示即可。
     * 
     * @return 实时的网速（单位byte）
     */
    public static int getNetSpeedBytes()
    {
        String line;
        String[] segs;
        int received = 0;
        int i;
        int tmp = 0;
        boolean isNum;
        try
        {
            FileReader fr = new FileReader("/proc/net/dev");
            BufferedReader in = new BufferedReader(fr, 500);
            while ((line = in.readLine()) != null)
            {
                line = line.trim();
                if (line.startsWith("rmnet") || line.startsWith("eth") || line.startsWith("wlan"))
                {
                    segs = line.split(":")[1].split(" ");
                    for (i = 0; i < segs.length; i++)
                    {
                        isNum = true;
                        try
                        {
                            tmp = Integer.parseInt(segs[i]);
                        }
                        catch (Exception e)
                        {
                            isNum = false;
                        }
                        if (isNum == true)
                        {
                            received = received + tmp;
                            break;
                        }
                    }
                }
            }
            in.close();
        }
        catch (IOException e)
        {
            return -1;
        }
        return received;
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
                    public void Sucess()
                    {
                        handler.sendEmptyMessage(200);
                    }
                    
                    @Override
                    public void Load()
                    {
                    }
                    
                    @Override
                    public void needRerpull()
                    {
                    }
                });
            }
        }
    };
    
    public void showView()
    {
        new NetWorkSpeedUtils(this, handler).startShowNetSpeed();
        helpter = new VideoHelpter(this);
        helpter.getWritableDatabase();
        unUpmlist = new ArrayList<VideoBean>();
        upmlist = new ArrayList<VideoBean>();
        List<VideoBean> allMlist = new ArrayList<VideoBean>();
        if (helpter.selectAll() != null)
            allMlist.addAll(helpter.selectAll());
        
        for (int i = 0; i < allMlist.size(); i++)
        {
            if (allMlist.get(i).getIsPack() == 0)
            {
                unUpmlist.add(allMlist.get(i));
            }
            else if (allMlist.get(i).getIsPack() == 1)
            {
                upmlist.add(allMlist.get(i));
            }
        }
        int unUP = 0;
        int up = 0;
        if (unUpmlist != null && unUpmlist.size() != 0)
        {
            unUP = unUpmlist.size();
        }
        
        if (upmlist != null && upmlist.size() != 0)
        {
            up = upmlist.size();
        }
        tv_unload_vedio.setText("未上传(" + unUP + ")");
        tv_all_vedio.setText("全部(" + (unUP + up) + ")");
    }
    
    private void initListener()
    {
        load_vedio_back.setOnClickListener(this);
        
        tv_unload_vedio.setOnClickListener(this);
        tv_all_vedio.setOnClickListener(this);
        action_delete.setOnClickListener(this);
        to_loadingactivity.setOnClickListener(this);
    }
    
    private void initView()
    {
        updata_size = (TextView)findViewById(R.id.updata_size);
        now_updata_size = (TextView)findViewById(R.id.now_updata_size);
        updata_ks = (TextView)findViewById(R.id.updata_ks);
        load_vedio_back = (ImageView)findViewById(R.id.load_vedio_back);
        tv_unload_vedio = (TextView)findViewById(R.id.tv_unload_vedio);
        tv_all_vedio = (TextView)findViewById(R.id.tv_all_vedio);
        vedio_cursor = (TextView)findViewById(R.id.vedio_cursor);
        load_vedio_viewpager = (ViewPager)findViewById(R.id.load_vedio_viewpager);
        
        action_delete = (ImageView)findViewById(R.id.action_delete);
        to_loadingactivity = (LinearLayout)findViewById(R.id.to_loadingactivity);
        context = this;
        to_loadingactivity.setVisibility(View.GONE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.load_vedio_back:
                finish();
                break;
            case R.id.tv_unload_vedio:
                load_vedio_viewpager.setCurrentItem(0);
                break;
            case R.id.tv_all_vedio:
                load_vedio_viewpager.setCurrentItem(1);
                break;
            case R.id.action_delete:
                Intent deIntent = new Intent(context, DeleteVideoActivity.class);
                startActivity(deIntent);
                break;
            case R.id.to_loadingactivity:
                Intent loadIntent = new Intent(context, LoadActivity.class);
                startActivity(loadIntent);
                break;
            default:
                break;
        }
    }
    
    /*
     * 初始化ViewPager
     */
    @SuppressWarnings("deprecation")
    public void InitViewPager()
    {
        fragmentList = new ArrayList<Fragment>();
        
        noLoadFragment = new NoLoadFragment();
        
        allLoadFragment = new AllLoadFragment();
        fragmentList.add(noLoadFragment);
        fragmentList.add(allLoadFragment);
        // 给ViewPager设置适配器
        load_vedio_viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        load_vedio_viewpager.setCurrentItem(0);// 设置当前显示标签页为第一页
        load_vedio_viewpager.setOnPageChangeListener(new LoadOnPageChangeListener());// 页面变化时的监听器
    }
    
    /*
     * 初始化图片的位移像素
     */
    public void InitTextBar()
    {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        // 得到显示屏宽度
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // 1/2屏幕宽度
        int tabLineLength = metrics.widthPixels / 2;
        LayoutParams lp = (LayoutParams)vedio_cursor.getLayoutParams();
        lp.width = tabLineLength;
        vedio_cursor.setLayoutParams(lp);
    }
    
    public class LoadOnPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
            // 取得该控件的实例
            LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams)vedio_cursor.getLayoutParams();
            
            if (currIndex == arg0)
            {
                
                ll.leftMargin = (int)(currIndex * vedio_cursor.getWidth() + arg1 * vedio_cursor.getWidth());
            }
            else if (currIndex > arg0)
            {
                ll.leftMargin = (int)(currIndex * vedio_cursor.getWidth() - (1 - arg1) * vedio_cursor.getWidth());
            }
            vedio_cursor.setLayoutParams(ll);
        }
        
        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }
        
        @Override
        public void onPageSelected(int arg0)
        {
            currIndex = arg0;
            if (currIndex == 0)
            {
                tv_unload_vedio.setTextColor(getResources().getColor(R.color.main_color));
                tv_all_vedio.setTextColor(getResources().getColor(R.color.text_color));
            }
            else
            {
                tv_unload_vedio.setTextColor(getResources().getColor(R.color.text_color));
                tv_all_vedio.setTextColor(getResources().getColor(R.color.main_color));
            }
        }
    }
}
