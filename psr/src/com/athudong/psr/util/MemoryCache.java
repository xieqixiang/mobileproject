package com.athudong.psr.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.graphics.Bitmap;

/**
 * bitmap�������
 */
public class MemoryCache {
	
	private static final String TAG = "MemoryCache";
	//���뻺��ʱ�Ǹ�ͬ������
	//linkedHashMap���췽�������һ������true�������map���Ԫ�ؽ��������ʹ�ô������ٵ������У���LRU
	//�����ĺô������Ҫ�������е�Ԫ���滻�����ȱ������������ʹ�õ�Ԫ�����滻�����Ч��
	private Map<String,SoftReference<Bitmap>> cache = Collections.synchronizedMap(new LinkedHashMap<String,SoftReference<Bitmap>>(10,1.5f,true));
	private Map<String,SoftReference<Bitmap>> removeCache = Collections.synchronizedMap(new LinkedHashMap<String,SoftReference<Bitmap>>(10,1.5f,true));
	
	//������ͼƬ��ռ�õ��ֽڣ���ʼ0����ͨ���˱����ϸ���ƻ�����ռ�õĶ��ڴ�
	private long size = 0 ; //current allocate size
	
	//������ͼƬ��ռ�õ��ֽڣ���ʼ0����ͨ���˱����ϸ���ƻ�����ռ�õĶ��ڴ�
	private long removeSize = 0 ; //current allocate size
	
	//����ֻ��ռ�õ������ڴ�
	private long limit = 1000000; //max memory in bytes
	
	public MemoryCache(){
		//use 25% of available heap size 
		setLimit(Runtime.getRuntime().maxMemory() / 10);
	}
	
	public void setLimit(long new_limit){
		limit = new_limit;
		Logger.i(TAG, "MemoryCache will use up to" + limit / 1024. / 1024. + "MB");
	}
	
	public Bitmap get(String id){
		try {
			if(!cache.containsKey(id)){
				return null;
			}
			SoftReference<Bitmap> rf = cache.get(id);
			return rf.get();
		} catch (NullPointerException ex) {
			return null;
		}
	}
	
	public Bitmap getRemove(String id){
		try {
			if(!removeCache.containsKey(id)){
				return null;
			}
			SoftReference<Bitmap> rf = cache.get(id);
			return rf.get();
		} catch (NullPointerException ex) {
			return null;
		}
	}
	
	public void put(String id,Bitmap bitmap){
		try {
			if(cache.containsKey(id)){
				SoftReference<Bitmap> sf = cache.get(id);
				size -= getSizeInBytes(sf.get());
			}
			SoftReference<Bitmap> sf2 = new SoftReference<Bitmap>(bitmap);
			cache.put(id, sf2);
			size += getSizeInBytes(sf2.get());
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	
	public void putRemove(String id, Bitmap bitmap){
		try {
			if(removeCache.containsKey(id)){
				SoftReference<Bitmap> sf = removeCache.get(id);
				removeSize -= getSizeInBytes(sf.get());
			}
			SoftReference<Bitmap> sf2 = new SoftReference<Bitmap>(bitmap);
			removeCache.put(id, sf2);
			removeSize += getSizeInBytes(sf2.get());
			checkRemoveSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	
	/**
	 * �ϸ���ƶ��ڴ棬��������������滻�������ʹ�õ��Ǹ�ͼƬ����
	 */
	private void checkSize(){
		Logger.i(TAG, "cache size="+size+" length="+cache.size());
		if(size > limit){
			//�ȱ����������ʹ�õ�Ԫ��
			Iterator<Entry<String,SoftReference<Bitmap>>> iter = cache.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String,SoftReference<Bitmap>> entry = iter.next();
				SoftReference<Bitmap> sf = entry.getValue();
 				size -= getSizeInBytes(sf.get());
				iter.remove();
				if(size <= limit)
					break;
			}
			Logger.i(TAG, "Clean cache , New size" + cache.size());
		}
	}
	
	/**
	 * �ϸ���ƶ��ڴ棬��������������滻�������ʹ�õ��Ǹ�ͼƬ����
	 */
	private void checkRemoveSize(){
		Logger.i(TAG, "cache size="+size+" length="+cache.size());
		if(removeSize > limit){
			//�ȱ����������ʹ�õ�Ԫ��
			Iterator<Entry<String,SoftReference<Bitmap>>> iter = removeCache.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String,SoftReference<Bitmap>> entry = iter.next();
				SoftReference<Bitmap> sf = entry.getValue();
				removeSize -= getSizeInBytes(sf.get());
				iter.remove();
				if(removeSize <= limit)
					break;
			}
			Logger.i(TAG, "Clean cache , New size" + cache.size());
		}
	}
	
	public void clear (){
		cache.clear();
	}
	
	public void clearRemove (){
		removeCache.clear();
	}
	
	/**
	 * ͼƬռ�õ��ڴ�
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap){
		if(bitmap == null){
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
