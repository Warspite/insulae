package com.warspite.insulae.database.geography
import com.warspite.insulae.database.InsulaeDatabaseException
import com.warspite.common.database.ExpectedRecordNotFoundException

class AreaIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No area with id " + id + " exists in database.") {}
class LocationIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No location with id " + id + " exists in database.") {}
