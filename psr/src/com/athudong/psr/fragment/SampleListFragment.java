package com.athudong.psr.fragment;

import com.athudong.psr.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * listviewFragment
 */
public class SampleListFragment extends ListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	
		return inflater.inflate(R.layout.al_sligine_meu,null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for(int i = 0 ; i < 1;i++){
			adapter.add(new SampleItem());
		}
		setListAdapter(adapter);
	}
	
	private class SampleItem {
		//public Button modifyInfo;
		//public Button existLogin;
	}
	
	
	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.al_row, null);
			}

			return convertView;
		}
	}
}
