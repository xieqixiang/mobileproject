package com.athudong.psr.adapter;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.model.ParkingSpace;


/**
 * ͣ�����б�
 * @author л����
 */
public class ParkingListAdapter extends BaseAdap {
	
	private ArrayList<ParkingSpace> parkingList;
	private BaseAct activity;
	
	/**����ͣ�����б�*/
	public void setParkingList(ArrayList<ParkingSpace> parkingList) {
		this.parkingList = parkingList;
	}

	public ParkingListAdapter(BaseAct activity){
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		if(parkingList ==null){
			return 0;
		}
		return 1;
	}
	
	@Override
	public Object getItem(int position) {
		
		return parkingList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolde vHolde;
		if(convertView == null){
			convertView = View.inflate(activity, R.layout.al_parking_list_item,null);
		    vHolde = new ViewHolde();
		    vHolde.tvParkingDescribe = (TextView) convertView.findViewById(R.id.ai_pli_describe);
		
		    convertView.setTag(vHolde);
		}else {
			vHolde = (ViewHolde) convertView.getTag();
		}
		ParkingSpace ps = parkingList.get(position);
		vHolde.tvParkingDescribe.setText(ps.getStrParkAddress());
		return convertView;
	}
	
	private class ViewHolde{
		/**ͣ��������*/
		public TextView tvParkingDescribe;
	}
	
}
