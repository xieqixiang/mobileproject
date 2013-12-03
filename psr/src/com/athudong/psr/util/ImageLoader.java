package com.athudong.psr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.athudong.psr.base.BaseAct;
import com.athudong.psr.base.BaseApp;

/**
 * 异步加载图片
 */
public class ImageLoader {
	
	private BaseAct activity;
	private BaseApp baseApp;
	private Map<ImageView,String> imagesMap = Collections.synchronizedMap(new WeakHashMap<ImageView,String>());
	
	private ExecutorService executorService;
	
	public ImageLoader(BaseAct activity){
		this.activity = activity;
		executorService = Executors.newFixedThreadPool(10);
		baseApp = (BaseApp) activity.getApplication();
	}
	
	public void DisplayImage(String url,ImageView imageView,boolean isLoadOnlyFromCache){
		imagesMap.put(imageView, url);
		//先从内存缓存中查找
		Bitmap bitmap = baseApp.memoryCache.get(url);
		if(bitmap !=null){
			imageView.setImageBitmap(bitmap);
		}else if(!isLoadOnlyFromCache){
			queuePhoto(url,imageView);
		}
	}
	
	public void DisplayImage(ArrayList<String> list,ImageView [] imageViews,boolean isLoadOnlyFromCache){
		int size = list.size();
		for(int i = 0 ; i< size ; i++){
			String url = list.get(i);
			ImageView imageView = imageViews[i];
			baseApp.removeViews.put(imageViews[i],url);
			Bitmap bitmap2 = baseApp.memoryCache.getRemove(url);
			if(bitmap2 !=null){
				imageView.setImageDrawable(AppUtil.bitmap2Drawable(activity.getResources(),bitmap2));
			}else if(!isLoadOnlyFromCache){
				queuePhotoRemove(url, imageView);
			}
		}
	}
	
	private void queuePhoto(String url,ImageView imageView){
		PhotoToLoad p = new PhotoToLoad(url,imageView);
		executorService.submit(new PhotosLoader(p));
	}
	
	private void queuePhotoRemove(String url,ImageView imageView){
		PhotoToLoad p = new PhotoToLoad(url,imageView);
		executorService.submit(new PhotosLoaderRemove(p));
	}
	
	private Bitmap getBitmap(String url){
		File f = FileUtil.getFile(url);
		//先从文件缓存中查找是否有
		Bitmap b = null;
		if(f !=null && f.exists()){
			b = decodeFile(f);
		}
		if(b !=null){
			return b;
		}
		//最后从指定的url中下载图片
		try {
			Bitmap remote = HttpUtil.downloadImage(activity, url);
			if(remote !=null){
				return remote;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	//decode 这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File file){
		try {
			//decode image size 
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file),null,o);
			
			//find the correct scale value,It should be the power of 2.
			int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while(true){
				if(width_tmp /2 < REQUIRED_SIZE || height_tmp /2 < REQUIRED_SIZE){
					break;
				}
				width_tmp /=2;
				height_tmp /=2;
				scale *=2;
			}
			//decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(file),null,o2);
		} catch (FileNotFoundException  e) {
			
		}
		return null;
	}
	
	//Task for the queue
	private class PhotoToLoad{
		public String url;
		public ImageView imageView;
		
		public PhotoToLoad(String u,ImageView i){
			url = u;
			imageView = i;
		}
	}
	
	class PhotosLoader  implements Runnable{

		PhotoToLoad photoToLoad;
		public PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}
		
		@Override
		public void run() {
			if(imageViewReused(photoToLoad)){
				return;
			}
			Bitmap bmp = getBitmap(photoToLoad.url);
			baseApp.memoryCache.put(photoToLoad.url,bmp);
			if(imageViewReused(photoToLoad)){
				return;
			}
			BitmapDisplayer bd = new BitmapDisplayer(bmp,photoToLoad);
			//更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}
	
	class PhotosLoaderRemove  implements Runnable{

		PhotoToLoad photoToLoad;
		public PhotosLoaderRemove(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}
		
		@Override
		public void run() {
			if(removeImageViewReused(photoToLoad)){
				return;
			}
			Bitmap bmp = getBitmap(photoToLoad.url);
			baseApp.memoryCache.putRemove(photoToLoad.url,bmp);
			if(removeImageViewReused(photoToLoad)){
				return;
			}
			BitmapDisplayerRemove bd = new BitmapDisplayerRemove(bmp,photoToLoad);
			//更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}
	
	/**
	 * 防止图片错位
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad){
		String tag = imagesMap.get(photoToLoad.imageView);
		if(tag == null || !tag.equals(photoToLoad.url)){
			return true;
		}
		return false;
	}
	
	/**
	 * 防止图片错位
	 * @param photoToLoad
	 * @return
	 */
	boolean removeImageViewReused(PhotoToLoad photoToLoad){
		String tag = baseApp.removeViews.get(photoToLoad.imageView);
		if(tag == null || !tag.equals(photoToLoad.url)){
			return true;
		}
		return false;
	}
	
	//用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		public BitmapDisplayer(Bitmap b,PhotoToLoad p){
			bitmap = b;
			photoToLoad = p;
		}
		
		public void run(){
			if(imageViewReused(photoToLoad)){
				return;
			}
			if(bitmap !=null){
				photoToLoad.imageView.setImageDrawable(AppUtil.bitmap2Drawable(activity.getResources(),bitmap));
			}
		}
	}
	
	//用于在UI线程中更新界面
	class BitmapDisplayerRemove implements Runnable{
			Bitmap bitmap;
			PhotoToLoad photoToLoad;
			public BitmapDisplayerRemove(Bitmap b,PhotoToLoad p){
				bitmap = b;
				photoToLoad = p;
			}
			
			public void run(){
				if(removeImageViewReused(photoToLoad)){
					return;
				}
				if(bitmap !=null){
					photoToLoad.imageView.setImageDrawable(AppUtil.bitmap2Drawable(activity.getResources(),bitmap));
				}
			}
		}
	
	public void clearCache(){
		baseApp.memoryCache.clear();
	}
	
	public void clearRemove(){
		baseApp.memoryCache.clearRemove();
	}
	
}
