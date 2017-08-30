package com.sycsky.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

public class ConfigUtils {

	public static InputStream getInputStream(final String fileName) throws FileNotFoundException {
		final String path = System.getProperty("app.config.path");
		if (StringUtils.isNotEmpty(path)) {
			StringBuilder sb = new StringBuilder(path.length() + fileName.length() + 1);
			sb.append(path);
			if (sb.charAt(sb.length() - 1) != File.separatorChar) {
				sb.append(File.separatorChar);
			}
			sb.append(fileName);

			File file = new File(sb.toString());
			if (file.exists()) {
				return new FileInputStream(file);
			}
		}
		return ConfigUtils.class.getClassLoader().getResourceAsStream(fileName);
	}
}
