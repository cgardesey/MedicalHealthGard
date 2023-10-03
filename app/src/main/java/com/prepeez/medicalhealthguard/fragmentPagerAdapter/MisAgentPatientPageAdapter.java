package com.prepeez.medicalhealthguard.fragmentPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment3;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment4;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment5;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment6;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment7;

public class MisAgentPatientPageAdapter extends FragmentPagerAdapter {

    public MisAgentPatientPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            case 3:
                TabFragment4 tab4 = new TabFragment4();
                return tab4;
            case 4:
                TabFragment5 tab5 = new TabFragment5();
                return tab5;
            case 5:
                TabFragment6 tab6 = new TabFragment6();
                return tab6;
            case 6:
                TabFragment7 tab7 = new TabFragment7();
                return tab7;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }
}