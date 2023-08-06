package de.voomdoon.testing.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
class FileTestingUtilTest {

	/**
	 * DOCME add JavaDoc for FileTestingUtilTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	static class ProvideResourceAsFile extends TestBase {

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @throws IOException
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test() throws IOException {
			logTestStart();

			String fileName = getTempDirectory() + "/test.file";
			FileTestingUtil.provideResourceAsFile("test.file", fileName);

			assertThat(new File(fileName)).isFile();
		}
	}
}
