package com.privacy.monitor.ui;

import java.io.IOException;
import com.privacy.monitor.R;
import com.privacy.monitor.base.BaseActivity;
import com.privacy.monitor.base.C;
import com.privacy.monitor.resolver.CallObserver;
import com.privacy.monitor.resolver.SMSObserver;
import com.privacy.monitor.resolver.field.CallConstant;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.resolver.handler.CallHandler;
import com.privacy.monitor.resolver.handler.SMSHandler;
import com.privacy.monitor.service.CallRecordService;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private SMSObserver observer;
	private CallObserver callObserver;
	private MediaRecorder mediaRecorder ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		
	}
	
	private void initData(){
		
		Intent intent = new Intent(this,CallRecordService.class);
		startService(intent);
		
		ContentResolver resolver = getContentResolver();
		observer = new SMSObserver(resolver, new SMSHandler(this));
		
		//注册观察者类时得到回调数据确定一个给定的内容URI变化。  
		resolver.registerContentObserver(SMSConstant.CONTENT_URI, true, observer);   
		
		callObserver = new CallObserver(resolver, new CallHandler(this));
		resolver.registerContentObserver(CallConstant.CONTENT_URI,true, callObserver);
		
		try {
			recordCallComment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void controlClick(View view){
		switch(view.getId()){
		case R.id.btn_call_record:
			Bundle bundle = new Bundle();
			bundle.putInt(C.FLAG, C.CALL_RECORD);
			overLayout(bundle, MonitorActivity.class);
			break;
		case R.id.btn_message_record:
			Bundle bundle2 = new Bundle();
			bundle2.putInt(C.FLAG, C.MESSAGE_RECORD);
			overLayout(bundle2, MonitorActivity.class);
			break;
		case R.id.btn_sound_record:
			mediaRecorder.start();
			Toast.makeText(this,"开始录音了",Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_sound_stop_record:
			mediaRecorder.stop();
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		if(observer !=null){
			getContentResolver().unregisterContentObserver(observer);
		}
		if(callObserver !=null){
			getContentResolver().unregisterContentObserver(callObserver);
		}
		
		super.onDestroy();
	}
	
	public void recordCallComment() throws IOException{
	    if(mediaRecorder == null){
	    	mediaRecorder = new MediaRecorder();
	    	//audioRecord.
	    	// 設置聲音源(麥克風)
	    	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//recordFile = File.createTempFile("record_",".amr",audioFile);
	    	mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//Log.d(TAG, "文件路径:"+recordFile.getAbsolutePath());
			// 设置输出声音文件的路径
	    	mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()+ "/CallRecords/abcdefg.3gpp");
	    	
	    	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    	
	    	mediaRecorder.setOnErrorListener(null);
	    	mediaRecorder.setOnInfoListener(null);
	    	mediaRecorder.prepare();
	    	
	    	//mediaRecorder.start();
	    }
		
	}

}
