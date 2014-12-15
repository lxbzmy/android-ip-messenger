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
package cn.devit.app.ip_messenger;

import static cn.devit.app.ip_messenger.pigeon.PigeonCommand.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
import cn.devit.app.ip_messenger.pigeon.AttachementLink;
import cn.devit.app.ip_messenger.pigeon.IPacketBuilder.PacketSetupStep1;
import cn.devit.app.ip_messenger.pigeon.IPacketBuilder.PacketSetupStep3;
import cn.devit.app.ip_messenger.pigeon.IpMessageProtocol;
import cn.devit.app.ip_messenger.pigeon.MessageImpl;
import cn.devit.app.ip_messenger.pigeon.PacketBuilder;
import cn.devit.app.ip_messenger.pigeon.PacketListener;
import cn.devit.app.ip_messenger.pigeon.PigeonCommand;
import cn.devit.app.ip_messenger.pigeon.PigeonMessage;
import cn.devit.app.ip_messenger.pigeon.PigeonPacket;
import cn.devit.app.ip_messenger.pigeon.UserData;

/**
 * 监听端口，收发信息，事件回调。
 * <p>
 * - 从首选项传入设置参数 - 异常输出到上级
 * 
 * @author lxb
 *
 */
/**
 * @author lxb
 *
 */
public class NetworkMonitor implements Runnable {

	/**
	 * Store every user reference.
	 */
	Set<UserData> userList = new HashSet<UserData>(10);

	public static final String debug_tag = "udp_socket";

	/**
	 * UDP socket for IPmessenger.
	 */
	DatagramSocket socket = null;

	/**
	 * network param for UDP communication.
	 */
	SocketParam param;

	/**
	 * 
	 */
	PacketListener packetListener;

	public void setPacketListener(PacketListener packetListener) {
		this.packetListener = packetListener;
	}

	/**
	 * listener for container.
	 */
	NetworkActivityListener listener;

	FileServer fileServer;

	public void bind() {
		try {
			if (socket == null) {
				socket = new DatagramSocket(param.port);
				Log.i(debug_tag, "open and listen on UDP port:" + param.port);
			}
			listener.onBindOk();
			onBind();
		} catch (SocketException e) {
			listener.onBindFailure(e);
			Log.e(debug_tag, "can't listen on UDP port:" + param.port);
		}
		// start file server thread.
		fileServer = new FileServer(param.port);
		new Thread(fileServer).start();
	}

	byte[] receive;

	/**
	 * Create buffer,and publish online.
	 */
	void onBind() {
		receive = new byte[1024];
		receivePacket = new DatagramPacket(receive, 1024);
		noticeOnline();
	}

	public void clear() {
		receive = null;
		receivePacket = null;
		socket.close();
	}

	boolean quit = false;

	private DatagramPacket receivePacket;

	@Override
	public void run() {
		// run thread,first open port and listen.
		this.bind();
		while (quit == false) {
			if (!socket.isClosed()) {
				try {
					// this is block mode.
					socket.receive(receivePacket);
				} catch (IOException e) {
					if (e instanceof SocketException) {
						// TODO network problem
					}
					e.printStackTrace();
				}
				if (receivePacket.getLength() > 0) {
					String string = new String(receive,
							receivePacket.getOffset(),
							receivePacket.getLength(), param.charset);
					if (packetListener != null) {
						packetListener.onReceive(string,
								(InetSocketAddress) receivePacket
										.getSocketAddress(), param.charset);
					}
					doPacket(string,
							(InetSocketAddress) receivePacket
									.getSocketAddress());
					Log.d(debug_tag, "receive:" + string);
				}
			}
		}
		// if (socket != null) {
		Log.d(debug_tag, "trun off udp port 2425.");
		clear();
		// }
	}

