package com.adl.closetstylist.storage.sd;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.storage.StorageInterface;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class SDCardStorage implements StorageInterface {
	private final static String LOG_TAG = SDCardStorage.class.getCanonicalName();
	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	private static final int MEDIA_TYPE_AUDIO = 3;

	@Override
	public Uri getOutputImageFileUri(int type, boolean isCrop) {
		return Uri.fromFile(getOutputImageFile(type, isCrop));	
	}

	@Override
	public File getOutputImageFile(int type, boolean isCrop) {
		Log.d(LOG_TAG, "getOutputMediaFile() type:" + type);
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		// For future implementation: store videos in a separate directory
		File mediaStorageDir = new File(
				Environment
						.getExternalStorageDirectory(),
				"ClosetStylist");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			if (isCrop) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "CROP_IMG_" + timeStamp + ".jpg");				
			} else {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "IMG_" + timeStamp + ".jpg");				
			}
		} else {
			Log.e(LOG_TAG, "typ of media file not supported: type was:" + type);
			return null;
		}

		return mediaFile;
	}

	@Override
	public void deleteItemImagesFromStorage(ItemData item) {
		if (!item.getImageLink().isEmpty()) { // delete if the image exists
			Log.d(LOG_TAG, "Search for " + item.getImageLink());
			// File file = new File(imagePath.toString()) ALDBG doesn't work,
			// don't know detail
			File file = new File(URI.create(item.getImageLink())); // ALDBG TODO fix delete images from Galery later
			if (file.exists()) { // only delete if the image was stored on SD
									// card
				Log.d(LOG_TAG, item.getImageLink() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ item.getImageLink());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + item.getImageLink());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + item.getImageLink());
			}
		}

		if (!item.getCropImageLink().isEmpty()) { // delete if the cropped image exists
			Log.d(LOG_TAG, "Search for " + item.getCropImageLink());
			File file = new File(URI.create(item.getCropImageLink()));
			if (file.exists()) { // only delete if the image was stored on SD
				// card
				Log.d(LOG_TAG, item.getCropImageLink() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ item.getCropImageLink());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + item.getCropImageLink());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + item.getCropImageLink());
			}

		}
	}

	@Override
	public boolean isFileExist(Uri uri) {
		if (null == uri) {
			return false;
		}

		File file = new File(URI.create(uri.toString()));
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void deleteFileIfExist(Uri uri) {
		if (null != uri) {
			File file = new File(URI.create(uri.toString()));
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
