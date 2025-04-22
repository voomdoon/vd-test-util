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
class TempOutputDirectoryTest extends TestBase {

	/**
	 * Test class for {@link WithTempOutputDirectories}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class WithTempOutputDirectoriesTest extends TestBase {

		/**
		 * @author André Schulz
		 *
		 * @since 0.2.0
		 */
		@Nested
		@ExtendWith(TempFileExtension.class)
		@WithTempOutputDirectories(create = false)
		class Create_false_Test extends TestBase {

			/**
			 * @since 0.2.0
			 */
			@Test
			void test_directoryDoesNotExists(@TempOutputDirectory File directory) throws Exception {
				logTestStart();

				assertThat(directory).doesNotExist();
			}
		}

		/**
		 * @author André Schulz
		 *
		 * @since 0.2.0
		 */
		@Nested
		@ExtendWith(TempFileExtension.class)
		@WithTempOutputDirectories(create = true)
		class Create_true_Test extends TestBase {

			/**
			 * @since 0.2.0
			 */
			@Test
			void test_directoryExists(@TempOutputDirectory File directory) throws Exception {
				logTestStart();

				assertThat(directory).exists();
			}
		}
	}

	/**
	 * @param directory
	 * @since 0.2.0
	 */
	@Test
	void test_parentIsNamedOutput(@TempOutputDirectory File directory) {
		logTestStart();

		assertThat(directory.getParentFile().getName()).isEqualTo("output");
	}

	/**
	 * @param directory
	 * @since 0.2.0
	 */
	@Test
	void test_twoAreNotEqual(@TempOutputDirectory File directory1, @TempOutputDirectory File directory2) {
		logTestStart();

		assertThat(directory1).isNotEqualTo(directory2);
	}
}
