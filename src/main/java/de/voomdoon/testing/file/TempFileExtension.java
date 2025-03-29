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
 * DOCME add JavaDoc for
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
	private Path tempDirectory = Path.of("target", "test-temp");

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
		Path tempFile = getNext(extensionContext);

		try {
			Files.createDirectories(tempDirectory);
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'resolveParameter': " + e.getMessage(), e);
		}

		if (parameterContext.getParameter().getType().equals(File.class)) {
			return tempFile.toFile();
		} else if (parameterContext.getParameter().getType().equals(Path.class)) {
			return tempFile;
		} else {
			// TODO implement resolveParameter
			throw new UnsupportedOperationException("Method 'resolveParameter' not implemented yet");
		}
	}

	/**
	 * @since 0.2.0
	 */
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.isAnnotated(TempFile.class)
				&& (File.class.isAssignableFrom(parameterContext.getParameter().getType())
						|| Path.class.isAssignableFrom(parameterContext.getParameter().getType()));
	}

	/**
	 * @param directory
	 *            {@link File}
	 * @since 0.2.0
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
	 * @param extensionContext
	 *            {@link ExtensionContext}
	 * 
	 * @return {@link Path}
	 * @since 0.2.0
	 */
	private Path getNext(ExtensionContext extensionContext) {
		List<Path> tempFiles = getOrCreateTempFiles(extensionContext);
		Path result;
		int i = 1;

		do {
			String name = String.format("file_%d.tmp", i++);
			result = tempDirectory.resolve(name);
		} while (tempFiles.contains(result));

		tempFiles.add(result);

		return result;
	}

	/**
	 * @param extensionContext
	 *            {@link ExtensionContext}
	 * @return {@link List} of {@link Path}
	 * @since 0.2.0
	 */
	@SuppressWarnings("unchecked")
	private List<Path> getOrCreateTempFiles(ExtensionContext extensionContext) {
		return getStore(extensionContext).getOrComputeIfAbsent(STORE_KEY, k -> new ArrayList<Path>(), List.class);
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
