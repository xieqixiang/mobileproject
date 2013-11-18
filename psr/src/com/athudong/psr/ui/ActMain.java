package com.athudong.psr.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import com.athudong.psr.R;
import com.athudong.psr.adapter.AdapFragmentPager;
import com.athudong.psr.base.BaseApp;
import com.athudong.psr.fragment.SampleListFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页
 */
public class ActMain extends SlidingFragmentActivity {
	private ViewPager vp;
	AdapFragmentPager mPagerAdapter;
	protected ListFragment mFragment;
	private SlidingMenu sm ;
	private Button btn1, btn2;
	private Resources resources;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_main);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		resources = getResources();
		vp = (ViewPager) findViewById(R.id.ai_main_viewpager);
		btn1 = (Button)findViewById(R.id.ai_main_parking_space);
		btn2 = (Button)findViewById(R.id.ai_main_rent_parking_space);
		
		setBehindContentView(R.layout.al_menu_frame);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mFragment  = new SampleListFragment();
		ft.replace(R.id.menu_frame,mFragment);
		ft.commit();
		
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.ad_shadow);
		sm.setMode(SlidingMenu.RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width);
		sm.setFadeDegree(0.35f);
		
		setSlidingActionBarEnabled(false);
		
		mPagerAdapter = new AdapFragmentPager(getSupportFragmentManager());
		vp.setAdapter(mPagerAdapter);
		vp.setCurrentItem(0);
	}

	private void initData() {
		btn1.setTextColor(resources.getColor(R.color.blue));
		btn2.setTextColor(resources.getColor(R.color.black));
	}

	private void initListener() {
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				setSelector(arg0);
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
	public void setSelector(int id) {
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
	}

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
}
