package de.voomdoon.testing.tests;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.experimental.UtilityClass;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@UtilityClass
public class FileTestingUtil {

	/**
	 * DOCME add JavaDoc for method provideResourceAsFile
	 * 
	 * @param sourceResource
	 * @param targetFile
	 * @throws IOException
	 *             DOCME
	 * @since 0.1.0
	 */
	public static void provideResourceAsFile(String sourceResource, String targetFile) throws IOException {
		InputStream is = FileTestingUtil.class.getResourceAsStream("/" + sourceResource);

		Path path = Paths.get(targetFile);
		Files.copy(is, path);
	}
}
