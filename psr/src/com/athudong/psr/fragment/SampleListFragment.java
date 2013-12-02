package com.athudong.psr.fragment;

import com.athudong.psr.R;
import com.athudong.psr.activity.RegisterAct;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * listviewFragment
 */
public class SampleListFragment extends ListFragment implements OnClickListener {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.al_sligine_meu,null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for(int i = 0 ; i < 1;i++){
			adapter.add(new SampleItem("ÐÞ¸Ä×¢²á×ÊÁÏ"));
		}
		setListAdapter(adapter);
	}
	
	private class SampleItem {
		public String strText;
		
		public SampleItem(String string){
			this.strText = string;
		}
	}
	
	
	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.al_row, null);
			}
			Button btn = (Button) convertView.findViewById(R.id.row_modify_info);
			btn.setOnClickListener(SampleListFragment.this);
			btn.setContentDescription(position+"");
			btn.setText(getItem(position).strText);
			return convertView;
		}
	}


	@Override
	public void onClick(View v) {
		if(v instanceof Button){
			String index = (String) v.getContentDescription();
			if(!TextUtils.isEmpty(index)){
				switch(v.getId()){
				case R.id.row_modify_info:
					if(Integer.valueOf(index)==0){
						Intent intent = new Intent(getActivity(),RegisterAct.class);
						Bundle bundle = new Bundle();
						bundle.putString("flag","modifyInfo");
						intent.putExtra("bundle",bundle);
						startActivity(intent);
					}
					break;
				}
			}
		}
	}
}
