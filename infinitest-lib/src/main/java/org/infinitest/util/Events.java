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
package org.infinitest.util;

import static com.google.common.collect.Lists.*;

import java.lang.reflect.*;
import java.util.*;

public class Events<T> {
	private final Method eventMethod;
	private final List<T> listeners = newArrayList();

	public Events(Method eventMethod) {
		this.eventMethod = eventMethod;
	}

	public void addListener(T listener) {
		listeners.add(listener);
	}

	public void fire(Object... eventData) {
		for (T each : listeners) {
			try {
				eventMethod.invoke(each, eventData);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void removeListener(T listener) {
		listeners.remove(listener);
	}

	public static <T> Events<T> eventFor(Class<T> listenerClass) {
		Method method = listenerClass.getDeclaredMethods()[0];
		return new Events<T>(method);
	}
}
