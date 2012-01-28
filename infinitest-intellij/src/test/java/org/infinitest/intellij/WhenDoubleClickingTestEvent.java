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
package org.infinitest.intellij;

import static org.hamcrest.Matchers.*;
import static org.infinitest.testrunner.TestEvent.TestState.*;
import static org.junit.Assert.*;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.infinitest.intellij.plugin.swingui.*;
import org.infinitest.testrunner.*;
import org.junit.*;

public class WhenDoubleClickingTestEvent {
	@Test
	public void shouldNavigateToSource() {
		FakeSourceNavigator navigator = new FakeSourceNavigator();

		ResultClickListener listener = new ResultClickListener(navigator);
		listener.mouseClicked(doubleClick(eventWithError()));

		assertThat(navigator.getClassName(), is(getClass().getName()));
		assertThat(navigator.getLine(), not(0));
	}

	@SuppressWarnings("serial")
	private MouseEvent doubleClick(final TestEvent event) {
		return new MouseEvent(new JTree() {
			@Override
			public TreePath getClosestPathForLocation(int x, int y) {
				return new TreePath(event);
			}
		}, 0, 0, 0, 0, 0, 2, false);
	}

	private static TestEvent eventWithError() {
		return new TestEvent(METHOD_FAILURE, "", "", "", new Exception());
	}
}
