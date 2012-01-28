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

import static org.infinitest.CoreDependencySupport.*;

import java.io.*;
import java.util.*;

import org.infinitest.changedetect.*;
import org.junit.*;

public class WhenTestFileIsRemoved {
	@Test
	public void shouldReloadIndex() throws Exception {
		InfinitestCore core = createCore(withRemovedFiles(), withNoTestsToRun());
		EventSupport eventSupport = new EventSupport();
		core.addTestQueueListener(eventSupport);
		core.update();
		eventSupport.assertReloadOccured();
	}

	private ChangeDetector withRemovedFiles() {
		return new FakeChangeDetector(Collections.<File> emptySet(), true);
	}
}
