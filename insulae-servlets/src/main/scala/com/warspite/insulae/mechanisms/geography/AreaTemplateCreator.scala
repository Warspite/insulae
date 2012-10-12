package com.warspite.insulae.mechanisms.geography
import com.warspite.insulae.database.InsulaeDatabase
import org.slf4j.LoggerFactory
import com.warspite.insulae.database.geography.AreaTemplate
import java.io.File


class AreaTemplateCreator(val db: InsulaeDatabase) {
  protected val logger = LoggerFactory.getLogger(getClass());

  def createTemplate(realmCanonicalName: String, jsonFile: File): AreaTemplate = {
    return null;
  }
}
