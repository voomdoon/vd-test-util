package de.voomdoon.testing.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link TestBase}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public abstract class TestBaseTest {

	/**
	 * Test class for {@link TestBase#getTempDirectory()}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	static class GetTempDirectoryTest extends TestBase {

		/**
		 * @since 0.1.0
		 */
		private static final String TARGET_TEMP = "target/temp";

		/**
		 * @throws IOException
		 * @since 0.1.0
		 */
		@Test
		void test() throws IOException {
			logTestStart();

			Path actual = getTempDirectory();

			assertThat(actual).exists().isDirectory();
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_before() {
			logTestStart();

			Path actual = Paths.get(TARGET_TEMP);

			assertThat(actual).doesNotExist();
		}

		@Test
		void test_exists_file() throws Exception {
			logTestStart();

			Path file = Paths.get(TARGET_TEMP);
			Files.createFile(file);

			try {
				assertThrows(FileAlreadyExistsException.class, () -> getTempDirectory());
			} finally {
				Files.delete(file);
			}
		}

		/**
		 * @throws IOException
		 * @since 0.1.0
		 */
		@Test
		void test_twice() throws IOException {
			logTestStart();

			assertThat(getTempDirectory()).isEqualTo(getTempDirectory());
		}
	}

	/**
	 * Test class for {@link TestBase#getTestMethodName()}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	static class GetTestMethodNameTest extends TestBase {

		@Test
		void test_error_IllegalStateException() throws Exception {
			logTestStart();

			MyTestBase test = new MyTestBase();

			assertThrows(IllegalStateException.class, () -> test.getTestMethodName());
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_overwriteLogTestStart() {
			logTestStart();

			MyTestBase test = new MyTestBase();
			test.logTestStart();

			assertThat(test.getTestMethodName()).isEqualTo("test_overwriteLogTestStart");
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_someName() {
			logTestStart();

			assertThat(getTestMethodName()).isEqualTo("test_someName");
		}
	}

	/**
	 * Test class for {@link TestBase#logTestStart()}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	static class LogTestStartTest extends TestBase {

		/**
		 * @throws Exception
		 * @since 0.1.0
		 */
		@Test
		void test_someName() throws Exception {
			logTestStart();

			new MyTestBase().logTestStart();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));

			new MyTestBase().logTestStart();

			assertThat(new String(out.toByteArray())).contains("test_someName");
		}
	}

	/**
	 * Test class for {@link TestBase#te}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	static class TearDowntempDirectoryTest extends TestBase {

		/**
		 * DOCME add JavaDoc for method test_directory
		 * 
		 * @since DOCME add inception version number
		 */
		@Test
		void test_directory() throws Exception {
			logTestStart();

			File directory = new File(getTempDirectory() + "/directory");
			directory.mkdir();

			tearDownTempDirectory();

			assertThat(directory).doesNotExist();
		}

		/**
		 * @since DOCME add inception version number
		 */
		@Test
		void test_file() throws Exception {
			logTestStart();

			Path file = Path.of(getTempDirectory().toString(), "test.file");

			Files.writeString(file, "test");

			tearDownTempDirectory();

			assertThat(file.toFile()).doesNotExist();
		}

		/**
		 * @throws Exception
		 * @since 0.1.0
		 */
		@Test
		void test_root() throws Exception {
			logTestStart();

			Path actual = getTempDirectory();

			tearDownTempDirectory();

			assertThat(actual).doesNotExist();
		}

		/**
		 * @throws Exception
		 * @since DOCME add inception version number
		 */
		@Test
		void test_RuntimeException_file_inUse() throws Exception {
			logTestStart();

			Path temp = getTempDirectory();

			BufferedWriter bw = new BufferedWriter(new FileWriter(temp + "/file"));

			assertThrows(RuntimeException.class, () -> tearDownTempDirectory());

			assertThat(temp).exists().isDirectory();

			bw.close();
		}
	}

	/**
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	private static class MyTestBase extends TestBase {

		/**
		 * @since 0.1.0
		 */
		@Override
		protected void logTestStart() {
			super.logTestStart();
		}
	}
}
