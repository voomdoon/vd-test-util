package de.voomdoon.testing.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures handling of {@link TempOutputFile}.
 * 
 * @author André Schulz
 *
 * @since 0.2.0
 * @deprecated moved to {@code vd-testing-file} module
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface WithTempOutputFiles {

	/**
	 * File extension to use for {@link TempOutputFile}s.
	 * 
	 * @return the file extension (default is {@code tmp})
	 * @since 0.2.0
	 */
	String extension() default "tmp";
}
