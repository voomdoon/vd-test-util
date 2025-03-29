package de.voomdoon.testing.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * JUnit 5 extension that injects temporary file instances into test method parameters annotated with {@link TempFile}.
 * Each injected file is unique and located in a temporary test directory, which is cleaned up after the test completes.
 *
 * <p>
 * To use this extension, annotate your test class with {@code @ExtendWith(TempFileExtension.class)} and annotate test
 * method parameters with {@code @TempFile}.
 * </p>
 *
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * &#64;ExtendWith(TempFileExtension.class)
 * class MyTest {
 *
 * 	&#64;Test
 * 	void testWithTempFile(@TempFile File tempFile) {
 * 		// use tempFile here
 * 	}
 * }
 * </pre>
 *
 * <p>
 * The temporary directory used is {@code target/test-temp}, and all files created during the test are automatically
 * deleted afterward.
 * </p>
 *
 * @see TempFile
 *
 * @author André Schulz
 * 
 * @since 0.2.0
 */
public class TempFileExtension implements ParameterResolver, AfterEachCallback {

	/**
	 * @since 0.2.0
	 */
	public static final String STORE_KEY = "temp-files";

	/**
	 * @since 0.2.0
	 */
	public static final String STORE_KEY_INPUT = STORE_KEY + "-input";

	/**
	 * @since 0.2.0
	 */
	private Path tempDirectory = Path.of("target", "test-temp");

	/**
	 * @since 0.2.0
	 */
	private Path tempInputDirectory = tempDirectory.resolve("input");

	/**
	 * @since 0.2.0
	 */
	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		deleteDirectory(tempDirectory.toFile());
	}

	/**
	 * @since 0.2.0
	 */
	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		boolean isInput = parameterContext.isAnnotated(TempInputFile.class);
		Path baseDir = isInput ? tempInputDirectory : tempDirectory;
		Path file = getNext(extensionContext, isInput);

		try {
			Files.createDirectories(baseDir);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create temp directory: " + baseDir, e);
		}

		Class<?> type = parameterContext.getParameter().getType();

		if (File.class.equals(type)) {
			return file.toFile();
		} else if (Path.class.equals(type)) {
			return file;
		}

		throw new ParameterResolutionException("Unsupported parameter type: " + type);
	}

	/**
	 * @since 0.2.0
	 */
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		boolean isSupportedType = //
				File.class.isAssignableFrom(parameterContext.getParameter().getType())
						|| Path.class.isAssignableFrom(parameterContext.getParameter().getType());

		boolean isAnnotated = //
				parameterContext.isAnnotated(TempFile.class) //
						|| parameterContext.isAnnotated(TempInputFile.class);

		return isSupportedType && isAnnotated;
	}

	/**
	 * @param directory
	 *            {@link File}
	 * @since 0.2.0
	 */
	private void deleteDirectory(File directory) throws IOException {
		if (directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					Files.delete(file.toPath());
				}
			}
		}

		Files.deleteIfExists(directory.toPath());
	}

	private Path getNext(ExtensionContext context, boolean input) {
		List<Path> files = getOrCreateFiles(context, input);
		Path dir = input ? tempInputDirectory : tempDirectory;
		String prefix = input ? "input" : "file";

		Path result;
		int i = 1;

		do {
			result = dir.resolve(String.format("%s_%d.tmp", prefix, i++));
		} while (files.contains(result));

		files.add(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<Path> getOrCreateFiles(ExtensionContext context, boolean input) {
		String key = input ? STORE_KEY_INPUT : STORE_KEY;

		return getStore(context).getOrComputeIfAbsent(key, k -> new ArrayList<Path>(), List.class);
	}

	/**
	 * @param context
	 *            {@link ExtensionContext}
	 * @return {@link ExtensionContext.Store}
	 * @since 0.2.0
	 */
	private ExtensionContext.Store getStore(ExtensionContext context) {
		return context.getStore(ExtensionContext.Namespace.create(getClass(), context));
	}
}
