package com.miwpfm.weplay.fragments;

import com.miwpfm.weplay.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMyInfo extends Fragment {


	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	Activity parent=getActivity();
    	ActionBar actionBar= parent.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle(getString(R.string.menu_option_myinfo));
		
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

				@Override
				public void onTabReselected(Tab tab,
						FragmentTransaction ft) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTabSelected(Tab tab,
						FragmentTransaction ft) {
					int position = tab.getPosition();
					Fragment fragment = null;
					
					switch (position) {
					case 0:
						fragment = new FragmentUserInfo();
						break;
					case 1:
						fragment = new FragmentUserLocation();
						break;
					case 2:
						fragment = new FragmentUserPassword();
						break;
					}
					ft.replace(R.id.fragment_container, fragment);					
				}

				@Override
				public void onTabUnselected(Tab tab,
						FragmentTransaction ft) {
				}
		    };
    			  
	    actionBar.removeAllTabs();
    	actionBar.addTab(actionBar.newTab().setText(getString(R.string.my_info_tab_info)).setTabListener(tabListener));
    	actionBar.addTab(actionBar.newTab().setText(getString(R.string.my_info_tab_location)).setTabListener(tabListener));
    	actionBar.addTab(actionBar.newTab().setText(getString(R.string.my_info_tab_password)).setTabListener(tabListener));

        return inflater.inflate(R.layout.fragment_myinfo, container, false);
    }
}
