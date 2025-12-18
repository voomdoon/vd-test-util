package de.voomdoon.testing.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects a temporary file for general use. See {@link TempFileExtension} for details.
 *
 * @author André Schulz
 * 
 * @since 0.2.0
 * @deprecated moved to {@code vd-testing-file} module
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Deprecated
public @interface TempFile {

}
