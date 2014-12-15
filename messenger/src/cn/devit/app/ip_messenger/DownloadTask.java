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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import cn.devit.app.ip_messenger.pigeon.AttachementLink;
import cn.devit.app.ip_messenger.pigeon.PigeonCommand;
import cn.devit.app.ip_messenger.pigeon.PigeonMessage;

/**
 * <p>
 * <ul>
 * <li>message and attachment
 * <li>total length of file;
 * <li>total number of files;
 * 
 * @author lxb
 */
public class DownloadTask extends AsyncTask<PigeonMessage, Long, Integer> {

	NetworkMonitor network;

	Activity mContext;

	public DownloadTask(NetworkMonitor network, Activity context) {
		super();
		this.network = network;
		this.mContext = context;
	}

	/**
	 * Store all files' total byte size.
	 */
	long size = 0;

	@Override
	protected Integer doInBackground(PigeonMessage... params) {
		long got = 0;// store all file's byte size.
		int count = 0;// store number of files received.
		for (PigeonMessage item : params) {
			for (int i = 0; i < item.getAttachements().size(); i++) {
				size += item.getAttachements().get(i).getLength();
			}
		}
		Log.d("main", "total bytes of file:" + size);
		// TODO 计算总大小放在上层，这里就调用publish
		for (PigeonMessage item : params) {
			for (int i = 0; i < item.getAttachements().size(); i++) {
				noteId = item.hashCode();
				AttachementLink link = item.getAttachements().get(i);
				if (link.getType() == PigeonCommand.IPMSG_FILE_REGULAR) {
					count++;
					Socket socket = null;
					try {
						socket = network.getAttachementStream(item, i);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					InputStream stream = null;
					try {
						stream = socket.getInputStream();
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					File download = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					String filename = link.getFilename();
					File file = new File(download, filename);
					if (file.exists()) {
						file.delete();
					} else {
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Log.d("main", "download file:" + file.getName());
					try {
						FileOutputStream fos = new FileOutputStream(file);
						byte[] cache = new byte[1024];
						try {
							int len = 0;
							while ((len = stream.read(cache)) >= 0) {
								got += len;

								fos.write(cache, 0, len);
								this.publishProgress(got);
							}
							fos.close();
							stream.close();
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}

				// try {
				// } catch (IOException e) {
				// if(socket!=null){
				// socket.
				// }
				// }

			}
		}

		// TODO Auto-generated method stub
		return count;
	}

	int noteId = 0;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mContext.setProgressBarVisibility(true);
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		Log.d("main", "got " + values[0]);
		// progress is from 0 to 10000,
		mContext.setProgress((int) ((float) values[0] / size * 10000));

		// NotificationManager notificationManager = (NotificationManager)
		// mContext
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification notification = new NotificationCompat.Builder(mContext)
		// .setAutoCancel(true)
		// .setProgress((int) size, values[0].intValue(), false).build();
		// notificationManager.notify(noteId, notification);
	}

	@Override
	protected void onPostExecute(Integer result) {
		mContext.setProgress(10000);
		Toast.makeText(mContext, "下载完成，共" + result + "个文件。", Toast.LENGTH_SHORT)
				.show();
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(noteId);

	}

}
