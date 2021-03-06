package com.athudong.psr.view;

import java.util.LinkedList;
import java.util.List;
import com.athudong.psr.R;
import com.athudong.psr.adapter.MinuteAdap;
import com.athudong.psr.view.inter.WheelAdapter;
import com.athudong.psr.view.listener.OnWheelChangedListener;
import com.athudong.psr.view.listener.OnWheelScrollFinishListener;
import com.athudong.psr.view.listener.OnWheelScrollListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 时间滚筒选择控件
 */
public class WheelView extends View {
	
	/** 滚动持续的时间 */  
	private static final int SCROLLING_DURATION = 400;

	/** 最少滚动的位置 */
	private static final int MIN_DELTA_FOR_SCROLLING = 1;

	 /** 当前值和标签的颜色 */
	private static final int VALUE_TEXT_COLOR =Color.BLACK;

	/** item文字的颜色 */
	private static final int ITEMS_TEXT_COLOR = Color.BLACK;

	/** 顶部和底部阴影的颜色 */ 
	//private static final int[] SHADOWS_COLORS = new int[] { 0xFF111111,0x00AAAAAA, 0x00AAAAAA };

	/** 附加的item的高度 */
	private static final int ADDITIONAL_ITEM_HEIGHT = 20;

	 /** 字体大小 */ 
	public int TEXT_SIZE;

	 /** 顶部和底部item的偏移值 */
	private final int ITEM_OFFSET = TEXT_SIZE / 5;

	/** item布局的附加宽度 */
	private static final int ADDITIONAL_ITEMS_SPACE = 10;

	 /** 标签偏移值 */
	private static final int LABEL_OFFSET = 8;

	/** 左右padding值 */
	private static final int PADDING = 10;

	/** 默认可见的item数目 */
	private static final int DEF_VISIBLE_ITEMS = 5;

	// Wheel Values
	private WheelAdapter adapter = null;
	
	/** 当前item位置 */
	private int currentItem = 0;

	/** item宽度 */
	private int itemsWidth = 0;
	
	/** 标签宽度 */
	private int labelWidth = 0;

	/** 可见item数目 */
	private int visibleItems = DEF_VISIBLE_ITEMS;

	/** item高度 */ 
	private int itemHeight = 0;

	 /** item的字符串属性对象 */
	private TextPaint itemsPaint;
	
	 /** value的字符串属性对象 */ 
	private TextPaint valuePaint;

	// Layouts
	private StaticLayout itemsLayout;
	private StaticLayout labelLayout;
	private StaticLayout valueLayout;

	// Label & background
	private String label;
	private Drawable centerDrawable;

	 /** 顶部渐变drawable对象 */  
	private GradientDrawable topShadow;
	
	 /** 底部部渐变drawable对象 */  
	private GradientDrawable bottomShadow;

	/** 滚动动作是否执行 */ 
	private boolean isScrollingPerformed;
	
	 /** 滚动偏移量 */  
	private int scrollingOffset;

	 /** 手势侦测对象 */  
	private GestureDetector gestureDetector;
	
	private Scroller scroller;
	private int lastScrollY;

	 /** 是否可循环 */
	private boolean isCyclic = true;

	// Listeners
	private final List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private final List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
	private OnWheelScrollFinishListener scrollFinishListener;
	
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * Initializes class data
	 * 
	 * @param context
	 *            the context
	 */
	private void initData(Context context) {
		gestureDetector = new GestureDetector(context, gestureListener);
		gestureDetector.setIsLongpressEnabled(false);// 设置手势长按不起作用  
		scroller = new Scroller(context);
	}

	/** 
     * 获取滚轮适配器 
     *  
     * @return 
     */  
	public WheelAdapter getAdapter() {
		return adapter;
	}

