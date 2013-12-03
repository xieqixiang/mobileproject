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
 * 下拉刷新
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

	private static final int PULL_TO_REFRESH = 0;// 下拉状态
	private static final int RELEASE_TO_REFRESH = 1;// 松开状态
	private static final int REFRESHING = 2;// 滚动状态
	private static final int DONE = 3;// 加载完数据
	private static final int RATIO = 3;// 实际距离于界面上距离之间的比例值

	private int state; // 当前下拉刷新控件的状态
	private int i = 0;
	private OnRefreshListener refreshListener;// 刷新监听器
	// isRefreshAble 记录是否能够刷新 , isRecoder记录startY,起始位置，在整个滑动中，只记录一次,
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

		// 设置headView与界面上边距的距离
		headView.setPadding(0, -1 * headContentHeight, 0, 0);

		// 设置footView,与界面上边距的距离
		footView.setPadding(0, -1 * footContentHeight, 0, 0);

		headView.invalidate();// headView重绘

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

	// 测量HeadView宽高
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
			childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY);// 适合、匹配
		} else {
			childMeasureHeight = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);// 未指定
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
					&& headProBar.getVisibility() == View.GONE) {// 向上拉获取更多数据
				if (haveMoreDate) {
					haveMoreDate = false;
					footView.setPadding(0, (getHeight() / footContentHeight)
							- RATIO, 0, 0);
					setSelection((getAdapter().getCount() - 1));
					footProBar.setVisibility(View.VISIBLE);
					onRefresh(C.flag.pullUp);// 刷新得到服务器数据
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

	/** 下拉刷新监听器 */
	public interface OnRefreshListener {
		abstract void onRefresh(int flag);
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		refreshListener = listener;
		isRefreshAble = true;
	}

	// 数据刷新完成后执行方法
	// 下拉刷新模式的改变时间的更新
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
							// 下拉刷新-->>松开刷新
							state = RELEASE_TO_REFRESH;
							changeHeadViewOfState();
						} else if (tempY - startY <= 0) {
							// 下拉刷新-->>回到刷新完成
							state = DONE;
							changeHeadViewOfState();
						}
					}

					if (state == RELEASE_TO_REFRESH) {
						if ((tempY - startY) / RATIO < headContentHeight
								&& tempY - startY > 0) {
							// 松开刷新-->>回到下拉刷新
							state = PULL_TO_REFRESH;
							isBack = true;// 从松开刷新回到下拉刷新
							changeHeadViewOfState();
						} else if (tempY - startY <= 0) {
							// 松开刷新 -->>回到刷新完成
							state = DONE;
							changeHeadViewOfState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							// 刷新完成-->>进入下拉刷新
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
						// 不需要做处理
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
						onRefresh(C.flag.pullDown);// 刷新得到服务器数据
					}
				}
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	// 改变headView
	private void changeHeadViewOfState() {
		switch (state) {
		case PULL_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			headProBar.setVisibility(View.GONE);
			alert.setVisibility(View.GONE);
			arrow.clearAnimation();
			if (isBack) {// 从松开刷新回到下拉刷新
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

	// 刷新得到服务器的数据
	private void onRefresh(int flag) {
		if (refreshListener != null) {
			refreshListener.onRefresh(flag);
		}
	}
	
	public void nowRefresh(int flag){
		state = REFRESHING;
		changeHeadViewOfState();
		onRefresh(flag);//刷新得到服务器数据
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
