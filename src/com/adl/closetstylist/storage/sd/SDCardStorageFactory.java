package com.adl.closetstylist.storage.sd;

import com.adl.closetstylist.storage.StorageFactory;
import com.adl.closetstylist.storage.StorageInterface;

public class SDCardStorageFactory extends StorageFactory {
	
	@Override
	protected StorageInterface createStorage() {
		return new SDCardStorage();
	}

}
