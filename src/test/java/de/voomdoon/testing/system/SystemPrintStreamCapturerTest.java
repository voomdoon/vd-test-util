package de.voomdoon.testing.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.assertj.core.api.SoftAssertionsProvider.ThrowingRunnable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.voomdoon.logging.Logger;
import de.voomdoon.testing.tests.TestBase;

/**
 * Tests for {@link SystemPrintStreamCapturer}.
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
class SystemPrintStreamCapturerTest extends TestBase {

	/**
	 * Tests for {@link SystemPrintStreamCapturer#log(Logger)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class LogTest extends TestBase {

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_err() throws Exception {
			logTestStart();

			Logger logger = Mockito.mock(Logger.class);

			SystemPrintStreamCapturer output = SystemPrintStreamCapturer.run(() -> System.err.println("test"));

			output.log(logger);

			verify(logger).debug("err:\ntest" + System.lineSeparator());
			verifyNoMoreInteractions(logger);
		}

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_errAndOut() throws Exception {
			logTestStart();

			Logger logger = Mockito.mock(Logger.class);

			SystemPrintStreamCapturer output = SystemPrintStreamCapturer.run(() -> {
				System.err.println("err1");
				System.out.println("out1");
				System.err.println("err2");
				System.out.println("out2");
			});

			output.log(logger);

			verify(logger).debug("err:\nerr1" + System.lineSeparator() + "err2" + System.lineSeparator());
			verify(logger).debug("out:\nout1" + System.lineSeparator() + "out2" + System.lineSeparator());
			verifyNoMoreInteractions(logger);
		}

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_out() throws Exception {
			logTestStart();

			Logger logger = Mockito.mock(Logger.class);

			SystemPrintStreamCapturer output = SystemPrintStreamCapturer.run(() -> System.out.println("test"));

			output.log(logger);

			verify(logger).debug("out:\ntest" + System.lineSeparator());
			verifyNoMoreInteractions(logger);
		}
	}

	/**
	 * Tests for {@link SystemPrintStreamCapturer#run(Runnable)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class RunTest extends TestBase {

		/**
		 * @param runnable
		 * @return
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		protected SystemPrintStreamCapturer run(ThrowingRunnable runnable) throws InvocationTargetException {
			SystemPrintStreamCapturer output = SystemPrintStreamCapturer.run(runnable);
			output.log(logger);

			return output;
		}

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_backupErr() throws Exception {
			logTestStart();

			PrintStream expected = System.err;

			run(() -> {
			});

			assertThat(System.err).isEqualTo(expected);
		}

		/**
		 * @since 0.2.0
		 */
		@Test
		void test_backupOut() throws Exception {
			logTestStart();

			PrintStream expected = System.out;

			run(() -> {
			});

			assertThat(System.out).isEqualTo(expected);
		}

		/**
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		@Test
		void test_err_println() throws InvocationTargetException {
			logTestStart();

			ThrowingRunnable runnable = () -> System.err.println("test");

			SystemPrintStreamCapturer actual = run(runnable);

			assertThat(actual).extracting(SystemPrintStreamCapturer::getErr).asString().containsSubsequence("test",
					"\n");
		}

		/**
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		@Test
		void test_Exception() throws InvocationTargetException {
			logTestStart();

			ThrowingRunnable runnable = () -> {
				throw new RuntimeException("test");
			};

			InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> run(runnable));

			assertThat(thrown.getTargetException()).hasMessage("test");
		}

		/**
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		@Test
		void test_out_println() throws InvocationTargetException {
			logTestStart();

			ThrowingRunnable runnable = () -> System.out.println("test");

			SystemPrintStreamCapturer actual = run(runnable);

			assertThat(actual).extracting(SystemPrintStreamCapturer::getOut).asString().containsSubsequence("test",
					"\n");
		}

		/**
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		@Test
		void test_out_println2() throws InvocationTargetException {
			logTestStart();

			ThrowingRunnable runnable = () -> {
				System.out.println("test1");
				System.out.println("test2");
			};

			SystemPrintStreamCapturer actual = run(runnable);

			assertThat(actual).extracting(SystemPrintStreamCapturer::getOut).asString().containsSubsequence("test1",
					"\n", "test2", "\n");
		}
	}

	/**
	 * Tests for {@link SystemPrintStreamCapturer#runWithCatch(Runnable)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class RunWithExceptionTest extends RunTest {

		/**
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		@Override
		protected SystemPrintStreamCapturer run(ThrowingRunnable runnable) throws InvocationTargetException {
			SystemPrintStreamCapturer output = SystemPrintStreamCapturer.runWithCatch(runnable);
			output.log(logger);

			return output;
		}

		/**
		 * @throws InvocationTargetException
		 * @since 0.2.0
		 */
		@Override
		@Test
		void test_Exception() throws InvocationTargetException {
			logTestStart();

			ThrowingRunnable runnable = () -> {
				throw new RuntimeException("test");
			};

			SystemPrintStreamCapturer actual = run(runnable);

			assertThat(actual.getException()).isPresent().get().extracting(Throwable::getMessage).isEqualTo("test");
		}
	}
}