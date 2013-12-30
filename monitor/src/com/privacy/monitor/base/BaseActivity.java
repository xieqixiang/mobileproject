package com.privacy.monitor.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T getView(int id) {
		return (T) findViewById(id);
	}
	
	protected void overLayout(Bundle bundle ,Class<?> clazz){
		Intent intent = new Intent(this,clazz);
		intent.putExtra(C.BUNDLE_NAME,bundle);
		startActivity(intent);
	}
}
