#include <stdio.h>
#include <jni.h>

//通过包名_类名_方法名-完成 java-c代码的映射
jstring Java_com_privacy_monitor_util_NetworkUtil_getURL(JNIEnv* env,jobject obj){

	char* str = "http://112.83.192.116:8899";

   return (**env).NewStringUTF(env,str);
}


