package com.athudong.psr.fragment;

import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.app.SherlockFragment;
import com.athudong.psr.R;

public class FragmentDemo extends SherlockFragment {
	public static final String ARG_OBJECT = "object";
	
	public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		Bundle args = getArguments();
		int index = args.getInt(ARG_OBJECT);
		View rootView = null;
		if(index==0){
			rootView = inflater.inflate(R.layout.al_parking_space, container,false);
		}
		if(index==1){
			rootView = inflater.inflate(R.layout.al_rent_parking_space, container,false);
		}
		return rootView;
	};
}
