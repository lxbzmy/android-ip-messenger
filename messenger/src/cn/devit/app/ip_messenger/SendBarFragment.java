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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link SendBarFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link SendBarFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class SendBarFragment extends Fragment {

	private OnFragmentInteractionListener mListener;
	private ImageButton sendButton;
	private ImageButton attachButton;
	private EditText editText;
	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String message = editText.getText().toString();
			if (mListener != null) {
				mListener.onSendMessage(message);
				// TODO 发送失败是否重试？
				editText.getText().clear();
			}
		}
	};
	private OnClickListener attachBtnlistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
			Intent i = Intent.createChooser(intent,
					"Select File As Attachement.");
			startActivityForResult(intent, MainActivity.PICKFILE_RESULT_CODE);

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
	 * @return A new instance of fragment SendFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SendBarFragment newInstance(String param1, String param2) {
		SendBarFragment fragment = new SendBarFragment();
		return fragment;
	}

	public SendBarFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// fin
		// sendButton = (ImageButton) this.getView().findViewById(
		// R.id.imageButton1);
		// attachButton = (ImageButton) this.getView().findViewById(
		// R.id.imageButton2);
		// editText = (EditText) this.getView().findViewById(R.id.editText1);
		// sendButton.setOnClickListener(listener);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sender_bar, container, false);
		sendButton = (ImageButton) view.findViewById(R.id.imageButton1);
		attachButton = (ImageButton) view.findViewById(R.id.imageButton2);
		editText = (EditText) view.findViewById(R.id.editText1);
		sendButton.setOnClickListener(listener);
		attachButton.setOnClickListener(attachBtnlistener);
		return view;
	}

	// // TODO: Rename method, update argument and hook method into UI event
	// public void onButtonPressed(Uri uri) {
	// if (mListener != null) {
	// mListener.onFragmentInteraction(uri);
	// }
	// }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// try {
		// mListener = (OnFragmentInteractionListener) activity;
		// } catch (ClassCastException e) {
		// throw new ClassCastException(activity.toString()
		// + " must implement OnFragmentInteractionListener");
		// }
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public void setInteractionListener(OnFragmentInteractionListener listener) {
		this.mListener = listener;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		public void onSendMessage(String text);
	}

}
