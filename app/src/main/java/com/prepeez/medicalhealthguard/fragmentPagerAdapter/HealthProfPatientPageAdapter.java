package com.prepeez.medicalhealthguard.fragmentPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prepeez.medicalhealthguard.fragment.healthProf.Tab1;
import com.prepeez.medicalhealthguard.fragment.healthProf.Tab2;
import com.prepeez.medicalhealthguard.fragment.healthProf.Tab3;

public class HealthProfPatientPageAdapter extends FragmentPagerAdapter {

    public HealthProfPatientPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}