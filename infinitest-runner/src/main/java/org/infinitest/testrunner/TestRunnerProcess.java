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
package org.infinitest.testrunner;

import static org.infinitest.testrunner.TestEvent.*;

import java.io.*;
import java.net.*;

// RISK This class is only tested by running it, which is slow and throws off coverage
public class TestRunnerProcess {
	public static final String TEST_RUN_ERROR = "Error occurred during test run";
	private NativeRunner runner;

	private TestRunnerProcess(String runnerClass) {
		createRunner(runnerClass);
	}

	private static void checkForJUnit4() {
		try {
			Class.forName("org.junit.runner.notification.RunListener");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Infinitest depends on JUnit 4. Please add JUnit 4 to your project classpath.");
		}
	}

	private void createRunner(String runnerClassName) {
		runner = instantiateTestRunner(runnerClassName);
	}

	private NativeRunner instantiateTestRunner(String runnerClassName) {
		try {
			Class<?> runnerClass = Class.forName(runnerClassName);
			return (NativeRunner) runnerClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private TestResults runTest(String testName) {
		return runner.runTest(testName);
	}

	public static void main(String[] args) {
		try {
			checkForJUnit4();

			TestRunnerProcess process = new TestRunnerProcess(args[0]);
			int portNum = Integer.parseInt(args[1]);
			Socket clientSocket = new Socket("127.0.0.1", portNum);
			// DEBT Extract this to a reader class
			ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

			String testName;
			do {
				testName = (String) inputStream.readObject();

				if (testName != null) {
					writeTestResultToOutputStream(process, outputStream, testName);
				}

			} while (testName != null);

			outputStream.close();
			clientSocket.close();
		}
		// CHECKSTYLE:OFF
		catch (Throwable e)
		// CHECKSTYLE:ON
		{
			System.out.println(TEST_RUN_ERROR);
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	private static void writeTestResultToOutputStream(TestRunnerProcess process, ObjectOutputStream outputStream, String testName) throws IOException {
		TestResults results;
		try {
			results = process.runTest(testName);
		}
		// CHECKSTYLE:OFF
		catch (Throwable e)
		// CHECKSTYLE:ON
		{
			results = new TestResults(methodFailed(testName, "", e));
		}
		outputStream.writeObject(results);
	}
}
