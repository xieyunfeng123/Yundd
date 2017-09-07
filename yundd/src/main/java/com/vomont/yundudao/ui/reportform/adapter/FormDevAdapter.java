package com.vomont.yundudao.ui.reportform.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.SubFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FormDevAdapter extends BaseAdapter
{
    private Context context;
    
    private List<SubFactory> mlist;
    
    public FormDevAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setData(List<SubFactory> mlist)
    {
        this.mlist = mlist;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        return null != mlist ? mlist.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return mlist.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_form_dev, null);
            holder = new Holder();
            holder.item_form_dev_name = (TextView)convertView.findViewById(R.id.item_form_dev_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }
        
        String name = mlist.get(position).getSubfactoryname();
        if (mlist.get(position).getMlist_device() != null && mlist.get(position).getMlist_device().size() != 0)
        {
            String item_name = "";
            List<DeviceInfo> deviceInfos = mlist.get(position).getMlist_device();
            for (int i = 0; i < deviceInfos.size(); i++)
            {
                if (i != (deviceInfos.size() - 1))
                    item_name = item_name + deviceInfos.get(i).getDevicename() + ";";
                else
                    item_name = item_name + deviceInfos.get(i).getDevicename();
            }
            name=name+"("+item_name+")";
        }
        holder.item_form_dev_name.setText(name);
        
        return convertView;
    }
    
    class Holder
    {
        TextView item_form_dev_name;
    }
}
