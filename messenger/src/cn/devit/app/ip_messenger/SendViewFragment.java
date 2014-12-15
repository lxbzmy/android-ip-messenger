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

import cn.devit.app.ip_messenger.SendBarFragment.OnFragmentInteractionListener;
import cn.devit.app.ip_messenger.pigeon.UserData;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link SendViewFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
/**
 * @author lxb
 *
 */
public class SendViewFragment extends Fragment {

	UserListFragment userList;

	SendBarFragment sendBar;

	@SuppressLint("NewApi")
	private SendBarFragment.OnFragmentInteractionListener listener = new SendBarFragment.OnFragmentInteractionListener() {

		@Override
		public void onSendMessage(String text) {
			// TODO Auto-generated method stub
			int count = userList.getListAdapter().getCount();
			int selectedCount = userList.getListView().getCheckedItemCount();
			if (selectedCount > 0) {
				UserData[] u = new UserData[selectedCount];
				int j = 0;

				SparseBooleanArray checkedItemPositions = userList
						.getListView().getCheckedItemPositions();
				for (int i = 0; i < count; i++) {
					boolean checked = checkedItemPositions.get(i);
					if (checked) {
						UserData userData = userList.userList.get(i);
						u[j++] = userData;
					}
				}
				// TODO real send
				if (mListener != null) {
					mListener.onSendMessage(u, text);
				}
				// add history.
			} else {
				Log.d("main", "not user select. ignore.");
			}

		}
	};

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment BlankFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SendViewFragment newInstance(String param1, String param2) {
		SendViewFragment fragment = new SendViewFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public SendViewFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.users_and_send_bar, container,
				false);
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d("main", "add user list and send bar.");
		if (userList == null) {
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			userList = new UserListFragment();
			transaction.add(R.id.user_list, userList);
			sendBar = new SendBarFragment();
			transaction.add(R.id.send_bar, sendBar);
			transaction.commit();
			sendBar.setInteractionListener(listener);
		}

	}

	SendViewFragment.OnFragmentInteractionListener mListener;

	public void setOnFragmentInteractionListener(
			SendViewFragment.OnFragmentInteractionListener listener) {
		this.mListener = listener;
	}

	public interface OnFragmentInteractionListener {
		public void onSendMessage(UserData[] user, String message);
	}

	@Override
	public void onDestroyView() {
		// FragmentManager mFragmentMgr = getChildFragmentManager();
		// FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
		// mTransaction.remove(userList);
		// mTransaction.remove(sendBar);
		// userList = null;
		// sendBar = null;
		// mTransaction.commit();
		super.onDestroyView();
	}
}
