package com.vomont.yundudao.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.ui.home.fragment.FactoryFragment;
import com.vomont.yundudao.ui.home.fragment.HomeFragment;
import com.vomont.yundudao.ui.home.fragment.HomeFragment.GetSysMessage;
import com.vomont.yundudao.ui.home.fragment.MangerFragment;
import com.vomont.yundudao.ui.home.fragment.MeFragment;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.upload.VideoUpService;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.Playutil;
import com.vomont.yundudao.utils.ShareUtil;
import com.wmclient.clientsdk.WMClientSdk;

public class HomeActivity extends FragmentActivity implements OnClickListener
{
    private FragmentManager fragmentManager;
    
    private HomeFragment homeFragment;
    
    private FactoryFragment factoryFragment;
    
    private MangerFragment mangerFragment;
    
    private MeFragment meFragment;
    
    private FragmentTransaction fragmentTransaction;
    
    private RadioButton radio_msg, radio_factory, radio_manager, radio_me;
    
    private Playutil playutil;
    
    private ShareUtil shareUtil;
    
    private VideoHelpter videoHelpter;
    
    private long exitTime = 0;
    
    private boolean needLogin = true;
    
    private int result = -1;
    
    public static String name = "";
    
    @Override
    protected void onCreate( Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.activity_home);
        name = new ShareUtil(this).getShare().getNum();
        CashActivty.finishActivity();
        radio_msg = (RadioButton)findViewById(R.id.radio_msg);
        radio_factory = (RadioButton)findViewById(R.id.radio_factory);
        radio_manager = (RadioButton)findViewById(R.id.radio_manager);
        radio_me = (RadioButton)findViewById(R.id.radio_me);
        playutil = new Playutil(this);
        shareUtil = new ShareUtil(this);
        initFragment();
        homeFragment.initHandler(new GetSysMessage()
        {
            @Override
            public void onEnd()
            {
                
            }
        });
        
        new Thread(new Runnable()
        {
            public void run()
            {
                while (needLogin)
                {
                    if (result != 0)
                    {
                        result = playutil.authenticate(shareUtil.getShare().getVeyeuserid(), shareUtil.getShare().getVeyekey(),shareUtil.getShare().getVeyesvrip(),shareUtil.getShare().getVeyesvrport());
                    }
                    else if (result != -1)
                    {
                        needLogin = false;
                    }
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
        
        // 绑定服务在主界面 后面的界面调用 解绑service后 不会销毁该服务
        Intent intent = new Intent(this, VideoUpService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        
        videoHelpter = new VideoHelpter(this);
        // 防止由于崩溃等原因导致在上传过程中 断掉上传 而出现上传状态的错误的更新
        videoHelpter.updataLoad();
        initListener();
    }
    
    private ServiceConnection conn = new ServiceConnection()
    {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            // 返回一个MsgService对象
            // myService = ((MyService.MsgBinder) service).getService();
        }
        
        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name)
        {
            // 、 myService = null;
        }
    };
    
    private void initFragment()
    {
        // 初始化 默认第一个页面
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        homeFragment = new HomeFragment();
        fragmentTransaction.add(R.id.activity_home_frame, homeFragment);
        if (factoryFragment != null)
        {
            fragmentTransaction.hide(factoryFragment);
        }
        
        if (mangerFragment != null)
        {
            fragmentTransaction.hide(mangerFragment);
        }
        
        if (meFragment != null)
        {
            fragmentTransaction.hide(meFragment);
        }
        fragmentTransaction.commit();
        WMClientSdk.getInstance().init(63);
    }
    
    private void initListener()
    {
        radio_msg.setOnClickListener(this);
        radio_factory.setOnClickListener(this);
        radio_manager.setOnClickListener(this);
        radio_me.setOnClickListener(this);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit();
        }
        return true;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.radio_msg:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (homeFragment == null)
                {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, homeFragment);
                }
                else
                {
                    fragmentTransaction.show(homeFragment);
                }
                
                if (factoryFragment != null)
                {
                    fragmentTransaction.hide(factoryFragment);
                }
                
                if (mangerFragment != null)
                {
                    fragmentTransaction.hide(mangerFragment);
                }
                
                if (meFragment != null)
                {
                    fragmentTransaction.hide(meFragment);
                }
                fragmentTransaction.commit();
                break;
            case R.id.radio_factory:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (factoryFragment == null)
                {
                    factoryFragment = new FactoryFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, factoryFragment);
                }
                else
                {
                    fragmentTransaction.show(factoryFragment);
                }
                
                if (homeFragment != null)
                {
                    fragmentTransaction.hide(homeFragment);
                }
                
                if (mangerFragment != null)
                {
                    fragmentTransaction.hide(mangerFragment);
                }
                
                if (meFragment != null)
                {
                    fragmentTransaction.hide(meFragment);
                }
                fragmentTransaction.commit();
                
                break;
            case R.id.radio_manager:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (mangerFragment == null)
                {
                    mangerFragment = new MangerFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, mangerFragment);
                }
                else
                {
                    fragmentTransaction.show(mangerFragment);
                }
                
                if (homeFragment != null)
                {
                    fragmentTransaction.hide(homeFragment);
                }
                
                if (factoryFragment != null)
                {
                    fragmentTransaction.hide(factoryFragment);
                }
                
                if (meFragment != null)
                {
                    fragmentTransaction.hide(meFragment);
                }
                fragmentTransaction.commit();
                break;
            case R.id.radio_me:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (meFragment == null)
                {
                    meFragment = new MeFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, meFragment);
                }
                else
                {
                    fragmentTransaction.show(meFragment);
                }
                
                if (homeFragment != null)
                {
                    fragmentTransaction.hide(homeFragment);
                }
                
                if (factoryFragment != null)
                {
                    fragmentTransaction.hide(factoryFragment);
                }
                
                if (mangerFragment != null)
                {
                    fragmentTransaction.hide(mangerFragment);
                }
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }
    
    public void exit()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else
        {
            ACache aCache = ACache.get(this);
            aCache.put("factoryBean", "");
            aCache.put("problemDetailInfo", "");
            finish();
            System.exit(0);
        }
    }
    
    // 改界面销毁 就是app销毁 结束该service的绑定
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(conn);
    }
}
