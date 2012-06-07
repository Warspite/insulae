package com.warspite.insulae;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warspite.insulae.jetty.JettyRunner;

public class WarExtractor {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final String directoryName;
	private final String warSuffix;

	public WarExtractor(final String directoryName, final String warSuffix) {
		this.directoryName = directoryName;
		this.warSuffix = warSuffix;
	}

	public File extract() throws IOException {
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
				if( entryName.startsWith(directoryName + "/") && entryName.endsWith(warSuffix) ) {
					final File fileWithPath = new File(entryName);
					final File outFile = new File(fileWithPath.getName());

					if( outFile.exists() )
						outFile.delete();

					final FileOutputStream fileoutputstream = new FileOutputStream(outFile);

					int n;
					final byte[] buf = new byte[1024];
					while ((n = zipStream.read(buf, 0, buf.length)) > -1)
						fileoutputstream.write(buf, 0, n);

					logger.info("Extracted " + outFile + " from CodeSource.");
					return outFile;
				}
				entry = zipStream.getNextEntry();
			}
		}
		finally {
			if( outStream != null )
				outStream.close();

			if( zipStream != null )
				zipStream.close();
		}

		throw new FileNotFoundException("Couldn't find any wars/*.war file in " + src.toString() + ".");
	}
}
