/*
 * This file is part of Infinitest.
 *
 * Copyright (C) 2010
 * "Ben Rady" <benrady@gmail.com>,
 * "Rod Coffin" <rfciii@gmail.com>,
 * "Ryan Breidenbach" <ryan.breidenbach@gmail.com>, et al.
 *
 * Infinitest is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Infinitest is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Infinitest.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.infinitest;

import static com.google.common.collect.Lists.*;
import static java.util.Collections.*;

import java.io.*;
import java.util.*;

import com.google.common.base.*;
import com.google.common.io.*;

public class FileCustomJvmArgumentReader implements CustomJvmArgumentsReader {
	public static final String FILE_NAME = "infinitest.args";

	private final File parentDirectory;

	public FileCustomJvmArgumentReader(File parentDirectory) {
		this.parentDirectory = parentDirectory;
	}

	public List<String> readCustomArguments() {
		File file = new File(parentDirectory, FILE_NAME);
		if (!file.exists()) {
			return emptyList();
		}

		try {
			List<String> lines = Files.readLines(file, Charsets.UTF_8);
			return buildArgumentList(lines);
		} catch (IOException e) {
			return emptyList();
		}
	}

	private List<String> buildArgumentList(List<String> lines) {
		List<String> arguments = newArrayList();
		for (String line : lines) {
			for (String arg : line.split(" ")) {
				arguments.add(arg);
			}
		}

		return arguments;
	}
}
