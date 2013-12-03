package com.athudong.psr.view;

import com.athudong.psr.R;
import com.athudong.psr.base.BaseAdap;
import com.athudong.psr.base.C;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * ����ˢ��
 */
public class CustomerListView extends ListView implements OnScrollListener {

	private Context context;
	private int firstVisibleIndex;
	private ImageView arrow;
	private View headView, footView;
	private ProgressBar headProBar, footProBar;
	private TextView alert;
	private int headContentHeight, footContentHeight;
	private BaseAdap adapter;
	public long lastRefreshTime = 0, firstRefreshTime = 0;

	private static final int PULL_TO_REFRESH = 0;// ����״̬
	private static final int RELEASE_TO_REFRESH = 1;// �ɿ�״̬
	private static final int REFRESHING = 2;// ����״̬
	private static final int DONE = 3;// ����������
	private static final int RATIO = 3;// ʵ�ʾ����ڽ����Ͼ���֮��ı���ֵ

	private int state; // ��ǰ����ˢ�¿ؼ���״̬
	private int i = 0;
	private OnRefreshListener refreshListener;// ˢ�¼�����
	// isRefreshAble ��¼�Ƿ��ܹ�ˢ�� , isRecoder��¼startY,��ʼλ�ã������������У�ֻ��¼һ��,
	private boolean isRefreshAble, isRecord, haveMoreDate, isBack, isPullDown;
	private float startY;
	private Animation animation, recoverAnimation;

	public boolean getIsPullDown() {
		return this.isPullDown;
	}

	public CustomerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CustomerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomerListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		headView = inflater.inflate(R.layout.pull_down, null);
		headView.setEnabled(false);

		footView = inflater.inflate(R.layout.pull_up, null);
		footView.setEnabled(false);
		footProBar = (ProgressBar) footView.findViewById(R.id.ai_pu_moredata_loading);

		arrow = (ImageView) headView.findViewById(R.id.ai_pd_arrow);
		headProBar = (ProgressBar) headView.findViewById(R.id.ai_pd_pb);
		alert = (TextView) headView.findViewById(R.id.ai_pd_refresh_alert);

		arrow.setMinimumHeight(60);

		measureView(headView);
		measureView(footView);

		headContentHeight = headView.getMeasuredHeight();
		footContentHeight = footView.getMeasuredHeight();

		// ����headView������ϱ߾�ľ���
		headView.setPadding(0, -1 * headContentHeight, 0, 0);

		// ����footView,������ϱ߾�ľ���
		footView.setPadding(0, -1 * footContentHeight, 0, 0);

		headView.invalidate();// headView�ػ�

		addHeaderView(headView);
		addFooterView(footView);
		footView.setVisibility(View.GONE);

