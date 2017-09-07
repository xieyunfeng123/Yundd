package com.vomont.yundudao.presenter.patrol;

import com.vomont.yundudao.bean.PatrolDetailItem;
import com.vomont.yundudao.bean.PatrolList;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.patrol.IPatrolModel;
import com.vomont.yundudao.model.patrol.PatrolModelImpl;
import com.vomont.yundudao.mvpview.patrol.IPatrolDetailView;
import com.vomont.yundudao.mvpview.patrol.IPatrolListView;

public class PatrolListPresenter {

	private IPatrolModel model;

	private IPatrolListView view;
	
	private IPatrolDetailView detailView;

	public PatrolListPresenter(IPatrolListView view) {
		this.view = view;
		model = new PatrolModelImpl();
	}
	
//	public PatrolListPresenter(IPatrolDetailView detailView ){
//		this.detailView = detailView;
//		model = new PatrolModelImpl();
//	}
	
	
	

	public void getPatrolList(int userid, String searchdescription, int pageidx) {
		model.getPatrolList(userid, searchdescription, pageidx,
				new OnHttpListener() {

					@Override
					public void onSucess(Object object) {
						view.getListData(((PatrolList) object).getVideos());
					}

					@Override
					public void onFail() {
						view.onFail();
					}
				});
	}
	
	
	public void getPatrolDetail(int userid,int videoid)
	{
		model.getPatrolDetail(userid, videoid, new OnHttpListener() {
			
			@Override
			public void onSucess(Object object) {
			    view.getSucess((PatrolDetailItem)object);
			}
			
			@Override
			public void onFail() {
			    view.getFail();
			}
		});
	}

}
