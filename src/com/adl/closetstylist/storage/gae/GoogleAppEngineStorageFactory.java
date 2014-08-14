package com.adl.closetstylist.storage.gae;

import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.storage.StorageFactory;
import com.adl.closetstylist.storage.StorageInterface;

public class GoogleAppEngineStorageFactory extends StorageFactory {
	ItemDatabaseHelper dbHelper;

	public GoogleAppEngineStorageFactory(ItemDatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	@Override
	protected StorageInterface createStorage() {
		return new GoogleAppEngineStorage(dbHelper);
	}
}
