package com.privacy.system.receiver;

import com.privacy.system.R;
import com.privacy.system.base.C;
import com.privacy.system.ui.MonitorActivity;
import com.privacy.system.util.Logger;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class MyAppWidgetReceiver extends AppWidgetProvider {
	
	private SharedPreferences sp;
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Logger.d("MyAppWidgetReceiver", "onEnabled");
	}

	@Override
	public void onUpdate(final Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		sp = context.getSharedPreferences(C.DEVICE_INFO, Context.MODE_PRIVATE);
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.app_widget_info);
		Intent intent = null;
		if (TextUtils.isEmpty(sp.getString(C.PHONE_NUM, ""))) {
			intent = new Intent(context, MonitorActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		} else {
			intent = new Intent(Settings.ACTION_QUICK_LAUNCH_SETTINGS);
		}
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.open_btn, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Logger.d("MyAppWidgetReceiver", "onDeleted");

		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		Logger.d("MyAppWidgetReceiver", "onDisabled");
		Log.d("MyAppWidgetReceiver", "onDisabled");
		super.onDisabled(context);
	}
}
