package com.prepeez.medicalhealthguard.fragmentPagerAdapter;

import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag1;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag2;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag3;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag4;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag5;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag6;

import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HealthRecordPageAdapter extends FragmentPagerAdapter {

    public HealthRecordPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (clickedPatient.getGender().equals("Female")) {
            switch (position) {
                case 0:
                    TabFrag1 tab1 = new TabFrag1();
                    return tab1;
                case 1:
                    TabFrag2 tab2 = new TabFrag2();
                    return tab2;
                case 2:
                    TabFrag3 tab3 = new TabFrag3();
                    return tab3;
                case 3:
                    TabFrag4 tab4 = new TabFrag4();
                    return tab4;
                case 4:
                    TabFrag5 tab5 = new TabFrag5();
                    return tab5;
                case 5:
                    TabFrag6 tab6 = new TabFrag6();
                    return tab6;
                default:
                    return null;
            }

        }
        else {
            switch (position) {
                case 0:
                    TabFrag1 tab1 = new TabFrag1();
                    return tab1;
                case 1:
                    TabFrag2 tab2 = new TabFrag2();
                    return tab2;
                case 2:
                    TabFrag3 tab3 = new TabFrag3();
                    return tab3;
                case 3:
                    TabFrag4 tab4 = new TabFrag4();
                    return tab4;
                case 4:
                    TabFrag6 tab6 = new TabFrag6();
                    return tab6;
                default:
                    return null;
            }

        }

    }

    @Override
    public int getCount() {
        if (clickedPatient.getGender().equals("Female")) {
            return 6;
        }
        else {
            return 5;
        }
    }
}