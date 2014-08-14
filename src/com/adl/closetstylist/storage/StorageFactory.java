package com.adl.closetstylist.storage;


public abstract class StorageFactory {

	abstract protected StorageInterface createStorage();
	
	public StorageInterface getInstance() {
		return createStorage();
	}
}
