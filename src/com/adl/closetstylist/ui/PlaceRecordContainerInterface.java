package com.adl.closetstylist.ui;

import com.adl.closetstylist.PlaceRecord;

/**
 * Activities that contain a PlaceRecord member and use LocationToPostalCodeTask
 * or PostalCodeToLocationTask will need to implement this interface to update
 * the member field.  
 * 
 * @author Anh Luong
 * 
 */
public interface PlaceRecordContainerInterface {

	public void setPlaceRecord(PlaceRecord place);
}
