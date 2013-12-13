package com.athudong.psr.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.adapter.RentPlanAdapter;
import com.athudong.psr.adapter.ViewPagerAdap;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.base.C;
import com.athudong.psr.model.RentPlan;

/**
 * 修改放租方案
 * @author 谢启祥
 */
public class ModifyRentManagerAct extends BaseAct {
	private ViewPager vp;
	private ViewPagerAdap vpAdapter;
	private ListView listView1, listView2;
	private ArrayList<View> arrayList;
	private Button btnStartTime, btnStopTime;
	private Resources resources;
	private int flag;
	private ArrayList<RentPlan> arrayList2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_modify_rent_plan);
		initView();
		initListener();
	}

	private void initView() {
		BaseApp application = (BaseApp) getApplication();
		
		TextView tvTitle = getView(R.id.ai_head_tv);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		flag = bundle.getInt("flag");
		if(flag==1){
			arrayList2 = application.rPlans;
			tvTitle.setText(getString(R.string.as_modify_daily_rent));
		}else {
			arrayList2 = application.rPlans2;
			tvTitle.setText(getString(R.string.as_modify_temporary_rent));
		}
		
		vp = getView(R.id.ai_amr_viewpager);
		vpAdapter = new ViewPagerAdap(this);
		View viewpage1 = getLayoutInflater().inflate(R.layout.al_rent_manager_page1, null);
		Button btnAdd1= (Button) viewpage1.findViewById(R.id.ai_rmp1_add);
		if(flag==1){
			btnAdd1.setVisibility(View.GONE);
		}
		
		View viewpage2 = getLayoutInflater().inflate(R.layout.al_rent_manager_page2, null);
		Button btnAdd2 = (Button) viewpage2.findViewById(R.id.ai_rmp2_add);
		if(flag==1){
			btnAdd2.setVisibility(View.GONE);
		}
		
		listView1 = (ListView) viewpage1.findViewById(R.id.lv_viewpage1);
		listView2 = (ListView) viewpage2.findViewById(R.id.lv_viewpage2);
		
		
		RentPlanAdapter rpAdapter1 = new RentPlanAdapter(this);
		rpAdapter1.setArrayList(arrayList2);
		rpAdapter1.setFlag(C.flag.startRent);
		listView1.setAdapter(rpAdapter1);

		RentPlanAdapter rpAdapter2 = new RentPlanAdapter(this);
		rpAdapter2.setArrayList(arrayList2);
		rpAdapter2.setFlag(C.flag.stopRent);
		listView2.setAdapter(rpAdapter2);

		arrayList = new ArrayList<View>();
		arrayList.add(viewpage1);
		arrayList.add(viewpage2);
		vpAdapter.setViews(arrayList);
		vp.setAdapter(vpAdapter);

		btnStartTime = getView(R.id.ai_mrp_rent_plan);
		btnStopTime = getView(R.id.ai_mrp_stop_rent_plan);
		resources = this.getResources();
		btnStartTime.setTextColor(resources.getColor(R.color.red));
	}

	private void initListener() {
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					btnStartTime.setTextColor(getResources().getColor(R.color.red));
					btnStopTime.setTextColor(getResources().getColor(R.color.dark_grey));
				}
				if (arg0 == 1) {
					btnStopTime.setTextColor(resources.getColor(R.color.red));
					btnStartTime.setTextColor(getResources().getColor(R.color.dark_grey));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public void controlClick(View view) {
		switch (view.getId()) {
		case R.id.ai_head_left:
			this.finish();
			break;
		case R.id.ai_mrp_rent_plan:
			vp.setCurrentItem(0);
			btnStartTime.setTextColor(getResources().getColor(R.color.red));
			btnStopTime.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.ai_mrp_stop_rent_plan:
			vp.setCurrentItem(1);
			btnStopTime.setTextColor(resources.getColor(R.color.red));
			btnStartTime.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.ai_rmp1_add:
		case R.id.ai_rmp2_add:
			overLayer(ModifyRentPlanAct.class, this);
			break;
		}
	}
}
