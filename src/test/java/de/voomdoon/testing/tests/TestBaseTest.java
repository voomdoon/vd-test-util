package de.voomdoon.testing.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link TestBase}.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class TestBaseTest {

	/**
	 * Test class for {@link TestBase#getTestMethodName()}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	@Nested
	class GetTestMethodNameTest extends TestBase {

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
	@Nested
	class LogTestStartTest extends TestBase {

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