		setOnScrollListener(this);

		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,05f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(250);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());

		recoverAnimation = new RotateAnimation(0, -180,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		recoverAnimation.setDuration(200);
		recoverAnimation.setFillAfter(true);
		recoverAnimation.setInterpolator(new LinearInterpolator());

		state = DONE;
		isRefreshAble = false;
	}

	// ����HeadView���
	private void measureView(View child) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
		int childMeasureHeight;
		if (lp.height > 0) {
			childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY);// �ʺϡ�ƥ��
		} else {
			childMeasureHeight = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);// δָ��
		}
		child.measure(childMeasureWidth, childMeasureHeight);
	}

	public View getHeadView() {
		return headView;
	}

	public View getFootView() {
		return footView;
	}

	public void removeFootView() {
		removeFooterView(footView);
	}

	public void setHaveMoreDate(boolean haveMoreDate) {
		this.haveMoreDate = haveMoreDate;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			if (view.getLastVisiblePosition() == (view.getCount() - 1)
					&& headProBar.getVisibility() == View.GONE) {// ��������ȡ��������
				if (haveMoreDate) {
					haveMoreDate = false;
					footView.setPadding(0, (getHeight() / footContentHeight)
							- RATIO, 0, 0);
					setSelection((getAdapter().getCount() - 1));
					footProBar.setVisibility(View.VISIBLE);
					onRefresh(C.flag.pullUp);// ˢ�µõ�����������
				}
			}
			adapter.setBlBusy(false);
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			adapter.setBlBusy(true);
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			adapter.setBlBusy(false);
			break;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstVisibleIndex = firstVisibleItem;
	}

	/** ����ˢ�¼����� */
	public interface OnRefreshListener {
		abstract void onRefresh(int flag);
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		refreshListener = listener;
		isRefreshAble = true;
	}

	// ����ˢ����ɺ�ִ�з���
	// ����ˢ��ģʽ�ĸı�ʱ��ĸ���
	public void onRefreshComplete() {
		state = DONE;
		changeHeadViewOfState();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isRefreshAble) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstVisibleIndex == 0 && isRecord) {
					startY = ev.getY();
					isRecord = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float tempY = ev.getY();
				if (firstVisibleIndex == 0 && !isRecord) {
					startY = tempY;
					isRecord = true;
				}
				if (state != REFRESHING) {
					if (state == PULL_TO_REFRESH) {
						if ((tempY - startY) / RATIO > headContentHeight
								&& tempY - startY > 0) {
							// ����ˢ��-->>�ɿ�ˢ��
							state = RELEASE_TO_REFRESH;
							changeHeadViewOfState();
						} else if (tempY - startY <= 0) {
							// ����ˢ��-->>�ص�ˢ�����
							state = DONE;
							changeHeadViewOfState();
						}
					}

					if (state == RELEASE_TO_REFRESH) {
						if ((tempY - startY) / RATIO < headContentHeight
								&& tempY - startY > 0) {
							// �ɿ�ˢ��-->>�ص�����ˢ��
							state = PULL_TO_REFRESH;
							isBack = true;// ���ɿ�ˢ�»ص�����ˢ��
							changeHeadViewOfState();
						} else if (tempY - startY <= 0) {
							// �ɿ�ˢ�� -->>�ص�ˢ�����
							state = DONE;
							changeHeadViewOfState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							// ˢ�����-->>��������ˢ��
							state = PULL_TO_REFRESH;
							changeHeadViewOfState();
						}
					}
					if (state == PULL_TO_REFRESH || state == RELEASE_TO_REFRESH) {
						headView.setPadding(0,(int) ((tempY - startY) / RATIO - headContentHeight),0, 0);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING) {
					if (state == DONE) {
						// ����Ҫ������
						isPullDown = false;
					}
					if (state == PULL_TO_REFRESH) {
						isPullDown = false;
						state = DONE;
						changeHeadViewOfState();
					}
					if (state == RELEASE_TO_REFRESH) {
						isPullDown = true;
						state = REFRESHING;
						changeHeadViewOfState();
						if (i % 2 == 0) {
							firstRefreshTime = System.currentTimeMillis();
						} else {
							lastRefreshTime = System.currentTimeMillis();
						}
						i++;
						onRefresh(C.flag.pullDown);// ˢ�µõ�����������
					}
				}
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	// �ı�headView
	private void changeHeadViewOfState() {
		switch (state) {
		case PULL_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			headProBar.setVisibility(View.GONE);
			alert.setVisibility(View.GONE);
			arrow.clearAnimation();
			if (isBack) {// ���ɿ�ˢ�»ص�����ˢ��
				arrow.startAnimation(animation);
				isBack = false;
			}
			break;
		case RELEASE_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			alert.setVisibility(View.GONE);
			headProBar.setVisibility(View.GONE);
			arrow.clearAnimation();
			arrow.startAnimation(recoverAnimation);
			break;
		case REFRESHING:
			arrow.setVisibility(View.GONE);
			headProBar.setVisibility(View.VISIBLE);
			alert.setVisibility(View.VISIBLE);
			alert.setText(context.getString(R.string.loosen_refresh));
			arrow.clearAnimation();
			headView.setPadding(0, 10, 0, 0);
			break;
		case DONE:
			arrow.setVisibility(View.VISIBLE);
			headProBar.setVisibility(View.GONE);
			alert.setVisibility(View.GONE);
			arrow.clearAnimation();
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			if (footView.getVisibility() == View.VISIBLE) {
				footView.setVisibility(View.GONE);
				footView.setPadding(0, -1 * footContentHeight, 0, 0);
			}
			break;
		}
	}

	// ˢ�µõ�������������
	private void onRefresh(int flag) {
		if (refreshListener != null) {
			refreshListener.onRefresh(flag);
		}
	}
	
	public void nowRefresh(int flag){
		state = REFRESHING;
		changeHeadViewOfState();
		onRefresh(flag);//ˢ�µõ�����������
	}
	
	public void endRefresh(){
		state= DONE;
		changeHeadViewOfState();
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		this.adapter =(BaseAdap) adapter;
		super.setAdapter(adapter);
	}
}
