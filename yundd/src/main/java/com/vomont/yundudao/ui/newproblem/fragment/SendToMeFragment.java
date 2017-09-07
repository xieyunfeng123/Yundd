package com.vomont.yundudao.ui.newproblem.fragment;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.newproblem.adapter.SendToMeAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SendToMeFragment extends Fragment
{
    
    private PullToRefreshListView problem_tome_list;
    
    private SendToMeAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_problem_tome, container, false);
        problem_tome_list = (PullToRefreshListView)view.findViewById(R.id.problem_tome_list);
        adapter = new SendToMeAdapter(getActivity());
        problem_tome_list.setAdapter(adapter);
        return view;
    }
}
