package com.privacy.monitor.listener;

import java.io.File;
import java.io.IOException;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 通话状态监听
 */
public class MyPhoneStateListener extends PhoneStateListener {
	
	
	private static final String TAG =MyPhoneStateListener.class.getSimpleName();

	File audioFile;
	File recordFile;
	
	MediaRecorder mediaRecorder ;
	
	Context context;
	
	boolean iscall = false;
	
	public MyPhoneStateListener(Context context,File file){
		this.context = context;
		iscall = false;
		this.audioFile=file;
	}
	
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		
		switch(state){
		case TelephonyManager.CALL_STATE_RINGING://电话铃响
			try {
				recordCallComment();
			} catch (IOException e1) {
				e1.printStackTrace();
				stopRecord();
			}
			Log.d(TAG, "CALL_STATE_RINGING:电话铃响");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK://摘机(处于通话中)
			iscall = true;
			Log.d(TAG, "CALL_STATE_OFFHOOK:通话中");
			try {
				recordCallComment();
			} catch (IOException e) {
				e.printStackTrace();
				stopRecord();
			}
			
			break;
		case TelephonyManager.CALL_STATE_IDLE://空闲(处于待机状态)
			Log.d(TAG, "CALL_STATE_IDLE:空闲中");
			if(iscall){
				stopRecord();
				iscall = false;
			}
			break;
		}
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
	    	
	    	mediaRecorder.start();
	    }else {
	    	mediaRecorder.start();
			Log.d(TAG, "正在录音....");
		}
		
	}
	
	public void stopRecord(){
		if(mediaRecorder !=null ){
			if(iscall){
				//mediaRecorder.release();
				mediaRecorder.stop();
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder = null;
			}
		}
	}
}
