package com.athudong.psr.fragment;

import com.athudong.psr.R;
import com.athudong.psr.activity.IncomeAct;
import com.athudong.psr.activity.RegisterAct;
import com.athudong.psr.activity.RentManagerAct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
		adapter.add(new SampleItem("车位管理"));
		adapter.add(new SampleItem("收益报表"));
		adapter.add(new SampleItem("修改注册资料"));
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
			int index = Integer.valueOf(v.getContentDescription().toString());
				switch(index){
				case 0:
					overLayout(RentManagerAct.class,0);
					break;
				case 1:
					overLayout(IncomeAct.class,0);
					break;
				case 2:
					Intent intent = new Intent(getActivity(),RegisterAct.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag","modifyInfo");
					intent.putExtra("bundle",bundle);
					startActivity(intent);
					break;
			}
		}
	}
	
	private void overLayout(Class<?> class1,int flag){
		Activity activity = getActivity();
		Intent intent = new Intent(activity,class1);
		Bundle bundle = new Bundle();
		bundle.putInt("flag", flag);
		intent.putExtra("bundle",bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		activity.startActivity(intent);
	}
}
