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
package org.infinitest.eclipse;

import static java.util.Collections.*;
import static org.infinitest.eclipse.InfinitestCoreClasspath.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.*;
import org.osgi.framework.*;

public class WhenCreatingCoreClasspath {
	private InfinitestPlugin plugin;

	@Before
	public void inContext() {
		Bundle bundle = mock(Bundle.class);
		List<URL> urls = Arrays.asList(getClass().getResource("WhenCreatingCoreClasspath.class"));
		when(bundle.findEntries("", "*infinitest-runner*.jar", true)).thenReturn(enumeration(urls));
		plugin = new InfinitestPlugin();
		plugin.setPluginBundle(bundle);
	}

	@Test
	public void shouldWriteInfinitestCoreOutToTempDirectory() {
		File coreJarLocation = getCoreJarLocation(plugin);
		assertTrue(coreJarLocation.exists());
		assertTrue(coreJarLocation.getAbsolutePath().endsWith(".jar"));
	}

	@Test
	public void shouldRecreateJarIfItIsDeleted() {
		File coreJarLocation = getCoreJarLocation(plugin);
		assertTrue(coreJarLocation.exists());
		assertTrue(coreJarLocation.getAbsolutePath().endsWith(".jar"));
		assertTrue(coreJarLocation.delete());

		coreJarLocation = getCoreJarLocation(plugin);
		assertTrue(coreJarLocation.exists());
	}
}
