package com.myeverydaybaby.factories;

import android.app.Fragment;

import com.myeverydaybaby.fragments.DashboardFragment;
import com.myeverydaybaby.fragments.DiaperFragment;
import com.myeverydaybaby.fragments.DiaperTrendFragment;
import com.myeverydaybaby.fragments.FeedingFragment;
import com.myeverydaybaby.fragments.FeedingTrendFragment;
import com.myeverydaybaby.fragments.SleepingFragment;
import com.myeverydaybaby.fragments.SleepingTrendFragment;

/**
 * Created by davidbogue on 9/1/13.
 */
public class NavigationFragmentFactory {

    public Fragment getNavigationFragment(int menuPosition){
        switch(menuPosition){
            case 0:
                return new DashboardFragment();
            case 1:
                return new FeedingFragment();
            case 2:
                return new SleepingFragment();
            case 3:
                return new DiaperFragment();
            case 4:
                return new FeedingTrendFragment();
            case 5:
                return new SleepingTrendFragment();
            case 6:
                return new DiaperTrendFragment();
            case 7:

            default:
                return new DashboardFragment();
        }
    }
}
