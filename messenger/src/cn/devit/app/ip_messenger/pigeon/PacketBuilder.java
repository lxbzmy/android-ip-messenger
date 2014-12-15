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

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import cn.devit.app.ip_messenger.pigeon.IPacketBuilder.PacketSetupStep1;
import cn.devit.app.ip_messenger.pigeon.IPacketBuilder.PacketSetupStep2;
import cn.devit.app.ip_messenger.pigeon.IPacketBuilder.PacketSetupStep3;

/**
 * Helper class to create IpMessenger UDP protocol byte array.
 * <p>
 * PacketBuilder.version()
 * 
 * @author lxb
 *
 */
public class PacketBuilder implements PacketSetupStep1, PacketSetupStep2,
		PacketSetupStep3 {

	public static String VERSION = "1";

	public static String FIELD_SPLIT = ":";
	
	static final AtomicInteger ai = new AtomicInteger(1);

	String version;
	String username;
	long seq;
	String hostName;
	int command;
	String message;

	StringBuilder sb = new StringBuilder();

	// sb.append(version);
	// sb.append(":");
	// sb.append(packetNo);
	// sb.append(":");
	// sb.append(senderName);
	// sb.append(":");
	// sb.append(senderHost);
	// sb.append(":");
	// sb.append(commandNo);
	// sb.append(":");
	// sb.append(additionalSection);

	/**
	 * Create Packet with specified version.
	 */
	public static PacketSetupStep1 version() {
		PacketBuilder builder = new PacketBuilder(VERSION.intern());
		return builder;
	}

	@Override
	public long getPacketNumber() {
		return seq;
	}

	protected PacketBuilder(String version) {
		this(version,ai.getAndIncrement());
	}
	
	protected PacketBuilder(String version,int seq) {
		super();
		this.seq = seq; 
		sb.append(version).append(FIELD_SPLIT).append(seq).append(FIELD_SPLIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.devit.app.ip_messenger.pigeon.PacketSetupSetup2#user(cn.devit.app.
	 * ip_messenger.pigeon.UserData)
	 */
	@Override
	public PacketSetupStep2 user(UserData user) {
		this.username = user.getUsername().intern();
		sb.append(username).append(FIELD_SPLIT).append(user.getHostname())
				.append(FIELD_SPLIT);
		return this;
	}

	@Override
	public PacketSetupStep3 command(int command) {
		this.command = command;
		sb.append(command).append(FIELD_SPLIT);
		return this;
	}

	@Override
	public PacketBuilder message(String message) {
		this.message = message;
		sb.append(message).append("\0");
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.devit.app.ip_messenger.pigeon.PacketSetupStep3#string()
	 */
	@Override
	public String toString() {
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.devit.app.ip_messenger.pigeon.PacketSetupStep3#bytes(java.nio.charset
	 * .Charset)
	 */
	@Override
	public byte[] bytes(Charset charset) {
		return sb.toString().getBytes(charset);
	}

}