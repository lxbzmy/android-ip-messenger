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
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class TextListFragment<T> extends ListFragment {

	private ArrayAdapter<T> adapter;

	public TextListFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<T>(getActivity(),
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		// setListAdapter(new SimpleAdapter(getActivity(), DummyContent1.ITEMS,
		// android.R.layout.two_line_list_item, new String[] { "text1",
		// "text2" }, new int[] { android.R.id.text1,
		// android.R.id.text2 }));
	}

	public void add(T what) {
		adapter.add(what);
		adapter.notifyDataSetChanged();
	}

	public void clear() {
		adapter.clear();
		adapter.notifyDataSetChanged();
	}

}
