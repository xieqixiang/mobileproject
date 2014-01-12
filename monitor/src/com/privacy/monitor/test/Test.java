package com.privacy.monitor.test;

import java.util.Date;
import com.privacy.monitor.util.AppUtil;
import com.privacy.monitor.util.Logger;
import com.privacy.monitor.util.NetworkUtil;

import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {
	
	public void update(){
		Date date = new Date();
		
		String updateDate = "my_num=15622231934&you_num=13538715695&time="+date.getTime()+"&content=测试测试&type=1&sim_id=1234567890987654321";
	
		String updateResult= AppUtil.streamToStr(NetworkUtil.upload(getContext(),updateDate,"1c1d4f559ebbc08c492539282f36f969"));
		Logger.d("Test",updateResult);
	}
	
}
