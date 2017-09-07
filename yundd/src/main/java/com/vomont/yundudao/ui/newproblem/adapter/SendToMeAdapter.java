package com.vomont.yundudao.ui.newproblem.adapter;

import com.vomont.yundudao.R;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SendToMeAdapter extends BaseAdapter
{
    
    private Context context;
    
    public SendToMeAdapter(Context context)
    {
        this.context = context;
    }
    
    @Override
    public int getCount()
    {
        return 10;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_problem_sendtome, null);
            holder = new Holder();
            holder.problem_img_list = (NoScrollGridView)convertView.findViewById(R.id.problem_img_list);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        
        ProblemImgAdapter adapter = new ProblemImgAdapter(context);
        holder.problem_img_list.setAdapter(adapter);
        
        return convertView;
    }
    
    class Holder
    {
        NoScrollGridView problem_img_list;
    }
    
}
