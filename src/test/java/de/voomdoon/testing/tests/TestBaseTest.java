package de.voomdoon.testing.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.voomdoon.logging.LogEvent;
import de.voomdoon.logging.LogEventHandler;
import de.voomdoon.logging.LogLevel;
import de.voomdoon.logging.LogManager;

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
	class LogTestStartTest extends TestBase implements LogEventHandler {

		/**
		 * @since 0.2.0
		 */
		private List<LogEvent> logEvents = new ArrayList<>();

		/**
		 * @since 0.2.0
		 */
		@Override
		public void handleLogEvent(LogEvent logEvent) {
			logEvents.add(logEvent);
		}

		/**
		 * @since 0.2.0
		 */
		@AfterEach
		void afterEach_removeLogEventHandler() {
			try {
				LogManager.removeLogEventHandler(this);
			} catch (NoSuchElementException e) {
				// ignore
			}
		}

		/**
		 * @throws Exception
		 * @since 0.1.0
		 */
		@Test
		void test_someName() throws Exception {
			logTestStart();

			LogManager.addLogEventHandler(this);

			new MyTestBase().logTestStart();

			List<LogEvent> infoEvents = logEvents.stream().filter(e -> e.getLevel() == LogLevel.INFO).toList();

			assertThat(infoEvents).hasSize(1);
			assertThat(infoEvents.get(0).getMessage()).isEqualTo("running test 'test_someName'...");
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
