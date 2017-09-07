package com.vomont.yundudao.ui.reportform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xclcharts.chart.PieData;

import com.googlecode.mp4parser.boxes.MLPSpecificBox;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.bean.FormFactory;
import com.vomont.yundudao.bean.FormType;
import com.vomont.yundudao.bean.UserInfo;
import com.vomont.yundudao.common.ACache_Con;
import com.vomont.yundudao.mvpview.reportform.IReportFormView;
import com.vomont.yundudao.presenter.reportform.ReportFormPresenter;
import com.vomont.yundudao.ui.reportform.adapter.ReportFormAdapter;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ACache.ACacheManager;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.pieChart.PieView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReportFormActivity extends Activity implements OnClickListener, IReportFormView
{
    // gridview_linearlayout
    
    private PieView piechartview;
    
    private TextView week_data;
    
    private TextView mouth_data;
    
    private TextView top_name;
    
    private ImageView go_back;
    
    private LinearLayout most_layout;
    
    private TextView most_type_name;
    
    private List<PieData> chartData;
    
    private ListView list_data;
    
    private ReportFormAdapter adapter;
    
    private TextView more_data;
    
    private ReportFormPresenter presenter;
    
    private long startTime, endTime;
    
    private ACache aCache;
    
    private ShareUtil shareUtil;
    
    private boolean isMouth = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportfrom);
        piechartview = (PieView)findViewById(R.id.piechartview);
        list_data = (ListView)findViewById(R.id.list_data);
        more_data = (TextView)findViewById(R.id.more_data);
        week_data = (TextView)findViewById(R.id.week_data);
        mouth_data = (TextView)findViewById(R.id.mouth_data);
        top_name = (TextView)findViewById(R.id.top_name);
        go_back = (ImageView)findViewById(R.id.go_back);
        most_layout = (LinearLayout)findViewById(R.id.most_layout);
        most_type_name = (TextView)findViewById(R.id.most_type_name);
        presenter = new ReportFormPresenter(this);
        chartData = new ArrayList<PieData>();
        adapter = new ReportFormAdapter(this);
        list_data.setAdapter(adapter);
        more_data.setOnClickListener(this);
        week_data.setOnClickListener(this);
        mouth_data.setOnClickListener(this);
        go_back.setOnClickListener(this);
        top_name.setText("报表");
        piechartview.setVisibility(View.GONE);
        most_layout.setVisibility(View.GONE);
        
        aCache = ACache.get(this);
        shareUtil = new ShareUtil(this);
        getWeek();
        presenter.getForm(shareUtil.getShare().getUser_id() + "", startTime, endTime);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.more_data:
                Intent intent = new Intent(ReportFormActivity.this, FormTypeActivity.class);
                startActivity(intent);
                break;
            case R.id.go_back:
                finish();
                break;
            case R.id.week_data:
                upDataView(week_data, mouth_data);
                if (!isMouth)
                {
                    getWeek();
                    presenter.getForm(shareUtil.getShare().getUser_id() + "", startTime, endTime);
                }
                isMouth = !isMouth;
                break;
            case R.id.mouth_data:
                upDataView(mouth_data, week_data);
                if (isMouth)
                {
                    getMoth();
                    presenter.getForm(shareUtil.getShare().getUser_id() + "", startTime, endTime);
                }
                isMouth = !isMouth;
                break;
            default:
                break;
        }
    }
    
    public void upDataView(TextView textView1, TextView textView2)
    {
        textView1.setBackgroundResource(R.drawable.textview_suface);
        textView1.setTextColor(getResources().getColor(R.color.main_color));
        
        textView2.setBackgroundResource(R.color.white);
        textView2.setTextColor(getResources().getColor(R.color.back_color));
    }
    
    @Override
    public void getSucess(FormBean formBean)
    {
        if (formBean != null && formBean.getResult() == 0)
        {
            // piechartview.setVisibility(View.GONE);
            most_layout.setVisibility(View.VISIBLE);
            List<FormType> types = formBean.getTypestatistic();
            if (types != null && types.size() == 0)
            {
                Collections.sort(types, new Comparator<FormType>()
                {
                    @Override
                    public int compare(FormType o1, FormType o2)
                    {
                        return (o1.getProblemcnt() + "").compareTo(o2.getProblemcnt() + "");
                    }
                });
                int max = 0;
                for (int i = 0; i < types.size(); i++)
                {
                    if (i > 3)
                    {
                        types.remove(i);
                    }
                    else
                    {
                        max = max + types.get(i).getProblemcnt();
                    }
                }
                most_type_name.setText(types.get(0).getTypename());
                for (int i = 0; i < types.size(); i++)
                {
                    int color = Color.rgb(22, 160, 232);
                    if (i == 0)
                    {
                        color = Color.rgb(22, 160, 232);
                    }
                    else if (i == 1)
                    {
                        color = Color.rgb(247, 116, 116);
                    }
                    else if (i == 2)
                    {
                        color = Color.rgb(252, 131, 42);
                    }
                    else if (i == 3)
                    {
                        color = Color.rgb(55, 178, 130);
                    }
                    chartData.add(new PieData(types.get(i).getTypename(), types.get(i).getProblemcnt() * 100 / max + "%", 10f, color));
                }
            }
            List<FormFactory> factories = formBean.getStatusstatistic();
            if (factories != null && factories.size() != 0)
            {
                adapter.setData(factories);
                adapter.notifyDataSetChanged();
                piechartview.setVisibility(View.VISIBLE);
                setListViewHeightBasedOnChildren(list_data);
            }
        }
        else
        {
            piechartview.setVisibility(View.GONE);
            most_layout.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void getError()
    {
        
    }
    
    public void getWeek()
    {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        String start = dateFormater.format(cal.getTime()) + " 00:00:00";
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        String end = dateFormater.format(cal.getTime()) + " 23:59:59";
        try
        {
            startTime = format.parse(start).getTime();
            endTime = format.parse(end).getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    
    public void getMoth()
    {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String start = dateFormater.format(cal.getTime()) + " 00:00:00";
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end = dateFormater.format(cal.getTime()) + " 23:59:59";
        try
        {
            startTime = format.parse(start).getTime();
            endTime = format.parse(end).getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
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
}
