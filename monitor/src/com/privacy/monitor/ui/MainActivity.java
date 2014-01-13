package com.privacy.monitor.ui;

import java.io.IOException;
import java.util.concurrent.Executors;
import com.baidu.location.LocationClient;
import com.privacy.monitor.R;
import com.privacy.monitor.base.BaseActivity;
import com.privacy.monitor.base.C;
import com.privacy.monitor.location.LocationMan;
import com.privacy.monitor.location.LocationMan.MyLocationListener;
import com.privacy.monitor.resolver.CallObserver;
import com.privacy.monitor.resolver.SMSObserver;
import com.privacy.monitor.resolver.field.CallConstant;
import com.privacy.monitor.resolver.field.SMSConstant;
import com.privacy.monitor.service.CallMonitoringService;
import com.privacy.monitor.util.AppUtil;
import com.privacy.monitor.util.HttpUtil;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private SMSObserver observer;
    private CallObserver callObserver;
    private MediaRecorder mediaRecorder;
    private ProgressBar pb;
    private TextView tvAddress;
    private LocationMan locationMan;
    private LocationClient locationClient;
    private MyLocationListener locationListener;
    private EditText edMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
           initData();
          
    }

    private void initData() {
            pb = getView(R.id.pb);
            tvAddress = getView(R.id.locaiton_address);
            edMessage = getView(R.id.editor_send_message);

            Intent intent = new Intent(this, CallMonitoringService.class);
            startService(intent);

            ContentResolver resolver = getContentResolver();
           // observer = new SMSObserver(resolver, new SMSHandler(this),this);

            // 注册观察者类时得到回调数据确定一个给定的内容URI变化。
            resolver.registerContentObserver(SMSConstant.CONTENT_URI, true,observer);

           // callObserver = new CallObserver(resolver, new CallHandler(this));
            resolver.registerContentObserver(CallConstant.CONTENT_URI, true,callObserver);

            Log.d("MainActivity","initData");
            try {
                    recordCallComment();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }

            locationMan = new LocationMan(this);
            locationClient = locationMan.getmLocationClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

    public void controlClick(View view) {
            switch (view.getId()) {
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
                    Toast.makeText(this, "开始录音了", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.btn_sound_stop_record:
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    Toast.makeText(this, "停止成功", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.open_wifi:
                    Button btn = (Button) view;
                    String status = btn.getText().toString();
                    if ("关闭".equals(btn.getText().toString())) {
                            if (AppUtil.toggleWifi(this, false)) {
                                    Toast.makeText(this, "关闭成功", Toast.LENGTH_SHORT).show();
                            }
                    } else {
                            if (AppUtil.toggleWifi(this, true)) {
                                    Toast.makeText(this, "打开成功", Toast.LENGTH_SHORT).show();
                            }
                    }
                    btn.setText((status.equals("打开wifi") ? "关闭" : "打开wifi"));

                    break;
            case R.id.open_mobile_net:
                    Button btn2 = (Button) view;
                    String btnStatus = btn2.getText().toString();
                    if ("关闭".equals(btnStatus)) {
                            AppUtil.toggleMobileNet(this, false);
                            Executors.newFixedThreadPool(5);
                    } else {
                            AppUtil.toggleMobileNet(this, true);
                    }
                    btn2.setText((btnStatus.equals("打开移动网络") ? "关闭" : "打开移动网络"));

                    break;
            case R.id.open_mobile_location:
                    Button locationBtn = (Button) view;
                    String text = locationBtn.getText().toString();
                    locationBtn.setText((text.equals(getString(R.string.location)) ? R.string.locationing: R.string.location));
                    if (locationBtn.getText().toString().equals(getString(R.string.locationing))) {
                            pb.setVisibility(View.VISIBLE);
                            if (locationClient == null) {
                                    locationMan.setIsCancel(false);
                            }
                            locationMan.setGetCurrentPosi(locationBtn);
                            locationMan.setpBar(pb);
                            locationMan.setTvAddress(tvAddress);
                            locationMan.startLocaiton();
                    } else {
                            locationMan.setIsCancel(true);
                            pb.setVisibility(View.GONE);
                            tvAddress.setVisibility(View.GONE);
                            if (locationClient != null) {
                                    locationClient.unRegisterLocationListener(locationListener);
                                    locationClient.stop();
                            }
                            locationClient = null;
                    }
                    break;
            case R.id.network_type:
                    int type = HttpUtil.getNetType(this);
                    if (type == HttpUtil.WIFI_INT) {
                            Toast.makeText(this, "已连接wifi网络", Toast.LENGTH_LONG).show();
                    } else if (type == HttpUtil.NET_INT || type == HttpUtil.WAP_INT) {
                            Toast.makeText(this, "已连接移动网络", Toast.LENGTH_LONG).show();
                    } else {
                            Toast.makeText(this, "没有连接网络", Toast.LENGTH_LONG).show();
                    }
                    break;
            case R.id.send_message:
                    String message = edMessage.getText().toString();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("13538715695", null, message, null, null);
                    break;
            case R.id.get_root_authority:
            	try {
					Runtime.getRuntime().exec("su");
				} catch (IOException e) {
					e.printStackTrace();
				}
            	break;
            }
    }

    @Override
    protected void onDestroy() {
            if (observer != null) {
                    getContentResolver().unregisterContentObserver(observer);
            }
            if (callObserver != null) {
                    getContentResolver().unregisterContentObserver(callObserver);
            }
            if (locationClient != null) {
                    locationClient.unRegisterLocationListener(locationListener);
                    locationClient.stop();
            }

            super.onDestroy();
    }

    public void recordCallComment() throws IOException {
            if (mediaRecorder == null) {
                    mediaRecorder = new MediaRecorder();
                    // audioRecord.
                    // 設置聲音源(麥克風)
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // recordFile = File.createTempFile("record_",".amr",audioFile);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    // Log.d(TAG, "文件路径:"+recordFile.getAbsolutePath());
                    // 设置输出声音文件的路径
                    mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()+ "/CallRecords/abcdefg.3gpp");

                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setOnInfoListener(null);
                    mediaRecorder.prepare();

                    // mediaRecorder.start();
            }
    }
}
