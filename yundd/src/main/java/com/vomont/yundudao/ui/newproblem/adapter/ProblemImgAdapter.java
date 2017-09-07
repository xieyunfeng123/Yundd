package com.vomont.yundudao.ui.newproblem.adapter;

import com.vomont.yundudao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public class ProblemImgAdapter extends BaseAdapter
{
    Context context;
    
    public ProblemImgAdapter(Context context)
    {
        this.context = context;
    }
    
    @Override
    public int getCount()
    {
        return 3;
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
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_img_list, null);
            holder = new Holder();
            convertView.setTag(holder);
            final View conView2=convertView;
            convertView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    AbsListView.LayoutParams param = new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, conView2.getWidth());
                    conView2.setLayoutParams(param); 
                }
            });
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }

     
        return convertView;
    }
    
    class Holder
    {
        
    }
    
}