	/**
	 * Parse packet and do command job.
	 * 
	 * @param bin
	 *            String encode from byte array.
	 * @param scoketAddress
	 *            The address where packet send from.
	 */
	void doPacket(String bin, InetSocketAddress socketAddress) {

		// receive:1:1407839300:lee:LIYONG:6291459:lee����
		// 09-10 11:30:47.700: D/udp_socket(14452):
		// receive:1:1409839254:lxb:PC201308010918:288:fds��
		PigeonPacket packet = PigeonPacket.parse(bin);
		switch (packet.getCommand() & 0xff) {
		case PigeonCommand.IPMSG_BR_ENTRY:
			UserData user = new UserData(packet.getUsername(),
					packet.getHostname());
			String message;
			String[] split;
			if (packet.getMessage() != null) {
				message = packet.getMessage();
				split = message.split(":");
				if (split.length > 1) {
					user.setGroup(split[1]);
				}
			}
			user.setAddress(socketAddress);
			addUser(user);

			// TODO 忘记干什么用了。
			PacketBuilder.version().user(param.user)
					.command(PigeonCommand.IPMSG_ANSENTRY)
					.message(param.user.getUsername())
					.message(param.user.getGroup()).toString();
			break;
		case PigeonCommand.IPMSG_ANSENTRY:
			user = new UserData(packet.getUsername(), packet.getHostname());

			message = packet.getMessage();
			split = message.split(":");
			if (split.length > 1) {
				user.setGroup(split[1]);
			}
			user.setAddress(socketAddress);
			addUser(user);
			break;
		case IPMSG_SENDMSG:// 收到发送过来的消息。
			message = packet.getMessage();
			List<AttachementLink> list = null;
			UserData from = getUser(packet.getUsername(), packet.getHostname(),
					socketAddress);

			// 回复收到消息。
			if ((packet.getCommand() & IPMSG_FLAG_SENDCHECKOPT) == IPMSG_FLAG_SENDCHECKOPT) {
				// 构造通报收到消息报文
				sendUdpData(
						PacketBuilder.version().user(this.param.user)
								.command(IPMSG_RECVMSG)
								.message(packet.getPacketNumber()).toString(),
						socketAddress.getAddress(), socketAddress.getPort());
			}
			// 构造附件链接
			if ((packet.getCommand() & IPMSG_FILEATTACHOPT) == IPMSG_FILEATTACHOPT) {
				String[] splits = message.split("\0");
				list = new ArrayList<AttachementLink>();
				String a = splits[1];
				String[] attaches = a.split("\7");// same as \a BELL 0x7
				for (String item : attaches) {
					String[] segments = item.split(":");
					AttachementLink link = new AttachementLink(
							Integer.parseInt(segments[0]), segments[1],
							Long.parseLong(segments[2], 16),
							Long.parseLong(segments[3], 16),
							Integer.parseInt(segments[4]));
					list.add(link);
				}
				MessageImpl messageImpl = new MessageImpl(
						packet.getPacketNumber(), from, splits[0], list);
				onMessage(messageImpl);
			} else {
				MessageImpl messageImpl = new MessageImpl(
						packet.getPacketNumber(), from, message, null);
				onMessage(messageImpl);
			}
		}
	}

	private UserData getUser(String username, String hostname,
			InetSocketAddress socketAddress) {
		// equal.
		UserData tmp = new UserData(username, hostname);
		tmp.setAddress(socketAddress);
		for (UserData item : userList) {
			if (item.equals(tmp)
					|| (item.getAddress() != null && item.getAddress().equals(
							socketAddress))) {
				return item;
			}
		}
		// a new user not exists in address book.
		userList.add(tmp);
		addUser(tmp);
		return tmp;
	}

	private void onMessage(MessageImpl message) {
		this.listener.onMessage(message);

	}

	private void addUser(UserData user) {
		if (userList.add(user)) {
			Log.d(debug_tag, "add user meta " + user + ":" + user.getAddress());
			this.listener.onAddUser(user);
		} else {
			// duplicate user event.
			Log.d(debug_tag, "duplicate user " + user);
		}
	}

	/**
	 * 发送上线广播
	 */
	public void noticeOnline() {
		String m = PacketBuilder.version().user(param.user)
				.command(PigeonCommand.IPMSG_BR_ENTRY)
				.message(param.user.getUsername())
				.message(param.user.getGroup()).toString();
		sendUdpData(m, param.broadcastAddresses.get(0), PigeonCommand.PORT);
	}

	public void noticeOffline() {
		String m = PacketBuilder.version().user(param.user)
				.command(PigeonCommand.IPMSG_BR_EXIT)
				.message(param.user.getUsername())
				.message(param.user.getGroup()).toString();
		sendUdpData(m, param.broadcastAddresses.get(0), param.port);
	}

	public synchronized void sendUdpData(String sendStr, InetAddress sendto,
			int sendPort) {
		if (packetListener != null) {
			packetListener.onSend(sendStr, new InetSocketAddress(sendto,
					sendPort), param.charset);
		}
		DatagramPacket udpSendPacket;
		byte[] bin = sendStr.getBytes(param.charset);
		udpSendPacket = new DatagramPacket(bin, bin.length, sendto, sendPort);
		try {
			socket.send(udpSendPacket);
			Log.i(debug_tag, "sending udp to" + sendto.getHostAddress()
					+ ",data:" + sendStr);
			udpSendPacket = null;
		} catch (IOException e) {
			udpSendPacket = null;
			Log.e(debug_tag, "send failure", e);
		}
	}

