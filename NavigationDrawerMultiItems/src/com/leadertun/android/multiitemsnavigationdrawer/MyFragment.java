package com.leadertun.android.multiitemsnavigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends BaseFragment {
	public static final String ARG_NAME_NUMBER = "name_number";

	public MyFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.my_fragment, container, false);
		int i = getArguments().getInt(ARG_NAME_NUMBER);
		String str = getArguments().getString("addaccount");
      
		((TextView) rootView.findViewById(R.id.fragment_layout_textView))
				.setText(str);

		getActivity().setTitle(str);
		return rootView;
	}
}
