package de.voomdoon.testing.tests;

import org.junit.jupiter.api.AfterEach;

import de.voomdoon.logging.LogManager;
import de.voomdoon.logging.Logger;

//TODO #1: rework logging of test start and end using TestExecutionListener

/**
 * Base class for tests.
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public abstract class TestBase {

	/**
	 * @since 0.1.0
	 */
	protected final Logger logger = LogManager.getLogger(getClass());

	/**
	 * @since 0.1.0
	 */
	private String testMethodName;

	/**
	 * @return {@link String}
	 * @since 0.1.0
	 */
	protected String getTestMethodName() {
		if (testMethodName == null) {
			throw new IllegalStateException("Test method name not known => 'logTestStart' not called!");
		}

		return testMethodName;
	}

	/**
	 * @since 0.1.0
	 */
	@AfterEach
	protected void logTestEnd() {
		logger.debug("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	}

	/**
	 * @since 0.1.0
	 */
	protected void logTestStart() {
		testMethodName = determineTestMethodName();

		logger.debug("+ + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +");
		logger.info("running test '" + testMethodName + "'...");
	}

	/**
	 * @return {@link String}
	 * @since 0.1.0
	 */
	private String determineTestMethodName() {
		int i = 3;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		while (true) {
			String name = stackTrace[i].getMethodName();

			if (!name.equals("logTestStart")) {
				return name;
			}

			i++;
		}
	}
}
