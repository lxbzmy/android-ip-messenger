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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class DebugLogFragment extends Fragment {

	public DebugLogFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_debug_log,
				container, false);
		return rootView;
	}

	TextListFragment<String> list;
	Button button;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		button = (Button) view.findViewById(R.id.button1);
		button.setOnClickListener(cc);

		if (list == null) {
			list = new TextListFragment<String>();
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			transaction.add(R.id.frame1, list);
			transaction.commit();
		}

	}

	OnClickListener cc = new OnClickListener() {
		@Override
		public void onClick(View v) {
			list.clear();
		}
	};

	public void append(String what) {
		if (list != null) {
			list.add(what);
		}
	}

}