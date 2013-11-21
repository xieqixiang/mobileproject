package com.athudong.psr.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentPagerAdap extends FragmentStatePagerAdapter {

	public FragmentPagerAdap(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = new FragmentAdap();
		Bundle args = new Bundle();
		args.putInt(FragmentAdap.ARG_OBJECT,arg0);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
