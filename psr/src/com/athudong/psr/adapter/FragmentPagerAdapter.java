package com.athudong.psr.adapter;

import com.athudong.psr.fragment.FragmentDemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

	public FragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = new FragmentDemo();
		Bundle args = new Bundle();
		args.putInt(FragmentDemo.ARG_OBJECT,arg0);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
