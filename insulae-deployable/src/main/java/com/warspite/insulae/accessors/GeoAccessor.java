package com.warspite.insulae.accessors;

import java.io.File;

import com.warspite.common.cli.CliListener;
import com.warspite.common.cli.annotations.Cmd;
import com.warspite.insulae.mechanisms.geography.AreaTemplateCreator;
import com.warspite.insulae.database.geography.AreaTemplate;

public class GeoAccessor implements CliListener {
	private AreaTemplateCreator areaTemplateCreator = null;
	
	public GeoAccessor() {
	}

	public void injectHelpers(final AreaTemplateCreator areaTemplateCreator) {
		this.areaTemplateCreator = areaTemplateCreator;
	}
	
	@Cmd(name="createAreaTemplate",description="Create new area template in <realmCanonicalName> based on JSON properties in <path>.", printReturnValue = true)
	public String createAreaTemplate(String realmCanonicalName, String path) throws InterruptedException {
		if(areaTemplateCreator == null)
			return "AreaTemplateCreator hasn't been set.";
		
		File templateFile = new File(path);
		if(!templateFile.exists())
			return "Coulnd't find " + path;
		
		try {
			AreaTemplate t = areaTemplateCreator.createTemplate(realmCanonicalName, templateFile);
			return "Created " + t + ".";
		}
		catch (Throwable e) {
			return "Failed to create area template: " + e;
		}
	}
}
