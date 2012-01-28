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

import static com.google.common.collect.Lists.*;
import static org.infinitest.testrunner.TestEvent.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.infinitest.eclipse.markers.*;
import org.infinitest.eclipse.workspace.*;
import org.infinitest.testrunner.*;
import org.junit.*;
import org.mockito.*;

public class FailureMediatorTest {
	private FailureMediator mediator;
	private ProblemMarkerRegistry registry;
	private TestEvent event1;
	private TestEvent event2;
	private TestEvent event3;
	private FakeResourceFinder finder;

	@Before
	public void inContext() {
		registry = Mockito.mock(ProblemMarkerRegistry.class);
		finder = new FakeResourceFinder();
		mediator = new FailureMediator(registry, finder);
		event1 = event("method1");
		event2 = event("method1");
		event3 = event("method1");
	}

	@Test
	public void shouldRelayAddRemoveEvents() {
		Collection<TestEvent> failuresAdded = newArrayList(event1, event2);
		Collection<TestEvent> failuresRemoved = newArrayList(event3);

		mediator.failureListChanged(failuresAdded, failuresRemoved);

		verify(registry, times(2)).addMarker(any(MarkerInfo.class));
		ProblemMarkerInfo markerInfo = new ProblemMarkerInfo(event3, finder);
		verify(registry).removeMarker(markerInfo);
	}

	@Test
	public void shouldRelayUpdateEvents() {
		Collection<TestEvent> updatedFailures = newArrayList(event1, event2);

		mediator.failuresUpdated(updatedFailures);

		verify(registry, times(2)).updateMarker(any(MarkerInfo.class));
	}

	private TestEvent event(String methodName) {
		return methodFailed("testClass", methodName, new AssertionError());
	}
}
