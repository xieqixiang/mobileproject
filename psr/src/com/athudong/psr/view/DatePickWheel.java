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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * �Զ�������ѡ��ؼ�
 * 
 * @author л����
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
	private Button btStartTime;
	private TextView tvStopTime;
	private int length;
	
	public void setLength(int length) {
		this.length = length;
	}

	public void setTvStopTime(TextView tvStopTime) {
		this.tvStopTime = tvStopTime;
	}

	public void setEtShowTime(Button btStartTime) {
		this.btStartTime = btStartTime;
	}

	// ����
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	// С��
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
		// ��
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new NumericAdap(START_YEAR, END_YEAR));// ����"��"����ʾ����

		// ��
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new NumericAdap(1, 12));

		// ��
		wv_day = (DayWheelView) findViewById(R.id.day);
		wv_day.setAdapter(new DateAdap());
		wv_day.setCyclic(true);

		// ʱ
		wv_hours = (WheelView) findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericAdap(0, 23));

		// ��
		wv_mins = (WheelView) findViewById(R.id.mins);
		wv_mins.setAdapter(new MinuteAdap(0, 59, "%02d"));

		setOnScrollingFinishedLinstener(new ScrollListener());
	}

	private void adjustView() {
		// ������Ļ�ܶ�ָ��ѡ��������Ĵ�С
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
	 * ������ֹ������
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
		wv_hours.setCurrentItem(hour);
		wv_mins.setCurrentItem((minute/2));
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
	
	public String getIndexDate(int index){
		Calendar calendar = getIndexCalender(index);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d�� HH:mm",Locale.CHINA);
		String dateString = sdf.format(date);
		return dateString;
	}
	
	public String getDate(){
		Calendar calendar = getSelectCalendar();
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d�� HH:mm",Locale.CHINA);
		String dateString = sdf.format(date);
		return dateString;
	}

	private class ScrollListener implements OnWheelScrollFinishListener {
		@Override
		public void scrollStop() {
			if (btStartTime != null && btStartTime instanceof Button) {
				btStartTime.setText(getDate());
				tvStopTime.setText("ֹͣʱ��:"+getIndexDate(length));
			}
		}
	}
}
