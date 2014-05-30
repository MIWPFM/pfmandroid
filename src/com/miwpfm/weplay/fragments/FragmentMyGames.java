package com.miwpfm.weplay.fragments;

import com.miwpfm.weplay.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMyGames extends Fragment {

	 
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {    	
    	Activity parent=getActivity();
    	ActionBar actionBar= parent.getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle(getString(R.string.menu_option_mygames));
		
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

				@Override
				public void onTabReselected(Tab arg0,
						FragmentTransaction arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTabSelected(Tab arg0,
						FragmentTransaction arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTabUnselected(Tab arg0,
						FragmentTransaction arg1) {
					// TODO Auto-generated method stub
					
				}
		    };
    			  
	    actionBar.removeAllTabs();
    	actionBar.addTab(actionBar.newTab().setText("Ejemplo").setTabListener(tabListener));
    	actionBar.addTab(actionBar.newTab().setText("Ejemplo2").setTabListener(tabListener));
    	actionBar.addTab(actionBar.newTab().setText("Ejemplo3").setTabListener(tabListener));

        return inflater.inflate(R.layout.fragment_mygames, container, false);
    }
}
