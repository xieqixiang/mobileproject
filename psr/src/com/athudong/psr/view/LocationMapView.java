package com.athudong.psr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * 
 * @author л����
 */
public class LocationMapView extends MapView {
	
	public PopupOverlay popOverlay = null; // ��������ͼ�㣬���ͼƬʹ��

	public LocationMapView(Context context) {
		super(context);
	}

	public LocationMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LocationMapView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// ��������
			if (popOverlay != null&& event.getAction() == MotionEvent.ACTION_UP)
				popOverlay.hidePop();
		}
		return true;
	}
}
