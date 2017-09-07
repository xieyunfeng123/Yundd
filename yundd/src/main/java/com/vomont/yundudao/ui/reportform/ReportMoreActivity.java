package com.vomont.yundudao.ui.reportform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.SubFormBean;
import com.vomont.yundudao.bean.SubFormCall;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.mvpview.reportform.IReportMoreView;
import com.vomont.yundudao.presenter.reportform.ReportMorePresenter;
import com.vomont.yundudao.ui.reportform.adapter.MoreListAdapter;
import com.vomont.yundudao.ui.reportform.adapter.TagReportAdapter;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;
import com.vomont.yundudao.view.flow.FlowLayout;
import com.vomont.yundudao.view.flow.TagAdapter;
import com.vomont.yundudao.view.flow.TagFlowLayout;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReportMoreActivity extends Activity implements IReportMoreView
{
    private TagFlowLayout tag_report;
    
    private ListView list_report;
    
    private TagReportAdapter tagReportAdapter;
    
    private MoreListAdapter adapter;
    
    private List<TagInfo> allInfos;
    
    private List<TagInfo> sendTags;
    
    private long startTime, endTime;
    
    private List<DeviceInfo> sendDeviceInfos;
    
    private TextView report_starttime;
    
    private TextView report_endtime;
    
    private ScrollView scrollview;
    
    private long weekLength = 7 * 24 * 60 * 60 * 1000;
    
    private long oneDayLength = 24 * 60 * 60 * 1000;
    
    private ReportMorePresenter presenter;
    
    private ShareUtil shareUtil;
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_more);
        tag_report = (TagFlowLayout)findViewById(R.id.tag_report);
        list_report = (ListView)findViewById(R.id.list_report);
        report_starttime = (TextView)findViewById(R.id.report_starttime);
        report_endtime = (TextView)findViewById(R.id.report_endtime);
        scrollview = (ScrollView)findViewById(R.id.scrollview);
        allInfos = (List<TagInfo>)getIntent().getSerializableExtra("tags");
        sendTags = (List<TagInfo>)getIntent().getSerializableExtra("types");
        startTime = getIntent().getLongExtra("starttime", 0);
        endTime = getIntent().getLongExtra("endtime", 0);
        
        Log.e("insert", (endTime - startTime) + "=======" + weekLength);
        sendDeviceInfos = (List<DeviceInfo>)getIntent().getSerializableExtra("devs");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        report_starttime.setText(format.format(new Date(startTime)));
        report_endtime.setText(format.format(new Date(endTime)));
        final LayoutInflater mInflater = LayoutInflater.from(this);
        tag_report.setAdapter(new TagAdapter<TagInfo>(allInfos)
        {
            @Override
            public View getView(FlowLayout parent, int position, TagInfo t)
            {
                TextView tv = (TextView)mInflater.inflate(R.layout.item_report_more_gridview, parent, false);
                tv.setText(t.getTypename());
                tv.setTextColor(getResources().getColor(R.color.text_color));
                for (int i = 0; i < sendTags.size(); i++)
                {
                    if (t.getTypeid() == sendTags.get(i).getTypeid())
                    {
                        tv.setBackgroundResource(R.drawable.report_select_no);
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }
                }
                return tv;
            }
        });
        
        adapter = new MoreListAdapter(this);
        list_report.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(list_report);
        scrollview.smoothScrollTo(0, 0);
        presenter = new ReportMorePresenter(this);
        shareUtil = new ShareUtil(this);
        // if ((endTime - startTime) > weeklength)
        // {
        //
        // }
        // else
        // {
        //
        // }
        new Thread(runnable).start();
    }
    
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            getData();
        }
    };
    
    public void getData()
    {
        if ((endTime - startTime) > weekLength)
        {
            // 大于一周
            
        }
        else
        {
            // 一周以内
            int size = (int)((endTime - startTime) / oneDayLength + (endTime - startTime) % oneDayLength == 0 ? 0 : 1);
            for (int i = 0; i < size; i++)
            {
                long start = startTime + i * oneDayLength;
                long end = (i == (size - 1)) ? endTime : (start + oneDayLength);
                String result = presenter.getFormMore(shareUtil.getShare().getUserid() + "", start, end, "", "");
                if (result != null)
                {
                    Gson gson = new Gson();
                    SubFormCall call = gson.fromJson(result, SubFormCall.class);
                    if (call.getResult() == 0)
                    {
                        List<SubFormBean> subFormBeans = call.getStatistic();
                    }
                }
            }
        }
    }
    
    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    
    @Override
    public void getSucess(FormBean formBean)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void getError()
    {
        // TODO Auto-generated method stub
        
    }
    
}
