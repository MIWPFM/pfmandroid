package com.miwpfm.weplay.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.miwpfm.weplay.R;

public class FragmentUserPassword extends Fragment {
	EditText editOldPassword;
	EditText editNewPassword;
	EditText editRepeatPassword;
	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_user_password, null,
				false);
		editOldPassword = (EditText) view.findViewById(R.id.editOldPassword);
		editNewPassword = (EditText) view.findViewById(R.id.editNewPassword);
		editRepeatPassword = (EditText) view.findViewById(R.id.editRepeatPassword);
		
        return view;
    }
}
