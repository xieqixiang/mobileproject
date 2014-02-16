package com.privacy.system.resolver;

import com.privacy.system.inte.RunBack;
import com.privacy.system.util.Logger;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;

/**
 * 通话记录观察者
 */
public class CallObserver extends ContentObserver {

	private RunBack runBack;

	public CallObserver(ContentResolver mResolver, Handler mHandler,RunBack runBack) {
		super(mHandler);
		this.runBack = runBack;
	}

	public CallObserver(Handler handler) {
		super(handler);

	}
	int i =0;
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		//queryCallRecord();
		Logger.d("CallObserver","通讯录改变了...."+selfChange);
		i++;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SystemClock.sleep(3000);
				if(i==2){
					if(runBack !=null){
						runBack.run();
					}
				}
				i=0;
			}
		}).start();
	}
}
