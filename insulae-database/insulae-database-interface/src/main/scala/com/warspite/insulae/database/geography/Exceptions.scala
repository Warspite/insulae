package com.warspite.insulae.database.geography
import com.warspite.insulae.database.InsulaeDatabaseException
import com.warspite.common.database.ExpectedRecordNotFoundException

class AreaAtCoordinatesAlreadyExistsException(realmId: Int, x: Int, y: Int) extends InsulaeDatabaseException("Could not insert area at coordinates " + realmId + "," + x + "," + y + ", because it already exists in database.");
class LocationAtCoordinatesAlreadyExistsException(areaId: Int, x: Int, y: Int) extends InsulaeDatabaseException("Could not insert location at coordinates " + areaId + "," + x + "," + y + ", because it already exists in database.");
