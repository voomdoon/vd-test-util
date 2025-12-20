package de.voomdoon.testing.system;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.api.SoftAssertionsProvider.ThrowingRunnable;

import de.voomdoon.logging.Logger;

/**
 * Wrapper for the output at the console.
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
public class SystemPrintStreamCapturer {

	/**
	 * Runs a {@link ThrowingRunnable} and tracks the output.
	 *
	 * @param runnable
	 *            the {@link ThrowingRunnable} to run
	 * @return The tracked {@link SystemPrintStreamCapturer}.
	 * @throws InvocationTargetException
	 * @since 0.2.0
	 */
	public static SystemPrintStreamCapturer run(ThrowingRunnable runnable) throws InvocationTargetException {
		return run(runnable, false);
	}

	/**
	 * Runs a {@link ThrowingRunnable} and tracks the output. Catches {@link Throwable}.
	 *
	 * @param runnable
	 *            the {@link ThrowingRunnable} to run
	 * @return {@link SystemPrintStreamCapturer}
	 * @throws InvocationTargetException
	 * @since 0.2.0
	 * @see SystemPrintStreamCapturer#getException()
	 */
	public static SystemPrintStreamCapturer runWithCatch(ThrowingRunnable runnable) throws InvocationTargetException {
		return run(runnable, true);
	}

	/**
	 * @param runnable
	 *            The {@link ThrowingRunnable} to run
	 * @param catchException
	 *            {@code true} to catch exceptions, {@code false} to throw them
	 * @return {@link SystemPrintStreamCapturer}
	 * @throws InvocationTargetException
	 * @since 0.2.0
	 */
	private static SystemPrintStreamCapturer run(ThrowingRunnable runnable, boolean catchException)
			throws InvocationTargetException {
		PrintStream out = System.out;
		PrintStream err = System.err;

		try {
			ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
			PrintStream outputStreamOut = new PrintStream(baosOut);
			System.setOut(outputStreamOut);

			ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
			PrintStream outputStreamErr = new PrintStream(baosErr);
			System.setErr(outputStreamErr);

			return run(runnable, catchException, baosOut, baosErr);
		} finally {
			System.setOut(out);
			System.setErr(err);
		}
	}

	/**
	 * @param runnable
	 *            The {@link ThrowingRunnable} to run
	 * @param catchException
	 *            {@code true} to catch exceptions, {@code false} to throw them
	 * @param out
	 *            where to capture {@link System#out}
	 * @param err
	 *            where to capture {@link System#err}
	 * @return {@link SystemPrintStreamCapturer}
	 * @throws InvocationTargetException
	 * @since 0.2.0
	 */
	private static SystemPrintStreamCapturer run(ThrowingRunnable runnable, boolean catchException,
			ByteArrayOutputStream out, ByteArrayOutputStream err) throws InvocationTargetException {
		try {
			runnable.run();
		} catch (Throwable e) {
			if (catchException) {
				return new SystemPrintStreamCapturer(new String(out.toByteArray()), new String(err.toByteArray()), e);
			}

			throw new InvocationTargetException(e);
		}

		return new SystemPrintStreamCapturer(new String(out.toByteArray()), new String(err.toByteArray()));
	}

	/**
	 * @since 0.2.0
	 */
	private final String err;

	/**
	 * @since 0.2.0
	 */
	private Throwable exception;

	/**
	 * @since 0.2.0
	 */
	private final String out;

	/**
	 * @param out
	 *            {@link System.out}
	 * @param err
	 *            {@link System.err}
	 *
	 * @since 0.2.0
	 */
	private SystemPrintStreamCapturer(String out, String err) {
		this.out = out;
		this.err = err;
	}

	/**
	 * @param out
	 * @param err
	 * @param exception
	 * @since 0.2.0
	 */
	private SystemPrintStreamCapturer(String out, String err, Throwable exception) {
		this(out, err);

		this.exception = exception;
	}

	/**
	 * Returns the captured output to {@link System#err}.
	 *
	 * @return captured output as {@link String}
	 * @since 0.2.0
	 */
	public String getErr() {
		return err;
	}

	/**
	 * Returns the captured {@link Exception} if any.
	 * 
	 * @return {@link Optional} containing the exception as {@link Throwable}, or empty if none
	 * @since 0.2.0
	 */
	public Optional<Throwable> getException() {
		return Optional.ofNullable(exception);
	}

	/**
	 * Returns the captured output to {@link System#out}.
	 *
	 * @return captured output as {@link String}
	 * @since 0.2.0
	 */
	public String getOut() {
		return out;
	}

	/**
	 * Logs the contend of the {@link PrintStream}s.
	 *
	 * @param logger
	 * @since 0.2.0
	 */
	public void log(Logger logger) {
		Objects.requireNonNull(logger, "logger");

		log(logger, err, "err");
		log(logger, out, "out");
	}

	/**
	 * Logs a message if not empty.
	 *
	 * @param logger
	 *            {@link Logger}
	 * @param message
	 *            message {@link String}
	 * @param name
	 *            name of the stream as {@link String}
	 * @since 0.2.0
	 */
	private void log(Logger logger, String message, String name) {
		if (!message.isEmpty()) {
			logger.debug(name + ":\n" + message);
		}
	}
}