	/**
	 * close socket.
	 */
	public void quit() {
		this.quit = true;
		noticeOffline();
		// TODO add synchronized to all socket.
		synchronized (socket) {
			socket.close();
		}
		try {
			fileServer.stopFlag = 1;
			fileServer.serverSocket.close();
		} catch (IOException e) {
		}
	}

	public void sendMessage(UserData[] userList, String message) {
		String m = PacketBuilder.version().user(param.user)
				.command(PigeonCommand.IPMSG_SENDMSG).message(message)
				.toString();
		for (UserData item : userList) {
			Log.d(debug_tag, "send '" + message + "' to " + item.getAddress()
					+ ":" + item.getAddress());
			sendUdpData(m, item.getAddress().getAddress(), item.getAddress()
					.getPort());
		}
	}

	public void sendMessage(UserData[] userList, String message,
			List<File> attachements) {
		// TODO packet sequence number generator.
		// TODO send attachements.!!
		PacketSetupStep1 b = PacketBuilder.version();
		String seq = Long.toHexString(b.getPacketNumber());
		int command = PigeonCommand.IPMSG_SENDMSG;
		if (attachements != null && attachements.size() > 0) {
			command = command | PigeonCommand.IPMSG_FILEATTACHOPT;
		}
		
		PacketSetupStep3 b2 = b.user(param.user).command(command)
				.message(message);
		

		String attach = null;
		if (attachements != null && attachements.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < attachements.size(); i++) {
				// fileID:filename:size:mtime:fileattr[:extend-attr=val1
				// [,val2...][:extend-attr2=...]]:\a:fileID...
				File item = attachements.get(i);
				System.out.println("attach:"+i+",name="+item.getName()+",length="+item.length()+",readable="+item.canRead());
				try{
					InputStream is = new FileInputStream(item);
					while(is.read()!=-1){
						//nop
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				sb.append(i).append(":").append(item.getName()).append(":")
						.append(Long.toHexString(item.length())).append(":")
						.append(Long.toHexString(item.lastModified())).append(":").append(PigeonCommand.IPMSG_FILE_REGULAR).append("\7");
				fileServer.putFile(seq + ":" + i, item);
			}
			sb.deleteCharAt(sb.length() - 1);// remove last append split char
												// BEL
			attach = sb.toString();
			b2.message(attach);
		}

		String m = b2.toString();

		for (UserData item : userList) {
			Log.d(debug_tag, "send '" + message + " with attach " + attachements+ "' to " + item.getAddress()
					+ ":" + item.getAddress());
			sendUdpData(m, item.getAddress().getAddress(), item.getAddress()
					.getPort());
		}
	}

	/**
	 * Connection to remote user and request a attachment. TODO change return
	 * value more safe.
	 * 
	 * @param message
	 * @param i
	 *            attachment index to get.
	 * @return
	 * @throws IOException
	 */
	public Socket getAttachementStream(PigeonMessage message, int i)
			throws IOException {
		// When Receive Message
		// * downloads an attachment file, an IPMSG_GETFILEDATA command requests
		// a
		// * data transmission packet to the TCP port that is the same number as
		// the
		// * UDP sending port number. Input packetID:fileID:offset to the
		// extended
		// * area. (Use all hex format.) File Transfer side receives the
		// request.
		// * After recognizing that it's a correct request, then send the
		// specified
		// * data (no format)
		Socket socket = new Socket(message.getSender().getAddress()
				.getAddress(), message.getSender().getAddress().getPort());
		Log.d(debug_tag, "connect to tcp port:"
				+ message.getSender().getAddress());
		AttachementLink link = message.getAttachements().get(i);
		if (link.getType() == IPMSG_FILE_REGULAR) {
			PacketSetupStep3 pack = PacketBuilder
					.version()
					.user(this.param.user)
					.command(IPMSG_GETFILEDATA)
					.message(
							Long.toHexString(Long.parseLong(message.getId()))
									+ ":" + i + ":0");
			Log.d(debug_tag, "send command:" + pack.toString());
			OutputStream os = new BufferedOutputStream(socket.getOutputStream());
			os.write(pack.bytes(param.charset));
			os.flush();
			// InputStream inputStream = socket.getInputStream();
			return socket;
		} else {
			return null;
			// TODO not support dir and parent.
		}

	}

	/**
	 * Reject receive attachment.
	 * 
	 * @param m
	 */
	public void rejectAttachement(PigeonMessage m) {
		String a = PacketBuilder.version().user(this.param.user)
				.command(IPMSG_RELEASEFILES).message(m.getId()).toString();
		sendUdpData(a, m.getSender().getAddress().getAddress(), m.getSender()
				.getAddress().getPort());
	}

}
