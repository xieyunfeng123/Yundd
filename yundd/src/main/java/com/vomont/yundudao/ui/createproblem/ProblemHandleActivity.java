package com.vomont.yundudao.ui.createproblem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemDetailInfo;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemTypeBean;
import com.vomont.yundudao.mvpview.problem.ICenterProblemView;
import com.vomont.yundudao.mvpview.problem.IPronblemHandleView;
import com.vomont.yundudao.presenter.problem.ICenterProPresenter;
import com.vomont.yundudao.presenter.problem.IProblemHandlePresenter;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;

public class ProblemHandleActivity extends Activity implements OnClickListener, IPronblemHandleView, ICenterProblemView
{
    
    private ImageView problem_handle_back;
    
    private LinearLayout action_ll;
    
    private TextView rectification_complete;
    
    private TextView no_rectification;
    
    private EditText problem_action_speak;
    
    private Button problem_handle_request;
    
    private int action;
    
    private int problemid;
    
    private IProblemHandlePresenter presenter;
    
    private ShareUtil util;
    
    private boolean aciotn_type;
    
    private Dialog dialog;
    
    private ICenterProPresenter iCenterProPresenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_handle);
        iCenterProPresenter = new ICenterProPresenter(this);
        initView();
        initData();
        initListener();
        updateView();
    }
    
    private void initView()
    {
        problem_handle_back = (ImageView)findViewById(R.id.problem_handle_back);
        action_ll = (LinearLayout)findViewById(R.id.action_ll);
        rectification_complete = (TextView)findViewById(R.id.rectification_complete);
        no_rectification = (TextView)findViewById(R.id.no_rectification);
        problem_action_speak = (EditText)findViewById(R.id.problem_action_speak);
        problem_handle_request = (Button)findViewById(R.id.problem_handle_request);
    }
    
    private void initListener()
    {
        problem_handle_back.setOnClickListener(this);
        problem_handle_request.setOnClickListener(this);
        no_rectification.setOnClickListener(this);
        rectification_complete.setOnClickListener(this);
    }
    
    private void initData()
    {
        presenter = new IProblemHandlePresenter(this);
        util = new ShareUtil(this);
        Intent intent = getIntent();
        action = intent.getIntExtra("action", 0);
        problemid = intent.getIntExtra("problemid", 0);
    }
    
    private void updateView()
    {
        if (action == 2)
        {
            action_ll.setVisibility(View.GONE);
        }
        else if (action == 3)
        {
            rectification_complete.setText("已整改");
            no_rectification.setText(R.string.no_rectification);
            problem_action_speak.setVisibility(View.GONE);
        }
        else if (action == 4)
        {
            rectification_complete.setText("整改通过");
            no_rectification.setText("未通过");
            problem_action_speak.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.problem_handle_back:
                finish();
                break;
            case R.id.rectification_complete:
                aciotn_type = true;
                showTextView(aciotn_type);
                break;
            case R.id.no_rectification:
                aciotn_type = false;
                showTextView(aciotn_type);
                break;
            case R.id.problem_handle_request:
                try
                {
                    sendAction();
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    
    private void sendAction()
        throws UnsupportedEncodingException
    {
        String desp = "";
        
        if (action == 2)
        {
            if (!TextUtils.isEmpty(problem_action_speak.getText().toString()))
            {
                desp = problem_action_speak.getText().toString();
                if(desp.length()>20)
                {
                    Toast.makeText(this, "评论字数不能超过20个！", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            else
            {
                Toast.makeText(this, "评论不能为空！", Toast.LENGTH_LONG).show();
                return;
            }
            presenter.problemAction(util.getShare().getUser_id() + "", problemid + "", 2 + "", 0 + "", desp);
        }
        else if (action == 3)
        {
            if (!aciotn_type)
            {
                presenter.problemAction(util.getShare().getUser_id() + "", problemid + "", 3 + "", 3 + "", desp);
            }
            else
            {
                presenter.problemAction(util.getShare().getUser_id() + "", problemid + "", 3 + "", 2 + "", desp);
            }
        }
        else if (action == 4)
        {
            if (aciotn_type)
            {
                presenter.problemAction(util.getShare().getUser_id() + "", problemid + "", 4 + "", 4 + "", desp);
            }
            else
            {
                presenter.problemAction(util.getShare().getUser_id() + "", problemid + "", 4 + "", 5 + "", desp);
            }
        }
        dialog = ProgressDialog.createLoadingDialog(this, "上传中");
        dialog.show();
        
    }
    
    private void showTextView(boolean showView)
    {
        if (showView)
        {
            rectification_complete.setBackgroundResource(R.drawable.problem_handle_pressed_bg);
            rectification_complete.setTextColor(getResources().getColor(R.color.white));
            no_rectification.setBackgroundResource(R.drawable.problem_handle_bg);
            no_rectification.setTextColor(getResources().getColor(R.color.black));
        }
        else
        {
            no_rectification.setBackgroundResource(R.drawable.problem_handle_pressed_bg);
            no_rectification.setTextColor(getResources().getColor(R.color.white));
            rectification_complete.setBackgroundResource(R.drawable.problem_handle_bg);
            rectification_complete.setTextColor(getResources().getColor(R.color.black));
        }
    }
    
    @Override
    public void handleResult(int result)
    {
        if (result == 0)
        {
            ACache aCache = ACache.get(this);
            aCache.put("problemsend", "1");
            iCenterProPresenter.getProblemDetail(util.getShare().getUser_id() + "", problemid + "");
        }
        else
        {
            Toast.makeText(this, "提交失败", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }
    
    @Override
    public void getList(ProblemListBean bean)
    {
        
    }
    
    @Override
    public void getType(ProblemTypeBean result)
    {
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void getProbleDtail(ProblemDetailBean problemBean)
    {
        if (problemBean != null && problemBean.getResult() == 0)
        {
            ProblemDetailInfo problemDetailInfo = problemBean.getProblem();
            Intent intent = getIntent();
            problemDetailInfo.setImagecontents(null);
            problemDetailInfo.setImgeNames((List<String>)intent.getSerializableExtra("mlistPath"));
            intent.putExtra("problemDetailInfo", (Serializable)problemDetailInfo);
            setResult(RESULT_OK, intent);
            finish();
            dialog.dismiss();
        }
        else
        {
            dialog.dismiss();
        }
        Toast.makeText(this, "提交成功", Toast.LENGTH_LONG).show();
        finish();
    }
}
