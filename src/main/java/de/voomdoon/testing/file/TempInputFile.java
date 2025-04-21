package de.voomdoon.testing.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects a temporary input file. See {@link TempFileExtension} for details.
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TempInputFile {

}
