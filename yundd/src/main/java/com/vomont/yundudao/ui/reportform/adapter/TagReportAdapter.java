package com.vomont.yundudao.ui.reportform.adapter;

import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.view.flow.FlowLayout;
import com.vomont.yundudao.view.flow.TagAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class TagReportAdapter extends TagAdapter<String>
{

    public TagReportAdapter(List<String> datas)
    {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String t)
    {
        return parent;
    }

}
