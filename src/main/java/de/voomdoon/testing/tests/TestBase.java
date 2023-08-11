package de.voomdoon.testing.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;

import de.voomdoon.logging.LogManager;
import de.voomdoon.logging.Logger;

//TODO https://github.com/voomdoon/vd-test-util/issues/1 rework logging of test start and end using TestExecutionListener

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
	private static final String TEMP_DIRECTORY_NAME = "target/temp";

	/**
	 * @since 0.1.0
	 */
	protected final Logger logger = LogManager.getLogger(getClass());

	/**
	 * @since 0.1.0
	 */
	private Path tempDirectory;

	/**
	 * @since 0.1.0
	 */
	private String testMethodName;

	/**
	 * @return {@link Path}
	 * @since 0.1.0
	 */
	protected synchronized Path getTempDirectory() throws IOException {
		tempDirectory = Paths.get(TEMP_DIRECTORY_NAME);

		if (!Files.exists(tempDirectory)//
				|| Files.isRegularFile(tempDirectory)// get proper exception
		) {
			Files.createDirectory(tempDirectory);
		}

		return Paths.get(TEMP_DIRECTORY_NAME);
	}

	/**
	 * @return {@link String}
	 * @since 0.1.0
	 */
	protected String getTestMethodName() {// TESTME
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
	 * @since 0.1.0
	 */
	@AfterEach
	void tearDownTempDirectory() {
		if (tempDirectory != null && Files.exists(tempDirectory)) {
			try {
				deleteDirectory(tempDirectory.toFile());
			} catch (IOException e) {
				// TODO implement error handling
				throw new RuntimeException("Error at 'tearDownTempDirectory': " + e.getMessage(), e);
			}
		}
	}

	/**
	 * DOCME add JavaDoc for method deleteDirectory
	 * 
	 * @param directory
	 * @since 0.1.0
	 */
	private void deleteDirectory(File directory) throws IOException {
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				Files.delete(file.toPath());
			}
		}

		Files.delete(directory.toPath());
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
