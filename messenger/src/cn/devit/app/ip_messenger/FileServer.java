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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.net.ServerSocketFactory;

import android.util.Log;
import cn.devit.app.ip_messenger.pigeon.PigeonPacket;
import cn.devit.util.io.SerialStreamReader;

/**
 * File server send local file to remote.
 * 
 * @author lxb
 *
 */
public class FileServer implements Runnable {
	int port = 2345;

	Map<String, File> filemap = new HashMap<String, File>();

	public FileServer(int port) {
		super();
		this.port = port;
	}

	/**
	 * @param key
	 *            packetId:index
	 * @param file
	 */
	public void putFile(String key, File file) {
		this.filemap.put(key, file);
		Log.d("tcp_socket", "add file to index:" + key + "," + file);
	}

	int stopFlag = 0;
	ServerSocket serverSocket;

	public void run() {
		try {
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(
					port);
			Log.d("tcp_socket",
					"listen on socket:" + serverSocket.getLocalSocketAddress());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Log.d("tcp_socket", "listening.");
		Socket socket;
		while (stopFlag == 0) {
			try {
				socket = serverSocket.accept();
				Log.d("tcp_socket", "remote connected");
				InputStream inputStream = socket.getInputStream();
				SerialStreamReader reader = new SerialStreamReader(
						new InputStreamReader(inputStream, "utf-8"));
				reader.setTimeout(5000);
				String rawPacket = reader.readStringUntil("\0");
				PigeonPacket packet = PigeonPacket.parse(rawPacket);
				String[] keyAndOffset = packet.getMessage().split(":");
				String key = keyAndOffset[0] + ":" + keyAndOffset[1];
				long offset = Long.parseLong(keyAndOffset[2], 16);
				// TODO packet parse
				Log.d("tcp_socket", "file key=" + key + ",offset=" + offset);

				File file = filemap.get(key);
				if (file != null && file.canRead()) {
					FileInputStream fis = new FileInputStream(file);
					if (offset > 0) {
						fis.skip(offset);
					}
					byte[] buf = new byte[100];
					while (true) {
						int read = fis.read(buf);
						if (read >= 0) {
							socket.getOutputStream().write(buf, 0, read);
						} else {
							break;
						}
					}
					socket.getOutputStream().flush();
					socket.close();
					System.out.println("send done, close.");
				} else {
					System.out.println("not file close.");
					socket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
