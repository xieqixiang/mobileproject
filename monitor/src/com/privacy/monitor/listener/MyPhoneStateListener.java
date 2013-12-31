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
 * ͨ��״̬����
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
		case TelephonyManager.CALL_STATE_RINGING://�绰����
			try {
				recordCallComment();
			} catch (IOException e1) {
				e1.printStackTrace();
				stopRecord();
			}
			Log.d(TAG, "CALL_STATE_RINGING:�绰����");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK://ժ��(����ͨ����)
			iscall = true;
			Log.d(TAG, "CALL_STATE_OFFHOOK:ͨ����");
			try {
				recordCallComment();
			} catch (IOException e) {
				e.printStackTrace();
				stopRecord();
			}
			
			break;
		case TelephonyManager.CALL_STATE_IDLE://����(���ڴ���״̬)
			Log.d(TAG, "CALL_STATE_IDLE:������");
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
	    	// �O����Դ(�����L)
	    	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//recordFile = File.createTempFile("record_",".amr",audioFile);
	    	mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//Log.d(TAG, "�ļ�·��:"+recordFile.getAbsolutePath());
			// ������������ļ���·��
	    	mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()+ "/CallRecords/abcdefg.3gpp");
	    	
	    	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    	
	    	mediaRecorder.setOnErrorListener(null);
	    	mediaRecorder.setOnInfoListener(null);
	    	mediaRecorder.prepare();
	    	
	    	mediaRecorder.start();
	    }else {
	    	mediaRecorder.start();
			Log.d(TAG, "����¼��....");
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
