package com.adl.closetstylist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.adl.closetstylist.UriWorkerTask.AsyncDrawable;

public class ImageSubSampler {
	private Bitmap mLoadingBitmap;
	private Resources mResources;
	
	public ImageSubSampler(Context context) {
		mLoadingBitmap = BitmapFactory.decodeResource(mResources, R.drawable.ab_bg);
	}

	/*
	 * This function is used to subsample an image resId by a power of 2 
	 * and set the imageView to the newly subsampled image. 
	 */
	public static void subSample(int resId, ImageView imageView, Context context) {
		BitmapWorkerTask task = new BitmapWorkerTask(imageView, context);
	    task.execute(resId);
	}

	public void subSampleCroppedUri(ItemData itemData, ImageView imageView, Context context) {
		if (UriWorkerTask.cancelPotentialWork(itemData, imageView)) {
			UriWorkerTask task = new UriWorkerTask(imageView, context, true);
			AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
		    task.execute(itemData);			
		}
	}

	public void subSampleOriginalUri(ItemData itemData, ImageView imageView, Context context) {
		if (UriWorkerTask.cancelPotentialWork(itemData, imageView)) {
			UriWorkerTask task = new UriWorkerTask(imageView, context, false);
			AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
		    task.execute(itemData);			
		}
	}	
}
