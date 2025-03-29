package de.voomdoon.testing.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithTempInputFiles {

	/**
	 * DOCME add JavaDoc for method extension
	 * 
	 * @return
	 * @since 0.2.0
	 */
	String extension() default "tmp";
}
