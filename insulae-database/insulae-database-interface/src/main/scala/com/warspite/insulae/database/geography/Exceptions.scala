package com.warspite.insulae.database.geography
import com.warspite.insulae.database.InsulaeDatabaseException
import com.warspite.common.database.ExpectedRecordNotFoundException

class AreaIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No area with id " + id + " exists in database.") {}
class LocationIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No location with id " + id + " exists in database.") {}
class LocationCoordinatesDoNotExistException(areaId: Int, x: Int, y: Int) extends ExpectedRecordNotFoundException("No location with coordinates " + areaId + "," + x + "," + y + " exists in database.") {}
class LocationTypeIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No location type with id " + id + " exists in database.") {}
class TransportationCostDoesNotExistException(locationTypeId: Int, transportationTypeId: Int) extends ExpectedRecordNotFoundException("No transportation cost with locationTypeId " + locationTypeId + " and transportationTypeId " + transportationTypeId + " exists in database.") {}
class TransportationTypeIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No transportation type with id " + id + " exists in database.") {}
class ResourceTypeIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No resource type with id " + id + " exists in database.") {}
