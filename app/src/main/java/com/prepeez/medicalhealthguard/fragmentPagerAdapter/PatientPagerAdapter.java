package com.prepeez.medicalhealthguard.fragmentPagerAdapter;

/**
 * Created by Nana on 11/26/2017.
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prepeez.medicalhealthguard.fragment.patient.CompaniesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.FamiliesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment;


public class PatientPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PatientPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                IndividualsPatientFragment tab1 = new IndividualsPatientFragment();
                return tab1;
            case 1:
                FamiliesPatientFragment tab2 = new FamiliesPatientFragment();
                return tab2;
            case 2:
                CompaniesPatientFragment tab3 = new CompaniesPatientFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
