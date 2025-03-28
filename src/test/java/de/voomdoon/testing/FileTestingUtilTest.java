package de.voomdoon.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.voomdoon.testing.file.TempFile;
import de.voomdoon.testing.file.TempFileExtension;
import de.voomdoon.testing.tests.TestBase;

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
	@Nested
	@ExtendWith(TempFileExtension.class)
	class ProvideResourceAsFile extends TestBase {

		/**
		 * DOCME add JavaDoc for method test
		 * 
		 * @throws IOException
		 * 
		 * @since 0.1.0
		 */
		@Test
		void test(@TempFile File file) throws IOException {
			logTestStart();

			FileTestingUtil.provideResourceAsFile("test.file", file.getAbsolutePath());

			assertThat(file).isFile();
		}
	}
}
