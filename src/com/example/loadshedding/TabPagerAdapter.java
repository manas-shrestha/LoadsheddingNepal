package com.example.loadshedding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class TabPagerAdapter extends FragmentPagerAdapter {

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ListManager("1");
		case 1:
			// Games fragment activity
			return new ListManager("2");
		case 2:
			// Games fragment activity
			return new ListManager("3");
		case 3:
			// Games fragment activity
			return new ListManager("4");
		case 4:
			// Games fragment activity
			return new ListManager("5");
		case 5:
			// Games fragment activity
			return new ListManager("6");

		case 6:
			// Games fragment activity
			return new ListManager("7");
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 7;
	}
	
	

}