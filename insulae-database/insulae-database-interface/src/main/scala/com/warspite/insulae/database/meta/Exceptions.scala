package com.warspite.insulae.database.meta
import com.warspite.insulae.database.InsulaeDatabaseException
import com.warspite.common.database.ExpectedRecordNotFoundException

class TroubleReportTypeIdDoesNotExistException(id: Int) extends ExpectedRecordNotFoundException("No trouble report type with id " + id + " exists in database.") {}
