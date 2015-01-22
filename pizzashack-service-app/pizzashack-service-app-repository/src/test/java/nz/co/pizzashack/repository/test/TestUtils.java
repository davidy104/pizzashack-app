package nz.co.pizzashack.repository.test;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class TestUtils {

	public static String readFileAsString(final String fileClasspath) {
		final File file = new File(Resources.getResource(fileClasspath).getFile());
		try {
			return Files.toString(file, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
