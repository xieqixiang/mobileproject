package com.athudong.psr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * 
 * @author Ð»ÆôÏé
 */
public class LocationMapView extends MapView {
	
	public PopupOverlay popOverlay = null; // µ¯³öÅÝÅÝÍ¼²ã£¬µã»÷Í¼Æ¬Ê¹ÓÃ

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
			// ÏûÒþÅÝÅÝ
			if (popOverlay != null&& event.getAction() == MotionEvent.ACTION_UP)
				popOverlay.hidePop();
		}
		return true;
	}
}
