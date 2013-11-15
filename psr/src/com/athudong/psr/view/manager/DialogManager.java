package com.athudong.psr.view.manager;

import com.athudong.psr.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

	/**
	 * @param context上下文
	 * @param toasttext需要显示的文本信息
	 * @return 自定义toast
	 */
	public static Toast toastMakeText(Context context, String toasttext,int duration) {
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
	public static void showErrorDialog(Context context,String message, OnClickListener listener) {
		errorDialog = new Dialog(context, R.style.mDialog);
		errorDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		View view = View.inflate(context, R.layout.al_error_dialog, null);
		TextView errorTitle = (TextView) view.findViewById(R.id.ai_error_title);
		errorTitle.setText("");
		TextView errorMessage = (TextView) view.findViewById(R.id.ai_fail_message);
		errorMessage.setText(message);
		errorDialog.setContentView(view);
		Button sureBtn = (Button) view.findViewById(R.id.ai_suer);
		sureBtn.setOnClickListener(listener);
		errorDialog.show();
	}

	/**
	 * 提示dialog
	 * @param context
	 * @param title
	 * @param message
	 * @param listener
	 */
	public static void showAlertDialog(Context context, String title,String message, OnClickListener listener) {
		alertDialog = new Dialog(context, R.style.mDialog);
		alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		View view = View.inflate(context, R.layout.al_alert_dialog, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.ai_alert_title);

		tvTitle.setText(title);
		TextView tvMessage = (TextView) view.findViewById(R.id.ai_alert_message);
		tvMessage.setText(message);
		alertDialog.setContentView(view);
		Button cancel = (Button) view.findViewById(R.id.ai_alert_negative);
		cancel.setOnClickListener(listener);
		Button sure = (Button) view.findViewById(R.id.ai_alert_positive);
		sure.setOnClickListener(listener);
		alertDialog.show();
	}

	public static void showOptionDialog(Context context,OnClickListener listener) {
		optionDialog = new Dialog(context, R.style.mDialog);
		optionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		View view = View.inflate(context, R.layout.al_option_dialog, null);
		TextView one = (TextView) view.findViewById(R.id.ai_od_one);
		one.setOnClickListener(listener);
		TextView two = (TextView) view.findViewById(R.id.ai_od_two);
		two.setOnClickListener(listener);
		optionDialog.setContentView(view);
		optionDialog.show();
	}

	/**
	 * @param context 上下文
	 * @param text dialog显示的提示信息
	 * @return dialog
	 */
	public static Dialog showProgressDialog(Context context, String text) {
		progressDialog = new Dialog(context, R.style.mDialog);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		Window window = progressDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.5f;
		window.setAttributes(lp);
		View view = View.inflate(context, R.layout.al_progress_dialog, null);
	    pb = (ProgressBar) view.findViewById(R.id.ai_pd_pb);
		TextView textView = (TextView) view.findViewById(R.id.ai_pd_text);
		textView.setText(text);
		pb.setVisibility(View.VISIBLE);
		progressDialog.setContentView(view);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		return progressDialog;
	}
	
	public static boolean progressIsShow(){
		if(progressDialog !=null && progressDialog.isShowing()){
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
			pb.setVisibility(View.GONE);
			progressDialog.dismiss();
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

		animation = AnimationUtils.loadAnimation(context, R.anim.ya_rotate);
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
