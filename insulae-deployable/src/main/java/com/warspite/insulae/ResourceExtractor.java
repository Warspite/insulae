package com.warspite.insulae;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.insulae.jetty.JettyRunner;

public class ResourceExtractor {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public List<File> extract(final String directoryName, final String suffix, final boolean stripPath, final boolean overwriteExisting) throws IOException {
		final List<File> extractedResources = new ArrayList<File>();
		final CodeSource src = JettyRunner.class.getProtectionDomain().getCodeSource();

		FileOutputStream outStream = null;
		ZipInputStream zipStream = null;

		try {
			if (src == null)
				throw new IOException("Failed to get CodeSource.");

			final URL jarLocation = src.getLocation();
			zipStream = new ZipInputStream(jarLocation.openStream());

			ZipEntry entry = zipStream.getNextEntry();
			while(entry != null) {
				final String entryName = entry.getName();
				if( entryName.startsWith(directoryName + "/") && entryName.endsWith(suffix) ) {
					File f = new File(entryName);
					
					if( stripPath )
						f = new File(f.getName());

					logger.debug("Will extract " + f + " from CodeSource.");
					
					if( f.isDirectory() )
						extractDirectory(f);
					else
						extractFile(f, extractedResources, zipStream, overwriteExisting);
				}
				entry = zipStream.getNextEntry();
			}
		}
		catch(IOException e) {
			logger.error("Resource extraction failed.", e);
		}
		finally {
			if( outStream != null )
				outStream.close();

			if( zipStream != null )
				zipStream.close();
		}

		return extractedResources;
	}

	private void extractDirectory(File outFile) {
		outFile.mkdirs();
	}

	private void extractFile(File outFile, List<File> extractedResources, ZipInputStream zipStream, boolean overwriteExisting) throws IOException {
		if( outFile.exists() )
			if( overwriteExisting ) {
				logger.debug("Overwriting existing file " + outFile + ".");
				outFile.delete();
			}
			else {
				logger.debug("Skipped extraction of " + outFile + ", file already exists.");
				return;
			}

		final FileOutputStream fileoutputstream = new FileOutputStream(outFile);

		int n;
		final byte[] buf = new byte[1024];
		while ((n = zipStream.read(buf, 0, buf.length)) > -1)
			fileoutputstream.write(buf, 0, n);

		logger.debug("Extracted file " + outFile + " from CodeSource.");
		extractedResources.add(outFile);
	}
}
