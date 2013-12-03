package com.athudong.psr.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.graphics.Bitmap;

/**
 * bitmap缓存管理
 */
public class MemoryCache {
	
	private static final String TAG = "MemoryCache";
	//放入缓存时是个同步操作
	//linkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	//这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String,SoftReference<Bitmap>> cache = Collections.synchronizedMap(new LinkedHashMap<String,SoftReference<Bitmap>>(10,1.5f,true));
	private Map<String,SoftReference<Bitmap>> removeCache = Collections.synchronizedMap(new LinkedHashMap<String,SoftReference<Bitmap>>(10,1.5f,true));
	
	//缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0 ; //current allocate size
	
	//缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long removeSize = 0 ; //current allocate size
	
	//缓存只能占用的最大堆内存
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
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 */
	private void checkSize(){
		Logger.i(TAG, "cache size="+size+" length="+cache.size());
		if(size > limit){
			//先遍历最近最少使用的元素
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
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 */
	private void checkRemoveSize(){
		Logger.i(TAG, "cache size="+size+" length="+cache.size());
		if(removeSize > limit){
			//先遍历最近最少使用的元素
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
	 * 图片占用的内存
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap){
		if(bitmap == null){
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
