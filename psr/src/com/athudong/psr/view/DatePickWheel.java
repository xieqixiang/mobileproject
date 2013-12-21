package com.athudong.psr.view;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.athudong.psr.R;
import com.athudong.psr.adapter.DateAdap;
import com.athudong.psr.adapter.MinuteAdap;
import com.athudong.psr.adapter.NumericAdap;
import com.athudong.psr.view.listener.OnWheelScrollFinishListener;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义日期选择控件
 * 
 * @author 谢启祥
 */
public class DatePickWheel extends LinearLayout {

	private static int START_YEAR = 1990, END_YEAR = 2100;

	private WheelView wv_year;
	private WheelView wv_month;
	private DayWheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	private Calendar calendar;
	private OnWheelScrollFinishListener scrollFinishlistener;
	private TextView tvStartTime;
	private TextView tvStopTime;
	private int length;
	private boolean justHourMinute;
	private Resources resources;
	
	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public WheelView getWv_year() {
		return wv_year;
	}

	public WheelView getWv_month() {
		return wv_month;
	}

	public DayWheelView getWv_day() {
		return wv_day;
	}

	public WheelView getWv_hours() {
		return wv_hours;
	}

	public WheelView getWv_mins() {
		return wv_mins;
	}

	public void setJustHourMinute(boolean justHourMinute) {
		this.justHourMinute = justHourMinute;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setTvStopTime(TextView tvStopTime) {
		this.tvStopTime = tvStopTime;
	}

	public void setTvShowTime(TextView tvStartTime) {
		this.tvStartTime = tvStartTime;
	}

	// 大月
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	// 小月
	String[] months_little = { "4", "6", "9", "11" };

	final List<String> list_big = Arrays.asList(months_big);
	final List<String> list_little = Arrays.asList(months_little);
	private final Context mContext;

	public DatePickWheel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public DatePickWheel(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		findView();
		adjustView();
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		setDate(calendar);
	}

	private void findView() {
		// 年
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new NumericAdap(START_YEAR, END_YEAR));// 设置"年"的显示数据

		// 月
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new NumericAdap(1, 12));

		// 日
		wv_day = (DayWheelView) findViewById(R.id.day);
		wv_day.setAdapter(new DateAdap());
		wv_day.setCyclic(true);

		// 时
		wv_hours = (WheelView) findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericAdap(0, 23));

		// 分
		wv_mins = (WheelView) findViewById(R.id.mins);
		wv_mins.setAdapter(new MinuteAdap(0, 59, "%02d"));

		setOnScrollingFinishedLinstener(new ScrollListener());
	}

	private void adjustView() {
		// 根据屏幕密度指定选择器字体的大小
		int textSize = 0;
		textSize = pixelsToDip(mContext.getResources(), 15);
		wv_day.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
	}

	public static int pixelsToDip(Resources res, int pixels) {
		final float scale = res.getDisplayMetrics().density;
		return (int) (pixels * scale + 2.0f);
	}

	/**
	 * 滚动制止监听器
	 */
	public void setOnScrollingFinishedLinstener(
			OnWheelScrollFinishListener listener) {

		if (listener != null) {
			this.scrollFinishlistener = listener;
			wv_day.addScrollingFinishedListener(scrollFinishlistener);
			wv_hours.addScrollingFinishedListener(scrollFinishlistener);
			wv_mins.addScrollingFinishedListener(scrollFinishlistener);
		}
	}

	public DatePickWheel setDate(Calendar calendar) {
		if (calendar == null) {
			return this;
		}
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wv_day.setCurrentItem(3);
		hour = minute >=55 ? hour+1:hour;
		wv_hours.setCurrentItem(hour);
		int index = minute /5;
		wv_mins.setCurrentItem((index+1));
		return this;
	}

	public Calendar getSelectCalendar() {
		// TODO Auto-generated method stub
		TimeObject timeObject = wv_day.getAdapter().getTimeObjects().get(3);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeObject.startTime);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), wv_hours.getCurrentItem(),wv_mins.getCurrentItem(), 0);
		return calendar2;
	}

	public Calendar getIndexCalender(int index) {
		TimeObject timeObject = wv_day.getAdapter().getTimeObjects().get(3);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeObject.startTime);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),(wv_hours.getCurrentItem()+index),wv_mins.getCurrentItem(), 0);
		return calendar2;
	}
	
	public Calendar getIndexCalender(int hour,int minute) {
		TimeObject timeObject = wv_day.getAdapter().getTimeObjects().get(3);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeObject.startTime);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),(wv_hours.getCurrentItem()+hour),(wv_mins.getCurrentItem()+minute), 0);
		return calendar2;
	}
	
	public String getIndexDate(int index){
		Calendar calendar = getIndexCalender(index);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm",Locale.CHINA);
		String dateString = sdf.format(date);
		return dateString;
	}
	
	public String getIndexDate(int hour , int minute){
		Calendar calendar = getIndexCalender(hour,minute);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm",Locale.CHINA);
		String dateString = sdf.format(date);
		return dateString;
	}
	
	public String getDate(){
		
		Calendar calendar = getSelectCalendar();
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm",Locale.CHINA);
		String dateString = sdf.format(date);
		
		if(justHourMinute){
		 	
		 	int start= dateString.indexOf("日");
		 	return dateString.substring((start+2));
		}
		return dateString;
	}
	
	public long getStartStime(){
		Calendar calendar = getSelectCalendar();
		Date date = calendar.getTime();
		return date.getTime();
	}

	private class ScrollListener implements OnWheelScrollFinishListener {
		@Override
		public void scrollStop() {
			if (tvStartTime != null && tvStartTime instanceof TextView) {
				tvStartTime.setText(getDate());
				int color = tvStartTime.getCurrentTextColor();
				tvStartTime.setTextColor(color==resources.getColor(R.color.red)?resources.getColor(R.color.drak_blue):resources.getColor(R.color.red));
				if(tvStopTime !=null){
					tvStopTime.setText(getIndexDate(length));
					int color2 = tvStopTime.getCurrentTextColor();
					tvStopTime.setTextColor(color2==resources.getColor(R.color.drak_green)?resources.getColor(R.color.blue):resources.getColor(R.color.drak_green));
				}
			}
		}
	}
}
