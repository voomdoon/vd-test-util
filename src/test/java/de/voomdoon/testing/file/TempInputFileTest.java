package de.voomdoon.testing.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.voomdoon.testing.tests.TestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
@ExtendWith(TempFileExtension.class)
class TempInputFileTest extends TestBase {

	/**
	 * DOCME add JavaDoc for TempInputFileTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class WithTempInputFilesTest {

		/**
		 * DOCME add JavaDoc for TempInputFileTest.WithTempInputFilesTest
		 *
		 * @author André Schulz
		 *
		 * @since 0.2.0
		 */
		@Nested
		@WithTempInputFiles(extension = "txt")
		class ExtensionTest extends TestBase {

			/**
			 * DOCME add JavaDoc for method test_File_hasConfiguredExtension
			 * 
			 * @since 0.2.0
			 */
			@Test
			void test_File_hasConfiguredExtension(@TempInputFile File file) throws Exception {
				logTestStart();

				assertThat(file.getName()).endsWith(".txt");
			}
		}
	}

	/**
	 * @param file
	 * @since 0.2.0
	 */
	@Test
	void test_File_isAtSomeInputDirectory(@TempInputFile File file) {
		logTestStart();

		assertThat(file.getParent()).contains("input");
	}
}
