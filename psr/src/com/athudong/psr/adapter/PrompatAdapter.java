package com.athudong.psr.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.athudong.psr.R;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.util.AppUtil;

/**
 * ��ʾ������
 * @author л����
 */
public abstract class PrompatAdapter extends BaseAdap implements Filterable {

	protected BaseAct context;
	protected ArrayList<String> mObjects;//���˺��item
	protected boolean noAppend;
	private ArrayList<String> mOriginalValues ;//���е�Item
	private final Object mLock = new Object();
	private int maxMatch = -1 ;//�����ʾ���ٸ�ѡ�����ʾȫ��
	public PrompatAdapter(BaseAct activity){
		this.context = activity;
		mObjects = new ArrayList<String>();
		mOriginalValues = new ArrayList<String>();
	}

	protected void setMaxMatch(int maxMatch) {
		this.maxMatch = maxMatch;
	}

	@Override
	public int getCount() {
		
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		if(mObjects.size()>0){
			return mObjects.get(position);
		}else {
			return null;
		}
		
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drop_down_view, null);
			holder.searItem = (TextView) convertView.findViewById(R.id.ai_drop_down_view);
		    convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.searItem.setText(mObjects.get(position));
		return convertView;
	}
	
	/**
	 * �Ż�����,����TextView
	 */
	private class ViewHolder{
		TextView searItem;
	}

	@Override
	public Filter getFilter() {
		
		return new ArrayFilter();
	}
	
	protected abstract ArrayList<String> query(String params) throws Exception ;
	
	/**
	 * ��д���ַ�����������Խ���ģ����ѯ
	 */
	private class ArrayFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			
			FilterResults results = new FilterResults();
			
			if(prefix == null || prefix.length() == 0){
				synchronized (mLock) {
					ArrayList<String> list = new ArrayList<String>();
					results.values = list;
					results.count = list.size();
					return results;
				}
			}else {
				String prefixString = prefix.toString();
				try {
					mOriginalValues= query(prefixString);
					if(mOriginalValues !=null && mOriginalValues.size()>0){
						final int count = mOriginalValues.size();
						final ArrayList<String> newValues = new ArrayList<String>(count);
						for(int i = 0 ; i < count ; i++){
							
							if(AppUtil.isMatches(prefixString)){ //ƥ������ַ�
								String newString = null;
								if(i==0 && noAppend){
									newString = mOriginalValues.get(i);
								}else {
									newString = prefixString+mOriginalValues.get(i);
								}
								newValues.add(newString);
							}
							
							if(maxMatch > 0){ //��������
								if(newValues.size() > maxMatch - 1){
									break;
								}
							}
						}
						results.values = newValues;
						results.count = newValues.size();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) {
			if(results.values !=null){
				mObjects = (ArrayList<String>)results.values;
			}
			if(results.count > 0){
				notifyDataSetChanged();
			}else{
				notifyDataSetInvalidated();
			}
		}
	}
	
	public ArrayList<String> getAllItems(){
		return mOriginalValues;
	}

}
