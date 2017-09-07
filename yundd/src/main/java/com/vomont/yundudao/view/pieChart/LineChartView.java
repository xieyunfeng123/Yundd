package com.vomont.yundudao.view.pieChart;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.view.ChartView;

import com.vomont.yundudao.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class LineChartView extends ChartView implements Runnable
{
    private String TAG = "LineChart02View";
    
    private LineChart chart = new LineChart();
    
    // 标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    
    private LinkedList<LineData> chartData = new LinkedList<LineData>();
    
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
    
    public LineChartView(Context context)
    {
        super(context);
        initView();
    }
    
    public LineChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public LineChartView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    private void initView()
    {
        chartLabels(null);
        chartRender();
        chartDataSet(null);
        new Thread(this).start();
        // 綁定手势滑动事件
        this.bindTouch(this, chart);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        chart.setChartRange(w, h);
    }
    
    private void chartRender()
    {
        try
        {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(DensityUtil.dip2px(getContext(), 45), ltrb[1], ltrb[2], ltrb[3]);
            // 设定数据源
            chart.setCategories(labels);
            
            // 数据轴最大值
            chart.getDataAxis().setAxisMax(100);
            // 数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(20);
            // 指隔多少个轴刻度(即细刻度)后为主刻度
            // chart.getDataAxis().setDetailModeSteps(1);
            
            // 底部的标题
            // chart.getAxisTitle().setLeftTitle("问题数量");
            // chart.getAxisTitle().getLeftTitlePaint().setColor(Color.GRAY);
            // chart.getAxisTitle().getLeftTitlePaint().setTextSize(DensityUtil.dip2px(getContext(), 10));
            // 左边的标题
            // chart.getAxisTitle().setLowerTitle("时间");
            // chart.getAxisTitle().getLowerTitlePaint().setColor(Color.GRAY);
            // chart.getAxisTitle().getLowerTitlePaint().setTextSize(DensityUtil.dip2px(getContext(), 10));
            
            // 背景网格
            chart.getPlotGrid().showHorizontalLines();
            chart.getPlotGrid().getHorizontalLinePaint().setColor(getResources().getColor(R.color.gray_qian));
            chart.getPlotGrid().getHorizontalLinePaint().setStrokeWidth(0.1f);
            chart.getPlotGrid().showVerticalLines();
            chart.getPlotGrid().getVerticalLinePaint().setColor(getResources().getColor(R.color.gray_qian));
            chart.getPlotGrid().getVerticalLinePaint().setStrokeWidth(0.1f);
            // 设置轴风格
            // chart.getDataAxis().setTickMarksVisible(false);
            chart.getDataAxis().getAxisPaint().setStrokeWidth(2);
            chart.getDataAxis().getTickMarksPaint().setStrokeWidth(2);
            // chart.getDataAxis().showAxisLabels();
            chart.getDataAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getDataAxis().getAxisPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getDataAxis().hideTickMarks();
            
            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(2);
            chart.getCategoryAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getCategoryAxis().getAxisPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getCategoryAxis().hideTickMarks();
            
            // 定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack()
            {
                @Override
                public String textFormatter(String value)
                {
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }
            });
            
            // 定义线上交叉点标签显示格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack()
            {
                @Override
                public String doubleFormatter(Double value)
                {
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }
            });
            
            // 允许线与轴交叉时，线会断开
            chart.setLineAxisIntersectVisible(false);
            
            // 动态线
            chart.showDyLine();
            
            // 不封闭
            chart.setAxesClosed(false);
            
            // 扩展绘图区右边分割的范围，让定制线的说明文字能显示出来
            chart.getClipExt().setExtRight(150.f);
            
            // 设置标签交错换行显示
            chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.NORMAL);
            
            // 禁止滑动
            chart.disablePanMode();
            
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }
    }
    
    public void chartDataSet(HashMap<String, List<Double>> map)
    {
        
        // Line 1
        LinkedList<Double> dataSeries1 = new LinkedList<Double>();
        dataSeries1.add(1d);
        dataSeries1.add(3d);
        dataSeries1.add(2d);
        dataSeries1.add(5d);
        dataSeries1.add(6d);
        dataSeries1.add(8d);
        dataSeries1.add(4d);
        
        LineData lineData1 = new LineData("单间(5层光线好)", dataSeries1, Color.rgb(234, 83, 71));
        lineData1.setDotStyle(XEnum.DotStyle.RING);
        lineData1.getLinePaint().setStrokeWidth(3);
        lineData1.getDotPaint().setStrokeWidth(0.5f);
        // //Line 2
        LinkedList<Double> dataSeries2 = new LinkedList<Double>();
        dataSeries2.add(2d);
        dataSeries2.add(8d);
        dataSeries2.add(7d);
        dataSeries2.add(9d);
        dataSeries2.add(0.1d);
        dataSeries2.add(2d);
        dataSeries2.add(5d);
        
        LineData lineData2 = new LineData("一房一厅(3层无光线)", dataSeries2, Color.rgb(75, 166, 51));
        lineData2.setDotStyle(XEnum.DotStyle.RING);
        lineData2.getLinePaint().setStrokeWidth(3);
        // Line 3
        LinkedList<Double> dataSeries3 = new LinkedList<Double>();
        dataSeries3.add(2d);
        dataSeries3.add(5d);
        dataSeries3.add(8d);
        dataSeries3.add(3d);
        dataSeries3.add(1d);
        dataSeries3.add(5d);
        dataSeries3.add(6d);
        
        LineData lineData3 = new LineData("单间(9层无电梯)", dataSeries3, Color.rgb(123, 89, 168));
        lineData3.setDotStyle(XEnum.DotStyle.RING);
        lineData3.getLinePaint().setStrokeWidth(3);
        chartData.add(lineData1);
        chartData.add(lineData2);
        chartData.add(lineData3);
    }
    
    public void chartLabels(List<String> times)
    {
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
    }
    
    @Override
    public void render(Canvas canvas)
    {
        try
        {
            chart.render(canvas);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            chartAnimation();
        }
        catch (Exception e)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    private void chartAnimation()
    {
        try
        {
            
            if (chartData != null && chartData.size() != 0)
            {
                int count = chartData.size();
                for (int i = 0; i < count; i++)
                {
                    Thread.sleep(200);
                    LinkedList<LineData> animationData = new LinkedList<LineData>();
                    for (int j = 0; j <= i; j++)
                    {
                        animationData.add(chartData.get(j));
                    }
                    // Log.e(TAG,"size = "+animationData.size());
                    chart.setDataSource(animationData);
                    if (i == count - 1)
                    {
                        chart.getDataAxis().show();
                        chart.getDataAxis().showAxisLabels();
                        chart.setCustomLines(mCustomLineDataset);
                    }
                    
                    postInvalidate();
                }
            }
        }
        catch (Exception e)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        
        super.onTouchEvent(event);
        
        // if(event.getAction() == MotionEvent.ACTION_UP)
        // {
        // //交叉线
        // if(chart.getDyLineVisible())
        // {
        // chart.getDyLine().setCurrentXY(event.getX(),event.getY());
        // if(chart.getDyLine().isInvalidate())this.invalidate();
        // }
        // }
        return true;
    }
    
    // Demo中bar chart所使用的默认偏移值。
    // 偏移出来的空间用于显示tick,axistitle....
    protected int[] getBarLnDefaultSpadding()
    {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); // left
        ltrb[1] = DensityUtil.dip2px(getContext(), 60); // top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); // right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); // bottom
        return ltrb;
    }
    
    protected int[] getPieDefaultSpadding()
    {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 20); // left
        ltrb[1] = DensityUtil.dip2px(getContext(), 65); // top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); // right
        ltrb[3] = DensityUtil.dip2px(getContext(), 20); // bottom
        return ltrb;
    }
}
