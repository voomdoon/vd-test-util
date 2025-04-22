package de.voomdoon.testing.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.voomdoon.testing.tests.TestBase;

/**
 * Test class for {@link TempInputDirectory}.
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
@ExtendWith(TempFileExtension.class)
class TempInputDirectoryTest extends TestBase {

	/**
	 * Test class for {@link WithTempInputDirectories}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class WithTempInputDirectoriesTest extends TestBase {

		/**
		 * @author André Schulz
		 *
		 * @since 0.2.0
		 */
		@Nested
		@ExtendWith(TempFileExtension.class)
		@WithTempInputDirectories(create = false)
		class Create_false_Test extends TestBase {

			/**
			 * @since 0.2.0
			 */
			@Test
			void test_directoryDoesNotExists(@TempInputDirectory File directory) throws Exception {
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
		@WithTempInputDirectories(create = true)
		class Create_true_Test extends TestBase {

			/**
			 * @since 0.2.0
			 */
			@Test
			void test_directoryExists(@TempInputDirectory File directory) throws Exception {
				logTestStart();

				assertThat(directory).exists();
			}
		}
	}

	/**
	 * DOCME add JavaDoc for method test_isNotCreated
	 * 
	 * @since 0.2.0
	 */
	@Test
	void test_isNotCreated(@TempInputDirectory File directory) throws Exception {
		logTestStart();

		assertThat(directory).doesNotExist();
	}

	/**
	 * @param directory
	 * @since 0.2.0
	 */
	@Test
	void test_parentIsNamedInput(@TempInputDirectory File directory) {
		logTestStart();

		assertThat(directory.getParentFile().getName()).isEqualTo("input");
	}

	/**
	 * @param directory
	 * @since 0.2.0
	 */
	@Test
	void test_twoAreNotEqual(@TempInputDirectory File directory1, @TempInputDirectory File directory2) {
		logTestStart();

		assertThat(directory1).isNotEqualTo(directory2);
	}
}
