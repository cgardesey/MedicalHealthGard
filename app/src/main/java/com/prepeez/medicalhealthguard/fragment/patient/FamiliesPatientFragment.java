package com.prepeez.medicalhealthguard.fragment.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.PatientAdapter;
import com.prepeez.medicalhealthguard.materialDialog.CreateGroupMaterialDialog;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.prepeez.medicalhealthguard.util.AlertDialogHelper;
import com.prepeez.medicalhealthguard.util.RecyclerItemClickListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.prepeez.medicalhealthguard.activity.PatientActivity.search;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.isMultiSelect;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.mActionMode;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.multiselect_list;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.populatePatients;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.refreshAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FamiliesPatientFragment extends Fragment {

    static CreateGroupMaterialDialog createGroupMaterialDialog;
    static PatientAdapter patientAdapter;
    public static ArrayList<RealmPatient> patients = new ArrayList<>();
    public static ArrayList<RealmPatient> multiselect_list = new ArrayList<>();

    public static ActionMode mActionMode;
    Menu context_menu;
    public static boolean isMultiSelect = false;

    AlertDialogHelper alertDialogHelper;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    public FamiliesPatientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_patient, container, false);
        Realm.init(getContext());

        createGroupMaterialDialog = new CreateGroupMaterialDialog();

        //alertDialogHelper =new AlertDialogHelper(getActivity());
        recyclerView = rootView.findViewById(R.id.recyclerView);
        populatePatients();
        patientAdapter = new PatientAdapter(patients, multiselect_list, search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(patientAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new                       RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
//                else
//                    Toast.makeText(getContext(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<RealmPatient>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populatePatients();
        refreshAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initSearchView();
    }

    public static void populatePatients() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmPatient> results = realm.where(RealmPatient.class).equalTo("grouptype", "Family").findAll();

        patients.clear();
        for (RealmPatient patient : results) {
            patients.add(patient);
        }
    }
//    public void filter(ArrayList<RealmPatient> realmPatients)
//    {
//        if(realmPatients.size()>0) {
//            patientAdapter.setFilter(realmPatients);
//            // adapter = new Adapter(filterevents, false);
//            //  Toast.makeText(mContext,String.valueOf(realmPatients.size()),Toast.LENGTH_LONG).show();
//            linearLayoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(linearLayoutManager);
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            //Log.d("Cyrilgard","working");
//            //  myrecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//            recyclerView.setAdapter(patientAdapter);
//            Log.d("Cyrilgard","working");
//
//        }
//    }

    static public void filter_families(String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<RealmPatient> filteredModelList = new ArrayList<>();

        for (RealmPatient model : patients) {
            String patientId = model.getPatientid();
            String name = model.getTitle() + " " + model.getLastname() + " " + model.getFirstname();
            String nextOfKinName = model.getTitle() + " " + model.getLastname() + " " + model.getFirstname();
            String contact = model.getContact();
            String location = model.getLocation();

            patientId = patientId.toLowerCase();
            name = name.toLowerCase();
            nextOfKinName = nextOfKinName.toLowerCase();
            contact = contact.toLowerCase();
            location = location.toLowerCase();

            if (
                    patientId.contains(search_txt) ||
                            name.contains(search_txt) ||
                            nextOfKinName.contains(search_txt) ||
                            contact.contains(search_txt) ||
                            location.contains(search_txt)
                    ) {
                filteredModelList.add(model);
            }
        }
        //Log.d("hmmm", Boolean.toString(patientAdapter == null));
        patientAdapter.setFilter(filteredModelList);
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(patients.get(position)))
                multiselect_list.remove(patients.get(position));
            else
                multiselect_list.add(patients.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }


    public static void refreshAdapter() {
        Log.d("kofi", "kofi2");
        patientAdapter.selected_patients = multiselect_list;
        patientAdapter.patients = patients;
        patientAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_create_group) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                builder.setMessage("Delete from group?");
                builder.setNegativeButton(R.string.no, null);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                for (RealmPatient realmPatient : multiselect_list) {
                                    realmPatient.setGroupid(null);
                                    realmPatient.setGrouptype("Individual");
                                    ;
                                }
                                if (mActionMode != null) {
                                    mActionMode.finish();
                                }
                                isMultiSelect = false;
                                multiselect_list = new ArrayList<RealmPatient>();
                                populatePatients();
                                refreshAdapter();

                                IndividualsPatientFragment individualsPatientFragment = new IndividualsPatientFragment();
                                CompaniesPatientFragment companiesPatientFragment = new CompaniesPatientFragment();

                                individualsPatientFragment.populatePatients();
                                companiesPatientFragment.populatePatients();

                                individualsPatientFragment.refreshAdapter();
                                companiesPatientFragment.refreshAdapter();
                            }
                        });
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<RealmPatient>();
            refreshAdapter();
        }
    };
}
