/*
 * Copyright (c) 2014 lxb<lxbzmy@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.devit.util.io;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lxb
 *
 */
public class SerialStreamReader implements IStreamReader {

	Logger logger;
	int timeout = 1000;
	private InputStreamReader reader;

	private String lastBuffer;



	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public SerialStreamReader(InputStreamReader reader) {
		logger = Logger.getLogger(getClass().getName());
		this.reader = reader;
	}

	@Override
	public boolean find(String target) throws IOException {
		return findUntil(target, null);
	}

	@Override
	public boolean findUntil(String target, String terminator)
			throws IOException {
		long point = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[16];
		while (true && (System.currentTimeMillis() - point) < timeout) {
			if (reader.ready()) {
				int l = reader.read(buffer);
				sb.append(buffer, 0, l);
				if (sb.indexOf(target) > -1) {
					if (logger.isLoggable(Level.FINE)) {
						logger.fine(sb.toString());
					}
					lastBuffer = sb.toString();
					return true;
				} else if ((terminator != null && sb.indexOf(terminator) > -1)) {
					break;
				}
				point = System.currentTimeMillis();// update timestamp.
			}
		}
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(sb.toString());
		}
		lastBuffer = sb.toString();
		return false;
	}

	@Override
	public String readString() throws IOException {
		return readStringUntil(null);
	}

	@Override
	public String readStringUntil(String terminator) throws IOException {
		int bufferLen = 8;
		if (terminator != null && terminator.length() > 0) {
			bufferLen = terminator.length();
		}
		long point = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[bufferLen];
		while (true && (System.currentTimeMillis() - point) < timeout) {
			if (reader.ready()) {
				int l = reader.read(buffer);
				sb.append(buffer, 0, l);
				if (terminator != null && sb.indexOf(terminator) > -1) {
					if (logger.isLoggable(Level.FINE)) {
						logger.fine(sb.toString());
					}
					return sb.toString().replace(terminator, "");
				}
				point = System.currentTimeMillis();// update timestamp.
			}
		}
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(sb.toString());
		}
		return sb.toString();
	}

	@Override
	public String getLastBuffer() {
		return lastBuffer;
	}

	@Override
	public String flush() throws IOException{
		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[16];
		while (reader.ready()) {
			int l = reader.read(buffer);
			sb.append(buffer, 0, l);
		}
		return sb.toString();
	}
}
