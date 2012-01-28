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
package org.infinitest.eclipse.trim;

import static org.eclipse.swt.SWT.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.swt.layout.*;
import org.infinitest.eclipse.*;
import org.junit.*;

public class StatusBarTest {
	private StatusBar statusBar;

	@Before
	public void inContext() {
		statusBar = new StatusBar();
	}

	@Test
	public void shouldShrinkWhenMovedToSideBars() {
		RowData rowData = statusBar.getRowData(BOTTOM);
		assertEquals(400, rowData.width);

		rowData = statusBar.getRowData(LEFT);
		assertEquals(-1, rowData.width);
	}

	@Test
	public void shouldRegisterStatusPluginController() {
		final PluginActivationController mockController = mock(PluginActivationController.class);
		StatusBar statusBar = new StatusBar() {
			@Override
			protected PluginActivationController getPluginController() {
				return mockController;
			}
		};
		statusBar.init(null);

		verify(mockController).attachVisualStatus(statusBar);
	}
}
