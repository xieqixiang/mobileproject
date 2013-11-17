package com.athudong.psr.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdapFragmentPager extends FragmentStatePagerAdapter {

	public AdapFragmentPager(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = new AdapFragment();
		Bundle args = new Bundle();
		args.putInt(AdapFragment.ARG_OBJECT,arg0);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
