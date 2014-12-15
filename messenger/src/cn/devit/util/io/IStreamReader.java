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


/**
 * This class's prototype came from Arduino's Stream.h(some methods combined ,in java it is not useful.),
 * It is good to use in serial device communication.
 * @author lxb
 *
 */
public interface IStreamReader {

	/**
	 * get last buffered string.useful for find or findUntil.
	 * @return last string reads by {@link #find(String)} or {@link #findUntil(String, String)},because of these methods don't return string..
	 */
	public String getLastBuffer();

	/**
	 * sets maximum milliseconds to wait for stream data, default is 1 second
	 * @param timeout
	 */
	public void setTimeout(int timeout);

	/**
	 * reads data from the stream until the target string is found
	 * @param target
	 * @return true if target string is found, false if timed out (@see {@link #setTimeout(int)})
	 * @throws IOException
	 *
	 */
	public boolean find(String target) throws IOException;

	/**
	 * as {@link #find(String)}but search ends if the terminator string is found
	 * @return true if target is found,false when timeout or terminator found.
	 * @throws IOException
	 */
	public boolean findUntil(String target, String terminator) throws IOException;

	/**
	 * read string from input,until timeout.
	 * @return String read or blank string when timeout.
	 * @throws IOException
	 */
	String readString() throws IOException;

	/**
	 * read string from inputStreamReader and return when timeout or terminator string found.
	 * be careful,you must specify a unique terminator string.otherwise the return string will truncated by terminator.
	 * also note that The Returned string will not contain terminator!<br>
	 * <pre>example:
	 * inputStream:foo\r\nOK\r\n
	 * readStringUntil("OK\r\n")
	 * return:foo\r\n
	 * </pre>
	 * @param terminator
	 * @return String reads or blank string when timeout or terminator found.
	 * @throws IOException
	 */
	String readStringUntil(String terminator) throws IOException;


	/**
	 * read available chars from input stream.
	 * @return string reads from input.
	 */
	public String flush() throws IOException;

}
