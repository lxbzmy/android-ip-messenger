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

import static cn.devit.app.ip_messenger.Settings.CHARSET;
import static cn.devit.app.ip_messenger.Settings.DISPLAY_NAME;
import static cn.devit.app.ip_messenger.Settings.HOST_NAME;
import static cn.devit.app.ip_messenger.Settings.WORKGROUP_NAME;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import cn.devit.app.ip_messenger.dummy.DummyContent1;
import cn.devit.app.ip_messenger.pigeon.AttachementLink;
import cn.devit.app.ip_messenger.pigeon.PacketListener;
import cn.devit.app.ip_messenger.pigeon.PigeonMessage;
import cn.devit.app.ip_messenger.pigeon.UserData;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	public static final int ADD_USER = 1;

	public static final int RECEIVE_MSG = 2;

	public static final int PICKFILE_RESULT_CODE = 1;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	SendViewFragment sendView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		// 下载附件用的进度条。
		supportRequestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// TODO 启动界面，刷新，然后再出现列表。

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		setupListener();

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		network = new NetworkMonitor();
		network.setPacketListener(this.pl);
		network.param = new SocketParam();
		network.param.charset = Charset.forName(preferences.getString(CHARSET,
				"UTF-8"));
		network.param.port = 2425;
		network.listener = listener;
		UserData me = new UserData(
				preferences.getString(DISPLAY_NAME, "robot"),
				preferences.getString(HOST_NAME, "localhost"),
				preferences.getString(WORKGROUP_NAME, "IT"));
		// me.setAddress(new InetSocketAddress(InetAddress.getByName(""),
		// 2425));
		network.param.user = me;
		network.param.broadcastAddresses = new ArrayList<InetAddress>(1);
		try {
			network.param.broadcastAddresses.add(InetAddress
					.getByName("255.255.255.255"));
		} catch (UnknownHostException e) {
			Log.e("main", "Address lookup(255.255.255.255) failure.", e);
		}

		new Thread(network, "udp socket " + network.param.port).start();
		;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("menu_id", item.getTitle().toString());
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(getApplicationContext(),
					SettingsActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	List<File> attaches = new ArrayList<File>(2);

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("activity result:" + requestCode + ":" + resultCode);
		if (resultCode == RESULT_OK) {
			System.out.println("request code:" + requestCode);
			switch (requestCode) {
			case PICKFILE_RESULT_CODE:

			default:
				File file;
				try {
					file = new File(new URI(data.getDataString()));
					attaches.add(file);
					System.out.println("attach url:" + data.getDataString());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				break;
			}
		} else {
			System.out.println("result code is not ok.");
		}
	}

	int i = 10;

	SendViewFragment.OnFragmentInteractionListener sendFragmentInteractionListener = new SendViewFragment.OnFragmentInteractionListener() {
		@Override
		public void onSendMessage(final UserData[] user, final String message) {

			DummyContent1.addItem(String.valueOf(i++), "lxbzmy", message);
			Log.d("main",
					"send \"" + message + "\" to user " + Arrays.toString(user));
			// TODO use message.
			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("attaches:");
					System.out.println(attaches);
					if (attaches.size() == 0) {
						network.sendMessage(user, message);
					} else {
						List<File> copy = new ArrayList<File>(attaches.size());
						copy.addAll(attaches);
						attaches.clear();
						network.sendMessage(user, message, copy);
					}
				}
			}).start();
		}
	};

	private DebugLogFragment debugLogFragment;

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private SendViewFragment sendViewFragment;
		private MessageHistorykFragment historykFragment;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch (position) {
			case 0:
				if (sendViewFragment == null) {
					sendViewFragment = new SendViewFragment();
					MainActivity.this.sendView = sendViewFragment;
					MainActivity.this.sendView
							.setOnFragmentInteractionListener(sendFragmentInteractionListener);
				}
				return sendViewFragment;
			case 1:
				if (historykFragment == null) {
					historykFragment = new MessageHistorykFragment();
				}
				return historykFragment;
			case 2:
				if (debugLogFragment == null) {
					debugLogFragment = new DebugLogFragment();
				}
				return debugLogFragment;
			default:
				return new MessageHistorykFragment();
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			if (sharedPreferences.getBoolean(Settings.debug_packet, false)) {
				return 3;
			} else {
				return 2;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}

		// @Override
		// public void destroyItem(ViewGroup container, int position, Object
		// object) {
		// super.destroyItem(container, position, object);
		// }

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	// static class NetworkHandler extends Handler {
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// }
	// };

	PacketListener pl = new PacketListener() {

		@Override
		public void onSend(String packet, InetSocketAddress address,
				Charset charset) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = packet;
			packetHandler.sendMessage(msg);
		}

		@Override
		public void onReceive(String packet, InetSocketAddress address,
				Charset charset) {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = packet;
			packetHandler.sendMessage(msg);
		}
	};

	Handler packetHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (debugLogFragment == null) {
				return;
			}
			switch (msg.what) {
			case 1:// send
				debugLogFragment.append((String) msg.obj);
				break;
			case 2:// receive;
				debugLogFragment.append((String) msg.obj);
				break;
			}
		}
	};

	Handler handler;// = new NetworkHandler();
	NetworkListenerImpl listener = new NetworkListenerImpl();
	NetworkMonitor network;

	void setupListener() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ADD_USER:
					userList.add((UserData) msg.obj);
					// 会空指针，因为网络在先view在后。
					if (sendView != null && sendView.userList != null) {
						sendView.userList.setUserList(userList);
					} else {
						// TODO a deferred event must notice.
					}

					break;
				case RECEIVE_MSG:
					final PigeonMessage m = (PigeonMessage) msg.obj;

					String tip = m.getContent();
					if (m.hasAttachements()) {
						String attachementsDesc = "";
						for (AttachementLink item : m.getAttachements()) {
							attachementsDesc += item.getFilename();
						}
						tip += "附件：" + attachementsDesc;

						final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						new AlertDialog.Builder(MainActivity.this)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setTitle("收到文件传输请求")
								.setMessage(attachementsDesc)
								.setPositiveButton("收下",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO reject file
												notificationManager.cancel(m
														.hashCode());
												startDownloadAttachment(m);
											}
										})
								.setNegativeButton("不",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Log.d("main",
														"cancel notify NO.="
																+ m.hashCode());
												notificationManager.cancel(m
														.hashCode());
												// notificationManager.cancelAll();

												new Thread(new Runnable() {

													@Override
													public void run() {
														network.rejectAttachement(m);

													}
												}).start();

											}
										}).show();

						Intent notificationIntent = new Intent(
								MainActivity.this, MainActivity.class); // 点击该通知后要跳转的Activity
						PendingIntent contentItent = PendingIntent.getActivity(
								MainActivity.this, 0, notificationIntent, 0);
						// TODO move to method.
						Notification notification = new NotificationCompat.Builder(
								getApplicationContext())
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentTitle("有文件需要接收")
								.setContentText(attachementsDesc)
								.setStyle(new NotificationCompat.InboxStyle())
								.setAutoCancel(true)
								.setDefaults(Notification.DEFAULT_ALL)
								.addAction(0, "agree", contentItent)
								.addAction(0, "dismiss", contentItent).build();

						// new Notification.Builder(
						// getApplicationContext()).setTicker("有文件需要接收。")
						// // .setLargeIcon(R.drawable.ic_launcher)
						// .setSmallIcon(R.drawable.ic_launcher).build();
						// notification.flags |=
						// (Notification.FLAG_AUTO_CANCEL);
						// notification.defaults = Notification.DEFAULT_ALL;
						// String title = "有文件需要接收";

						// 把Notification传递给NotificationManager
						Log.d("main", "show notify NO.=" + m.hashCode());
						notificationManager.notify(m.hashCode(), notification);

					}
					Toast.makeText(getApplication(), tip, Toast.LENGTH_SHORT)
							.show();

					DummyContent1.addItem(String.valueOf(i++), m.getSender()
							.getUsername(), tip);
					break;
				default:
					Toast.makeText(getApplication(), String.valueOf(msg.obj),
							Toast.LENGTH_SHORT).show();
				}
				;
			}
		};
		listener.handler = handler;
	}

	/**
	 * 接收附件。
	 * 
	 * @param m
	 */
	void startDownloadAttachment(PigeonMessage m) {
		long total = 0;
		for (int i = 0; i < m.getAttachements().size(); i++) {
			total += m.getAttachements().get(i).getLength();
		}

		AsyncTask<PigeonMessage, Long, Integer> task = new DownloadTask(
				this.network, this).execute(m);

		// Toast.makeText(getApplication(), "已经进入下载队列。", Toast.LENGTH_SHORT)
		// .show();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("main", "exiting,close network.");
		new Thread() {
			@Override
			public void run() {
				network.quit();
			}

		}.start();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	List<UserData> userList = new ArrayList<UserData>(10);

	private SharedPreferences sharedPreferences;

	static class NetworkListenerImpl implements NetworkActivityListener {

		Handler handler;

		@Override
		public void onBindOk() {
			Message msg = new Message();
			msg.obj = "连接成功";
			handler.sendMessage(msg);
		}

		@Override
		public void onBindFailure(SocketException exception) {
			Message msg = new Message();
			msg.obj = exception.getLocalizedMessage();
			handler.sendMessage(msg);
		}

		@Override
		public void onAddUser(UserData user) {
			Log.d("main",
					"A new user online:" + user + ",address:"
							+ user.getAddress());
			Message msg = new Message();
			msg.what = ADD_USER;
			msg.obj = user;
			handler.sendMessage(msg);
		}

		@Override
		public void onMessage(PigeonMessage message) {
			Message msg = new Message();
			msg.what = RECEIVE_MSG;
			msg.obj = message;
			handler.sendMessage(msg);
			Log.d("main", "received message:" + message);
			// msg.

			// TODO Auto-generated method stub

		}

	}

}
