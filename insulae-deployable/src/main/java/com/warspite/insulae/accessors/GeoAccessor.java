package com.warspite.insulae.accessors;

import java.io.File;

import com.warspite.common.cli.CliListener;
import com.warspite.common.cli.annotations.Cmd;
import com.warspite.insulae.mechanisms.geography.AreaTemplateCreator;
import com.warspite.insulae.database.geography.AreaTemplate;

public class GeoAccessor implements CliListener {
	private AreaTemplateCreator areaTemplateCreator = null;
	
	public void injectHelpers(final AreaTemplateCreator areaTemplateCreator) {
		this.areaTemplateCreator = areaTemplateCreator;
	}
	
	@Cmd(name="createAreaTemplate",description="Create new area template in <realmCanonicalName> based on JSON properties in <path>.", printReturnValue = true)
	public String createAreaTemplate(String realmCanonicalName, String path) throws InterruptedException {
		if(areaTemplateCreator == null)
			return "AreaTemplateCreator hasn't been set.";
		
		File jsonFile = new File(path + ".json");
		if(!jsonFile.exists())
			return "Coulnd't find " + jsonFile;
		
		File pngFile = new File(path + ".png");
		if(!pngFile.exists())
			return "Coulnd't find " + pngFile;
		
		try {
			AreaTemplate t = areaTemplateCreator.createTemplate(realmCanonicalName, jsonFile, pngFile);
			return "Created " + t + ".";
		}
		catch (Throwable e) {
			return "Failed to create area template: " + e;
		}
	}
}
