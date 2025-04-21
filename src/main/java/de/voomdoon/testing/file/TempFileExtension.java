package de.voomdoon.testing.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	private enum FileType {

		/**
		 * @since 0.2.0
		 */
		DEFAULT,

		/**
		 * @since 0.2.0
		 */
		INPUT,

		/**
		 * @since 0.2.0
		 */
		OUTPUT,

		;
	}

	public static final String STORE_KEY = "temp-files";

	/**
	 * @since 0.2.0
	 */
	private static final Map<FileType, String> STORE_KEYS = Map.of(FileType.DEFAULT, STORE_KEY, FileType.INPUT,
			STORE_KEY + "/input", FileType.OUTPUT, STORE_KEY + "/output");

	/**
	 * @since 0.2.0
	 */
	private Path tempDirectory = Path.of("target", "test-temp");

	/**
	 * @since 0.2.0
	 */
	private Path tempInputDirectory = tempDirectory.resolve("input/0");

	/**
	 * @since 0.2.0
	 */
	private Path tempOutputDirectory = tempDirectory.resolve("output/0");

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
		FileType fileType = getFileType(parameterContext);
		Path file = getNext(extensionContext, fileType);

		Path baseDir = getDirectoryFor(fileType);

		try {
			Files.createDirectories(baseDir);
		} catch (IOException e) {
			throw new ParameterResolutionException("Failed to create temp directory: " + baseDir, e);
		}

		Class<?> type = parameterContext.getParameter().getType();

		if (File.class.equals(type)) {
			return file.toFile();
		} else if (Path.class.equals(type)) {
			return file;
		} else if (String.class.equals(type)) {
			return file.toString();
		}

		throw new ParameterResolutionException("Unsupported parameter type: " + type);
	}

	/**
	 * @since 0.2.0
	 */
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return supportsType(parameterContext) && supportsAnnotation(parameterContext);
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

	/**
	 * @param context
	 *            {@link ExtensionContext}
	 * @param type
	 *            {@link FileType}
	 * @return {@link String}
	 * @since 0.2.0
	 */
	private String getConfiguredFileNameExtension(ExtensionContext context, FileType type) {
		Class<?> testClass = context.getRequiredTestClass();

		return switch (type) {
			case INPUT -> {
				WithTempInputFiles config = testClass.getAnnotation(WithTempInputFiles.class);
				yield (config != null) ? config.extension() : "tmp";
			}
			case OUTPUT -> {
				WithTempOutputFiles config = testClass.getAnnotation(WithTempOutputFiles.class);
				yield (config != null) ? config.extension() : "tmp";
			}
			default -> "tmp";
		};
	}

	/**
	 * @param type
	 *            {@link FileType}
	 * @return {@link Path}
	 * @since 0.2.0
	 */
	private Path getDirectoryFor(FileType type) {
		return switch (type) {
			case INPUT -> tempInputDirectory;
			case OUTPUT -> tempOutputDirectory;
			default -> tempDirectory;
		};
	}

	/**
	 * @param parameterContext
	 *            {@link ParameterContext}
	 * @return {@link FileType}
	 * @since 0.2.0
	 */
	private FileType getFileType(ParameterContext parameterContext) {
		if (parameterContext.isAnnotated(TempInputFile.class)) {
			return FileType.INPUT;
		} else if (parameterContext.isAnnotated(TempOutputFile.class)) {
			return FileType.OUTPUT;
		}

		return FileType.DEFAULT;
	}

	/**
	 * @param context
	 *            {@link ExtensionContext}
	 * @param type
	 *            {@link FileType}
	 * @return {@link Path}
	 * @since 0.2.0
	 */
	private Path getNext(ExtensionContext context, FileType type) {
		List<Path> files = getOrCreateFiles(context, type);
		Path dir = getDirectoryFor(type);

		String prefix = switch (type) {
			case INPUT -> "input";
			case OUTPUT -> "output";
			default -> "file";
		};

		String extension = getConfiguredFileNameExtension(context, type);

		Path result;
		int i = 1;

		do {
			String fileName = String.format("%s_%d.%s", prefix, i++, extension);
			result = dir.resolve(fileName);
		} while (files.contains(result));

		files.add(result);

		return result;
	}

	/**
	 * DOCME add JavaDoc for method getOrCreateFiles
	 * 
	 * @param context
	 *            {@link ExtensionContext}
	 * @param type
	 *            {@link FileType}
	 * @return {@link List} of all files as {@link Path}
	 * @since 0.2.0
	 */
	private List<Path> getOrCreateFiles(ExtensionContext context, FileType type) {
		String key = STORE_KEYS.get(type);

		@SuppressWarnings("unchecked")
		List<Path> result = getStore(context).getOrComputeIfAbsent(key,
				k -> Collections.synchronizedList(new ArrayList<>()), List.class);

		return result;
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

	/**
	 * @param parameterContext
	 *            {@link ParameterContext}
	 * @return
	 * @since 0.2.0
	 */
	private boolean supportsAnnotation(ParameterContext parameterContext) {
		boolean isAnnotated = //
				parameterContext.isAnnotated(TempFile.class) //
						|| parameterContext.isAnnotated(TempInputFile.class)//
						|| parameterContext.isAnnotated(TempOutputFile.class);
		return isAnnotated;
	}

	/**
	 * @param parameterContext
	 *            {@link ParameterContext}
	 * @return
	 * @since 0.2.0
	 */
	private boolean supportsType(ParameterContext parameterContext) {
		boolean isSupportedType = //
				File.class.isAssignableFrom(parameterContext.getParameter().getType())
						|| Path.class.isAssignableFrom(parameterContext.getParameter().getType())
						|| String.class.isAssignableFrom(parameterContext.getParameter().getType());
		return isSupportedType;
	}
}
