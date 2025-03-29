package de.voomdoon.testing.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.voomdoon.testing.tests.TestBase;

/**
 * DOCME add JavaDoc for UseTempFilesTest
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
@Nested
@ExtendWith(TempFileExtension.class)
class TempFileTest extends TestBase {

	/**
	 * @since 0.2.0
	 */
	@Test
	void test_File(@TempFile File file) throws Exception {
		logTestStart();

		logger.debug("file: " + file);

		assertThat(file).isNotNull();
	}

	/**
	 * @since 0.2.0
	 */
	@Test
	void test_File_canBeCreated(@TempFile File file) throws Exception {
		logTestStart();

		boolean success = file.createNewFile();

		assertThat(success).isTrue();
	}

	/**
	 * @since 0.2.0
	 */
	@Test
	void test_File_doesNotExist(@TempFile File file) throws Exception {
		logTestStart();

		assumeThat(file).isNotNull();

		assertThat(file).doesNotExist();
	}

	/**
	 * @since 0.2.0
	 */
	@Test
	void test_File_twoAreDifferent(@TempFile File file1, @TempFile File file2) throws Exception {
		logTestStart();

		assertThat(file1).isNotEqualTo(file2);
	}

	/**
	 * @since 0.2.0
	 */
	@Test
	void test_Path(@TempFile Path path) throws Exception {
		logTestStart();

		logger.debug("path: " + path);

		assertThat(path).isNotNull();
	}
}