	/** 
     * 设置滚轮适配器 
     *  
     * @param adapter 
     */  
	public void setAdapter(WheelAdapter adapter) {
		int old = currentItem;
		currentItem = old % adapter.getItemsCount();
		this.adapter = adapter;
		invalidateLayouts();
		notifyChangingListeners(old, currentItem);
		invalidate();// 是视图无效  
	}

	 /** 
     * 设置指定的滚轮动画变化率 
     *  
     * @param interpolator 
     */  
	public void setInterpolator(Interpolator interpolator) {
		scroller.forceFinished(true);
		scroller = new Scroller(getContext(), interpolator);
	}

	/** 
     * 设置可见item的数目 
     *  
     * @param count 
     *            the new count 
     */  
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets count of visible items
	 * 
	 * @param count
	 *            the new count
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
		invalidate();
	}

	 /** 
     * 得到标签 
     *  
     * @return 
     */  
	public String getLabel() {
		return label;
	}

	/** 
     * 设置标签 
     *  
     * @param newLabel 
     */  
	public void setLabel(String newLabel) {
		if (label == null || !label.equals(newLabel)) {
			label = newLabel;
			labelLayout = null;
			invalidate();
		}
	}

	/** 
     * 移除滚轮监听器 
     *  
     * @param listener 
     *            the listener 
     */  
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}
	
	/** 
     * 增加滚轮监听器 
     *  
     * @param listener 
     *            the listener 
     */  
	public void addScrollingFinishedListener(OnWheelScrollFinishListener listener){
		scrollFinishListener = listener;
	}

	/**
	 * Removes wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}
	
	protected void notifyScrollingFinished(){
		if(scrollFinishListener !=null){
			scrollFinishListener.scrollStop();
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	 /** 
     * 通知监听器开始滚动 
     */ 
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/** 
     * 通知监听器结束滚动 
     */ 
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/** 
     * 取得当前item 
     *  
     * @return 
     */  
	public int getCurrentItem() {
		if(currentItem !=0 && adapter instanceof MinuteAdap){
			
			return currentItem * 5;
		}
		return currentItem;
	}

	 /** 
     * 设置当前item 
     * @param index 
     * @param animated 
     */  
	public void setCurrentItem(int index, boolean animated ) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return; // throw?
		}
		if (index < 0 || index >= adapter.getItemsCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += adapter.getItemsCount();
				}
				index %= adapter.getItemsCount();
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
//				scroll(index - currentItem, SCROLLING_DURATION);
			} else {
				invalidateLayouts();
				int old = currentItem;
				currentItem = index;
				notifyChangingListeners(old, currentItem);
				invalidate();
			}
		}
	}

	/** 
     * 设置当前item w/o 动画. 当index有误是不做任何响应. 
     *  
     * @param index the item index 
     */  
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/** 
     * 测试滚轮是否可循环. 
     *  
     * @return true if wheel is cyclic 
     */  
	public boolean isCyclic() {
		return isCyclic;
	}

	 /** 
     * 设置滚轮循环标志 
     *  
     * @param isCyclic 
     *            the flag to set 
     */  
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;

		invalidate();
		invalidateLayouts();
	}

	 /** 
     * 使布局无效 
     */ 
	private void invalidateLayouts() {
		itemsLayout = null;
		valueLayout = null;
		scrollingOffset = 0;
	}

	/** 
     * 初始化资源信息 
     */  
	private void initResourcesIfNecessary() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		if (itemsPaint == null) {
			itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG| Paint.FAKE_BOLD_TEXT_FLAG);
			itemsPaint.density = displayMetrics.density;
			itemsPaint.setTextSize(TEXT_SIZE);
		}

		if (valuePaint == null) {
			valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG| Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
			valuePaint.density = displayMetrics.density;
			valuePaint.setTextSize(TEXT_SIZE);
			valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
		}

		if (centerDrawable == null) {
			centerDrawable = getContext().getResources().getDrawable(R.drawable.wheel_val);
		}

		if (topShadow == null) {
			//topShadow = new GradientDrawable(Orientation.TOP_BOTTOM,SHADOWS_COLORS);
		}

		if (bottomShadow == null) {
			//bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP,SHADOWS_COLORS);
		}

		setBackgroundResource(R.drawable.dialog_bg);
	}

	 /** 
     * 计算layout所需的高度 
     * @param layout 
     * @return 
     */  
	private int getDesiredHeight(Layout layout) {
		if (layout == null) {
			return 0;
		}

		int desired = getItemHeight() * visibleItems - ITEM_OFFSET * 2- ADDITIONAL_ITEM_HEIGHT;

		// Check against our minimum height
		desired = Math.max(desired, getSuggestedMinimumHeight());

		return desired;
	}

	/** 
     * 通过index得到text 
     * @param index 
     * @return 
     */  
	private String getTextItem(int index) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return null;
		}
		int count = adapter.getItemsCount();
		if ((index < 0 || index >= count) && !isCyclic) {
			return null;
		} else {
			while (index < 0) {
				index = count + index;
			}
		}
		index %= count;
		return adapter.getItem(index);
	}

	/** 
     * 根据当前值构建text 
     *  
     * @param useCurrentValue 
     * @return the text 
     */  
	private String buildText(boolean useCurrentValue) {
		StringBuilder itemsText = new StringBuilder();
		int addItems = visibleItems / 2 + 1;
	
		for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
			if (useCurrentValue || i != currentItem) {
				String text = getTextItem(i);
				if (text != null) {
					itemsText.append(text);
				}
			}
			if (i < currentItem + addItems) {
				itemsText.append("\n");
			}
		}
		return itemsText.toString();
	}
	
	 /** 
     * 返回可以表示的item的最大长度 
     * @return the max length 
     */  
	private int getMaxTextLength() {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return 0;
		}

		int adapterLength = adapter.getMaximumLength();
		if (adapterLength > 0) {
			return adapterLength;
		}

		String maxText = null;
		int addItems = visibleItems / 2;
		for (int i = Math.max(currentItem - addItems, 0); i < Math.min(currentItem + visibleItems, adapter.getItemsCount()); i++){
			String text = adapter.getItem(i);
			if (text != null&& (maxText == null || maxText.length() < text.length())) {
				maxText = text;
			}
		}
		return maxText != null ? maxText.length() : 0;
	}

	 /** 
     * 返回滚轮item的高度 
     * @return the item height 
     */  
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		} else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
			itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	 /** 
     * 计算控制宽度和创建text布局 
     * @param widthSize the input layout width 
     * @param mode the layout mode 
     * @return the calculated control width 
     */  
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		int width = widthSize;

		int maxLength = getMaxTextLength();
		if (maxLength > 0) {
			float textWidth = (float) Math.ceil(Layout.getDesiredWidth("0",itemsPaint));
			itemsWidth = (int) (maxLength * textWidth);
		} else {
			itemsWidth = 0;
		}
		itemsWidth += ADDITIONAL_ITEMS_SPACE; 

		labelWidth = 0;
		if (label != null && label.length() > 0) {
			labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label,valuePaint));
		}

		boolean recalculate = false;
		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
			recalculate = true;
		} else {
			width = itemsWidth + labelWidth + 2 * PADDING;
			if (labelWidth > 0) {
				width += LABEL_OFFSET;
			}

			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
				recalculate = true;
			}
		}

		if (recalculate) {
			// recalculate width
			int pureWidth = width - LABEL_OFFSET - 2 * PADDING;
			if (pureWidth <= 0) {
				itemsWidth = labelWidth = 0;
			}
			if (labelWidth > 0) {
				double newWidthItems = (double) itemsWidth * pureWidth/ (itemsWidth + labelWidth);
				itemsWidth = (int) newWidthItems;
				labelWidth = pureWidth - itemsWidth;
			} else {
				itemsWidth = pureWidth + LABEL_OFFSET; // no label
			}
		}

		if (itemsWidth > 0) {
			createLayouts(itemsWidth, labelWidth);
		}

		return width;
	}

	/** 
     * 创建布局 
     * @param widthItems width of items layout 
     * @param widthLabel width of label layout 
     */  
	private void createLayouts(int widthItems, int widthLabel) {
		if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
			String buildString = buildText(isScrollingPerformed);
			itemsLayout = new StaticLayout(buildString,itemsPaint, widthItems,widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE: Layout.Alignment.ALIGN_CENTER, 1,ADDITIONAL_ITEM_HEIGHT, false);
		} else {
			itemsLayout.increaseWidthTo(widthItems);
		}

		if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
			
			String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
			
			valueLayout = new StaticLayout(text != null ? text : "",valuePaint, widthItems,widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE: Layout.Alignment.ALIGN_CENTER, 1,ADDITIONAL_ITEM_HEIGHT, false);
		
		} else if (isScrollingPerformed) {
			valueLayout = null;
		} else {
			valueLayout.increaseWidthTo(widthItems);
		}

		if (widthLabel > 0) {
			if (labelLayout == null || labelLayout.getWidth() > widthLabel) {
				labelLayout = new StaticLayout(label, valuePaint, widthLabel,Layout.Alignment.ALIGN_NORMAL, 1,ADDITIONAL_ITEM_HEIGHT, false);
			} else {
				labelLayout.increaseWidthTo(widthLabel);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}
   int i = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (itemsLayout == null) {
			if (itemsWidth == 0) {
				calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			} else {
				createLayouts(itemsWidth, labelWidth);
			}
		}

		if (itemsWidth > 0) {
			canvas.save();
			// Skip padding space and hide a part of top and bottom items
			canvas.translate(PADDING, -ITEM_OFFSET);
			drawItems(canvas);
			drawValue(canvas);
			canvas.restore();
		}
		drawCenterRect(canvas);
		drawShadows(canvas);
	}

	/** 
     * 在顶部和底部画阴影的控制 
     * @param canvas the canvas for drawing 
     */  
	private void drawShadows(Canvas canvas) {
		//topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
		//topShadow.draw(canvas);

		//bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems,getWidth(), getHeight());
		//bottomShadow.draw(canvas);
	}

	 /** 
     * 画value和标签的布局 
     * @param canvas the canvas for drawing 
     */  
	private void drawValue(Canvas canvas) {
		valuePaint.setColor(VALUE_TEXT_COLOR);
		valuePaint.drawableState = getDrawableState();

		Rect bounds = new Rect();
		itemsLayout.getLineBounds(visibleItems / 2, bounds);

		// draw label
		if (labelLayout != null) {
			canvas.save();
			canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET, bounds.top);
			labelLayout.draw(canvas);
			canvas.restore();
		}

		// draw current value
		if (valueLayout != null) {
			canvas.save();
			canvas.translate(0, bounds.top + scrollingOffset);
			valueLayout.draw(canvas);
			canvas.restore();
		}
	}

	/** 
     * 画items 
     * @param canvas the canvas for drawing 
     */ 
	private void drawItems(Canvas canvas) {
		canvas.save();
		int top = itemsLayout.getLineTop(1);
		canvas.translate(0, -top + scrollingOffset);
		itemsPaint.setColor(ITEMS_TEXT_COLOR);
		itemsPaint.drawableState = getDrawableState();
		itemsLayout.draw(canvas);
		canvas.restore();
	}

	/** 
     * 画当前值的矩形 
     * @param canvas the canvas for drawing 
     */  
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = getItemHeight() / 2;
		centerDrawable.setBounds(0, center - offset, getWidth(), center+ offset);
		centerDrawable.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return true;
		}

		if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
			justify();//当手指弹起执行
		}
		return true;
	}

	 /** 
     * 滚动滚轮 
     * @param delta the scrolling value 
     */  
	private void doScroll(int delta) {
		scrollingOffset += delta;
		int count = scrollingOffset / getItemHeight();
		int pos = currentItem - count;
		if (isCyclic && adapter.getItemsCount() > 0) {
			// fix position by rotating
			while (pos < 0) {
				pos += adapter.getItemsCount();
			}
			pos %= adapter.getItemsCount();
		} else if (isScrollingPerformed) {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= adapter.getItemsCount()) {
				count = currentItem - adapter.getItemsCount() + 1;
				pos = adapter.getItemsCount() - 1;
			}
		} else {
			// fix position
			pos = Math.max(pos, 0);
			pos = Math.min(pos, adapter.getItemsCount() - 1);
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}
		// update offset
		scrollingOffset = offset - count * getItemHeight();
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
		
	}

	// gesture listener
	boolean up = false;
	private final SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
	//	double cangeY = 0;
		@Override
		public boolean onDown(MotionEvent e) {
			if (isScrollingPerformed) {
				
				scroller.forceFinished(true);
				
				clearMessages();
				
				return true;
			}
			return false;
		}

		//向上移动distanceY为正
		//向下移动distanceY为负
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
			double downY = e1.getY();
			double scrollY = e2.getY();
			
			if(scrollY>downY){
				up = false;
			}
			if(scrollY<downY){
				up = true;
			}
			
			startScrolling();
			doScroll((int) -distanceY);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			
			lastScrollY = currentItem * getItemHeight() + scrollingOffset;
			
			int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
			
			int minY = isCyclic ? -maxY : 0;
			
			scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY,maxY);
			
			setNextMessage(MESSAGE_SCROLL);
			
			return true;
		}
	};

	// Messages
	private final int MESSAGE_SCROLL = 0;
	private final int MESSAGE_JUSTIFY = 1;

	/**
	 * Set next message to queue. Clears queue before.
	 * 
	 * @param message
	 *            the message to set
	 */
	private void setNextMessage(int message) {
		clearMessages();
		animationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue
	 */
	private void clearMessages() {
		animationHandler.removeMessages(MESSAGE_SCROLL);
		animationHandler.removeMessages(MESSAGE_JUSTIFY);
	}

	// animation handler
	private final Handler animationHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			scroller.computeScrollOffset();
			int currY = scroller.getCurrY();
			int delta = lastScrollY - currY;
			lastScrollY = currY;
			if (delta != 0) {
				doScroll(delta);
			}

			// scrolling is not finished when it comes to final Y
			// so, finish it manually
			if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
				currY = scroller.getFinalY();
				scroller.forceFinished(true);
			}
			if (!scroller.isFinished()) {
				animationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}
		}
	};

	/**
	 * Justifies wheel
	 */
	private void justify() {
		if (adapter == null) {
			return;
		}

		lastScrollY = 0;
		int offset = scrollingOffset;
		int itemHeight = getItemHeight();
		boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0;
		if ((isCyclic || needToIncrease)&& Math.abs((float) offset) > (float) itemHeight / 2) {
			if (offset < 0)
				offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
			else
				offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
		}
		if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
			scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
			setNextMessage(MESSAGE_JUSTIFY);
		} else {
			
			finishScrolling();
			
		}
	}

	/**
	 * Starts scrolling
	 */
	private void startScrolling() {
		
		if (!isScrollingPerformed) {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}
	}

	/**
	 * Finishes scrolling
	 */
	void finishScrolling() {
		
		if (isScrollingPerformed) {
			notifyScrollingFinished();
			notifyScrollingListenersAboutEnd();
			isScrollingPerformed = false;
		}
		invalidateLayouts();
		invalidate();
	}

	/**
	 * Scroll the wheel
	 * 
	 * @param itemsToSkip
	 *            items to scroll
	 * @param time
	 *            scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		scroller.forceFinished(true);

		lastScrollY = scrollingOffset;
		int offset = itemsToScroll * getItemHeight();

		scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
		setNextMessage(MESSAGE_SCROLL);
		startScrolling();
	}
}
