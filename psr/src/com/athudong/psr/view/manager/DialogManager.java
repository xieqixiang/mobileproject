package com.athudong.psr.view.manager;

import com.athudong.psr.R;
import com.athudong.psr.adapter.DialogItemAdap;
import com.athudong.psr.view.listener.OnAlertSelectId;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Dialog管理类
 */
public class DialogManager {

	private static Dialog errorDialog;
	private static Dialog progressDialog;
	private static Dialog optionDialog;
	private static Dialog alertDialog;
	private static View view = null;
	private static Toast toast = null;
	private static Animation animation;
	private static PopupWindow promptPopupWindow;
	private static ProgressBar pb;
	private static TextView tvAler;

	/**
	 * @param context上下文
	 * @param toasttext需要显示的文本信息
	 * @return 自定义toast
	 */
	public static Toast toastMakeText(Context context, String toasttext,
			int duration) {
		toast = new Toast(context);
		toast.setDuration(duration);
		view = View.inflate(context, R.layout.al_my_toast, null);
		toast.setView(view);
		TextView text = (TextView) view.findViewById(R.id.ai_toast_text);
		text.setText(toasttext);

		return toast;
	}

	/**
	 * 错误提示
	 */
	public static void showErrorDialog(Context context, String message,
			OnClickListener listener) {
		errorDialog = new Dialog(context, R.style.mDialog);
		errorDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		View view = View.inflate(context, R.layout.al_error_dialog, null);
		TextView errorTitle = (TextView) view.findViewById(R.id.ai_error_title);
		errorTitle.setText("");
		TextView errorMessage = (TextView) view
				.findViewById(R.id.ai_fail_message);
		errorMessage.setText(message);
		errorDialog.setContentView(view);
		Button sureBtn = (Button) view.findViewById(R.id.ai_suer);
		sureBtn.setOnClickListener(listener);
		errorDialog.show();
	}

	/**
	 * 提示dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param listener
	 */
	public static void showAlertDialog(Context context, String title,
			String message, OnClickListener listener) {
		alertDialog = new Dialog(context, R.style.mDialog);
		alertDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		View view = View.inflate(context, R.layout.alert_dialog, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.ai_alert_title);
		if (!TextUtils.isEmpty(title)) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title);
		} else {
			tvTitle.setVisibility(View.GONE);
		}

		TextView tvMessage = (TextView) view
				.findViewById(R.id.ai_alert_message);
		tvMessage.setText(message);
		alertDialog.setContentView(view);
		Button cancel = (Button) view.findViewById(R.id.alert_negative);
		cancel.setOnClickListener(listener);
		Button sure = (Button) view.findViewById(R.id.alert_positive);
		sure.setOnClickListener(listener);
		alertDialog.show();
	}

	public static void showOptionDialog(Context context,
			OnClickListener listener) {
		optionDialog = new Dialog(context, R.style.mDialog);
		optionDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		View view = View.inflate(context, R.layout.al_option_dialog, null);
		TextView one = (TextView) view.findViewById(R.id.ai_od_one);
		one.setOnClickListener(listener);
		TextView two = (TextView) view.findViewById(R.id.ai_od_two);
		two.setOnClickListener(listener);
		optionDialog.setContentView(view);
		optionDialog.show();
	}

	/**
	 * @param context
	 *            上下文
	 * @param text
	 *            dialog显示的提示信息
	 * @return dialog
	 */
	public static Dialog showProgressDialog(Context context, String text) {

		if (progressDialog != null && progressDialog.isShowing()) {
			tvAler.setText(text);
		} else {
			View view = View.inflate(context, R.layout.progress_dialog, null);
			tvAler = (TextView) view.findViewById(R.id.ai_pd_text);
			progressDialog = new Dialog(context, R.style.mDialog);
			Window window = progressDialog.getWindow();
			window.setBackgroundDrawableResource(android.R.color.transparent);

			WindowManager.LayoutParams lp = window.getAttributes();
			lp.alpha = 0.7f;
			window.setAttributes(lp);
			pb = (ProgressBar) view.findViewById(R.id.ai_pd_pb);
			tvAler.setText(text);
			pb.setVisibility(View.VISIBLE);
			progressDialog.setContentView(view);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		return progressDialog;
	}

	/** 底部弹出的Dialog */
	public static void showButtomDialog(final Activity activity,final String[] items, final OnAlertSelectId alertSelectId) {

		DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		float density = dm.density;
		int width= dm.widthPixels;
		
		final Dialog buttomDialog = new Dialog(activity, R.style.mDialog);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.buttom_dialog, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width-(30*density)),(int) (230*density));
		layout.setLayoutParams(params);
	
		Button btnButtom = (Button) layout.findViewById(R.id.ai_bd_buttom);
		btnButtom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttomDialog.dismiss();
			}
		});
		final ListView lv = (ListView) layout.findViewById(R.id.ai_bd_listview);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams((int) (width-(10*density)),(int) (210*density));
		lv.setPadding((int)(30*density),0,(int)(30*density),0);
		params2.topMargin = 20;
		lv.setLayoutParams(params2);
		DialogItemAdap adapter = new DialogItemAdap(activity);
		adapter.setItems(items);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				alertSelectId.onClick(position);
				buttomDialog.dismiss();
				lv.requestFocus();
			}
		});
		Window window = buttomDialog.getWindow();
		window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		window.setBackgroundDrawableResource(android.R.color.transparent);
		window.setWindowAnimations(R.style.dialog_style);
		buttomDialog.setContentView(layout);
		buttomDialog.show();
	}

	public static boolean progressIsShow() {
		if (progressDialog != null && progressDialog.isShowing()) {
			return true;
		}
		return false;
	}

	/**
	 * 关闭提示popupwindow
	 */
	public static void closePromptPopupWindow() {
		if (promptPopupWindow != null && promptPopupWindow.isShowing()) {
			promptPopupWindow.dismiss();
			promptPopupWindow = null;
		}
	}

	/**
	 * 关闭Progressdialog
	 */
	public static void progressDialogdimiss() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			pb.setVisibility(View.GONE);
			progressDialog = null;
		}
	}

	/**
	 * 关闭errorDialog
	 */
	public static void errorDialogdimiss() {
		if (errorDialog != null && errorDialog.isShowing()) {
			errorDialog.dismiss();
			errorDialog = null;
		}
	}

	/**
	 * 旋转动画
	 */
	public static Animation getAnimation(Context context) {

		animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
		return animation;
	}

	public static void stopAnimation() {
		animation.cancel();
		animation = null;
	}

	public static void closeOptionDialog() {
		if (optionDialog != null && optionDialog.isShowing()) {
			optionDialog.cancel();
		}
	}

	public static void closeAlertDialog() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
			alertDialog = null;
		}
	}
}
