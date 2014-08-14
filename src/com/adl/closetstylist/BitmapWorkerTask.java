package com.adl.closetstylist;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/*
 * ALDBG follows
 * 	- http://developer.android.com/training/displaying-bitmaps/load-bitmap.html 
 * 	- http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 * 
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{
	private final WeakReference<ImageView> imageViewReference;
	private int data = 0;
	private Context mContext;

	public BitmapWorkerTask(ImageView imageView, Context context) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		mContext = context.getApplicationContext();
	}
	
	@Override
	protected Bitmap doInBackground(Integer... params) {
		data = params[0];
		return decodeSampledBitmapFromResource(mContext.getResources(), data, 100, 100);
	}
	
    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
    	// associate the bitmap with the imageView created in the constructor 
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

		//Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		//public static Bitmap decodeStream (InputStream is, Rect outPadding, BitmapFactory.Options opts)
		//Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
		
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/*
	 * Subsample an image to load a much smaller version of the original image
	 * into memory (by power of 2, based on the target width and height).  
	 */
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}

}
