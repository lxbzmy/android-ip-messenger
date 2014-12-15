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
import java.nio.charset.Charset;


/**
 * This class's prototype came from Arduino's Stream.h(some methods combined ,in java it is not useful.),
 * It is good to use in serial device communication.
 * @author lxb
 *
 */
public interface ISerialStreamUtil {

	/**
	 * sets maximum milliseconds to wait for stream data, default is 1 second
	 * @param timeout
	 */
	public void setTimeout(int timeout);

	/**
	 * set character set of this reader for readString or readChar operation.
	 * ,It is not affect on String parameters pass to this function.
	 * @param charset
	 */
	public void setCharset(Charset charset);

	/**
	 * reads data from the stream until the target string is found
	 * @param target
	 * @return true if target string is found, false if timed out (@see {@link #setTimeout(int)})
	 * @throws IOException
	 *
	 */
	public boolean find(byte[] target) throws IOException;

	/**
	 * as {@link #find(String)}but search ends if the terminator string is found
	 * @return true if target is found,false when timeout or terminator found.
	 * @throws IOException
	 */
	public boolean findUntil(byte[] target, byte[]terminator) throws IOException;


	/**
	 * read byte from stream into buffer
	 * terminates if buffer.length characters have been read or timeout (see setTimeout)
	 * returns the number of characters placed in the buffer (0 means no valid data found)
	 */
	int readBytes(byte[] buffer);

	/**
	 *  as readBytes with terminator character
	 *  terminates if buffer.length characters have been read, timeout, or if the terminator character  detected
	 *  returns the number of characters placed in the buffer (0 means no valid data found)
	 */
	int readBytesUntil(byte terminator, byte[] buffer);

	/**
	 * read string from input,until timeout.
	 * @return String read or blank string when timeout.
	 */
	String readString();

	/**
	 *
	 * @param terminator
	 * @return String reads or blank string when timeout or terminator found.
	 */
	String readStringUntil(byte terminator);



}
