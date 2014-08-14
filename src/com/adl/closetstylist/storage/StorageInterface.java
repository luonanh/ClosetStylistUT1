package com.adl.closetstylist.storage;

import java.io.File;

import com.adl.closetstylist.ItemData;

import android.net.Uri;

public interface StorageInterface {
	//public Uri getOutputPathUri(int type);
	public Uri getOutputImageFileUri(int type, boolean isCrop);
	//public File getOutputPath(int type);
	public File getOutputImageFile(int type, boolean isCrop);
	public void deleteItemImagesFromStorage(ItemData item);
	public boolean isFileExist(Uri uri);
	public void deleteFileIfExist(Uri uri);
}
