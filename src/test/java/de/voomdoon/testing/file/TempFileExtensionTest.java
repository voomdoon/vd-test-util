package de.voomdoon.testing.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.Mockito;

import de.voomdoon.testing.tests.TestBase;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.2.0
 */
class TempFileExtensionTest {

	/**
	 * DOCME add JavaDoc for TempFileExtensionTest
	 *
	 * @author André Schulz
	 *
	 * @since 0.2.0
	 */
	@Nested
	class AfterEachTest extends TestBase {

		public void dummyMethod(@TempFile File file) {
		}

		/**
		 * DOCME add JavaDoc for method test_File_isDeleted
		 * 
		 * @since 0.2.0
		 */
		@Test
		void test_File_isDeleted() throws Exception {
			logTestStart();

			TempFileExtension extension = new TempFileExtension();

			ParameterContext parameterContext = mock(ParameterContext.class);
			ExtensionContext extensionContext = mock(ExtensionContext.class);
			ExtensionContext.Store store = mock(ExtensionContext.Store.class);
			when(extensionContext.getStore(any())).thenReturn(store);

			List<Path> trackedFiles = new ArrayList<>();

			when(store.get(Mockito.eq(TempFileExtension.STORE_KEY), Mockito.eq(List.class))).thenReturn(trackedFiles);
			when(store.getOrComputeIfAbsent(Mockito.eq(TempFileExtension.STORE_KEY), any(), Mockito.eq(List.class)))
					.thenReturn(trackedFiles);

			Parameter parameter = AfterEachTest.class.getDeclaredMethod("dummyMethod", File.class).getParameters()[0];

			when(parameterContext.getParameter()).thenReturn(parameter);
			when(parameterContext.isAnnotated(TempFile.class)).thenReturn(true);

			File file = (File) extension.resolveParameter(parameterContext, extensionContext);

			file.createNewFile();

			extension.afterEach(extensionContext);

			assertThat(file).doesNotExist();
		}

		/**
		 * DOCME add JavaDoc for method test_File_isDeleted
		 * 
		 * @since 0.2.0
		 */
		@Test
		void test_noFile_passes() throws Exception {
			logTestStart();

			TempFileExtension extension = new TempFileExtension();

			ParameterContext parameterContext = mock(ParameterContext.class);
			ExtensionContext extensionContext = mock(ExtensionContext.class);
			ExtensionContext.Store store = mock(ExtensionContext.Store.class);
			when(extensionContext.getStore(any())).thenReturn(store);

			List<Path> trackedFiles = new ArrayList<>();

			when(store.get(Mockito.eq(TempFileExtension.STORE_KEY), Mockito.eq(List.class))).thenReturn(trackedFiles);
			when(store.getOrComputeIfAbsent(Mockito.eq(TempFileExtension.STORE_KEY), any(), Mockito.eq(List.class)))
					.thenReturn(trackedFiles);

			Parameter parameter = AfterEachTest.class.getDeclaredMethod("dummyMethod", File.class).getParameters()[0];

			when(parameterContext.getParameter()).thenReturn(parameter);
			when(parameterContext.isAnnotated(TempFile.class)).thenReturn(true);

			assertDoesNotThrow(() -> extension.afterEach(extensionContext));
		}
	}
}
