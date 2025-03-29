package de.voomdoon.testing.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for test parameters that should be injected with a temporary file.
 * <p>
 * This annotation is used in conjunction with the {@link TempFileExtension}, which resolves parameters annotated with
 * {@code @TempFile} by injecting a unique, non-existing file located in a test-specific temporary directory. The
 * temporary files are automatically cleaned up after each test execution.
 * </p>
 * 
 * <p>
 * Supported parameter types:
 * <ul>
 * <li>{@link java.io.File}</li>
 * <li>{@link java.nio.file.Path}</li>
 * </ul>
 * </p>
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
 * @see TempFileExtension
 * 
 * @author André Schulz
 * 
 * @since 0.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TempFile {

}
