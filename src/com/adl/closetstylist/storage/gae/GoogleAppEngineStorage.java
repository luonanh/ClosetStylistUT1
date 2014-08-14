package com.adl.closetstylist.storage.gae;

import java.io.File;

import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.storage.StorageInterface;

import android.net.Uri;

public class GoogleAppEngineStorage implements StorageInterface {
	ItemDatabaseHelper dbHelper;
	
	public GoogleAppEngineStorage(ItemDatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	@Override
	public Uri getOutputImageFileUri(int type, boolean isCrop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getOutputImageFile(int type, boolean isCrop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItemImagesFromStorage(ItemData item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFileExist(Uri uri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteFileIfExist(Uri uri) {
		// TODO Auto-generated method stub
		
	}

}
