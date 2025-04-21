package de.voomdoon.testing.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

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
class TempInputDirectoryTest extends TestBase {

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
