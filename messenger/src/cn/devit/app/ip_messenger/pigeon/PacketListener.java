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
package cn.devit.app.ip_messenger.pigeon;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * A interface to debug packet send and received.
 * 
 * @author lxb
 *
 */
public interface PacketListener {

	/**
	 * When send packet to address.
	 * 
	 * @param packet
	 *            string before encode to bytes.
	 * @param address
	 *            send to.
	 * @param charset
	 */
	public void onReceive(String packet, InetSocketAddress address, Charset charset);

	/**
	 * When received packet from address.
	 * 
	 * @param packet
	 *            string decoded from byte stream.
	 * @param address
	 *            receive from
	 * @param charset
	 */
	public void onSend(String packet, InetSocketAddress address, Charset charset);

}
