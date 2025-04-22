package de.voomdoon.testing.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures handling of {@link TempOutputDirectory}.
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithTempOutputDirectories {

	/**
	 * @return {@code true} results in creation of the directory
	 * @since 0.2.0
	 */
	boolean create() default false;
}
