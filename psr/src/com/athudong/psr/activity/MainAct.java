package com.athudong.psr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.athudong.psr.R;
import com.athudong.psr.adapter.FragmentPagerAdap;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.fragment.SampleListFragment;
import com.athudong.psr.view.manager.DialogManager;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页
 */
public class MainAct extends SlidingFragmentActivity implements OnClickListener {
	private ViewPager vp;
	FragmentPagerAdap mPagerAdapter;
	protected ListFragment mFragment;
	private SlidingMenu sm ;
	//private Button btn1, btn2;
	//private Resources resources;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_main);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		//resources = getResources();
		vp = (ViewPager) findViewById(R.id.ai_main_viewpager);
		//btn1 = (Button)findViewById(R.id.ai_main_parking_space);
		//btn2 = (Button)findViewById(R.id.ai_main_rent_parking_space);
		
		setBehindContentView(R.layout.al_menu_frame);
		
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mFragment  = new SampleListFragment();
		ft.replace(R.id.menu_frame,mFragment);
		ft.commit();
		
		sm = getSlidingMenu();
		Button btnExistLogin = (Button) sm.findViewById(R.id.mf_exist_login);
		btnExistLogin.setOnClickListener(this);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.ad_shadow);
	    
		sm.setMode(SlidingMenu.RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width);
		sm.setFadeDegree(0.35f);
		setSlidingActionBarEnabled(false);
		mPagerAdapter = new FragmentPagerAdap(getSupportFragmentManager());
		vp.setAdapter(mPagerAdapter);
		vp.setCurrentItem(0);
	}

	private void initData() {
		//btn1.setTextColor(resources.getColor(R.color.blue));
		//btn2.setTextColor(resources.getColor(R.color.black));
	}

	private void initListener() {
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				//setSelector(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	
	@Override
	protected void onDestroy() {
		BaseApp app = (BaseApp) this.getApplication();
		if(app.mBMapManager !=null){
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		super.onDestroy();
	}

	/** 设置选中效果 */
	/*public void setSelector(int id) {
		switch(id){
		case 0:
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			btn1.setTextColor(resources.getColor(R.color.blue));
			btn2.setTextColor(resources.getColor(R.color.black));
			break;
		case 1:
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			btn2.setTextColor(resources.getColor(R.color.blue));
			btn1.setTextColor(resources.getColor(R.color.black));
			break;
		}
	}*/

	public void controlClick(View view) {
		switch (view.getId()) {
		case R.id.ai_main_parking_space:
			vp.setCurrentItem(0);
			break;
		case R.id.ai_main_rent_parking_space:
			vp.setCurrentItem(1);
			break;
		case R.id.ai_main_menu:
			sm.toggle();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.mf_exist_login:
			DialogManager.showAlertDialog(this,"",getString(R.string.logout),this);
			break;
		case R.id.alert_negative:
			DialogManager.closeAlertDialog();
			break;
		case R.id.alert_positive:
			DialogManager.closeAlertDialog();
			Intent intent = new Intent(this,LoginAct.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			this.finish();
			break;
		}
	}
}
