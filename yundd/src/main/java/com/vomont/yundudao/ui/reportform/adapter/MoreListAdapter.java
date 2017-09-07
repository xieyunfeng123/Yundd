package com.vomont.yundudao.ui.reportform.adapter;

import com.vomont.yundudao.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@SuppressLint("InflateParams") public class MoreListAdapter extends BaseAdapter
{

    private Context context;
    public MoreListAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount()
    {
        return 20;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder=null;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.item_more_report, null);
            holder=new Holder();
            convertView.setTag(holder);
        }
        else
        {
            holder=(Holder)convertView.getTag();
        }
        return convertView;
    }
    
    class Holder
    {
        
    }
}
