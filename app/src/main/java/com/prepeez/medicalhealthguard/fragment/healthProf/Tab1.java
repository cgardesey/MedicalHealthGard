package com.prepeez.medicalhealthguard.fragment.healthProf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperSupportFragment;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.PictureActivity;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.prepeez.medicalhealthguard.util.PixelUtil;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import static android.app.Activity.RESULT_OK;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.profilePicBitmap;

import com.yalantis.ucrop.UCrop;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.PICTURE_TYPE;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.TYPE_PROFILE_PIC;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2.BOUNDS_MOUNTAIN_VIEW;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2.PLACE_PICKER_REQUEST;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2.simpleDateFormat;

import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class Tab1 extends MultiPickerWrapperSupportFragment implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    private static final String TAG = "Tab2";

    public RoundedImageView profilePic;
    private EditText firstName, lastName, otherNames;
    private Spinner title;
    public static final String PREF_PROFILE_IMAGE_URI = "PREF_PROFILE_IMAGE_URI";
    Context mContext;
    private static final int LOAD_IMAGE_RESULTS = 999;
    private FloatingActionButton addimage, gal, cam, rem;
    LinearLayout controls;
    private ImageView opendate;
    private ProgressBar progressBar;
    EditText location;
    ImageView locPicker;
    EditText dateofbirth;
    IntlPhoneInput intlPhoneInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1, container, false);
        profilePic = rootView.findViewById(R.id.profile_imgview);
        firstName = rootView.findViewById(R.id.first_name);
        lastName = rootView.findViewById(R.id.last_name);
        otherNames = rootView.findViewById(R.id.other_names);
        title = rootView.findViewById(R.id.title_spinner);
        opendate = rootView.findViewById(R.id.dateselect);
        progressBar = rootView.findViewById(R.id.progress_bar);
        addimage = rootView.findViewById(R.id.addimage);
        controls = rootView.findViewById(R.id.add);
        gal = rootView.findViewById(R.id.gal);
        cam = rootView.findViewById(R.id.cam);
        rem = rootView.findViewById(R.id.rem);
        opendate = rootView.findViewById(R.id.dateselect);
        location = rootView.findViewById(R.id.location);
        dateofbirth = rootView.findViewById(R.id.dateofbirth);
        intlPhoneInput = rootView.findViewById(R.id.phonenumber);
        locPicker = rootView.findViewById(R.id.locPicker);


        mContext = getContext();

        opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(1980, 0, 1, R.style.DatePickerSpinner);
            }

        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controls.getVisibility() == View.VISIBLE) {
                    controls.setVisibility(View.GONE);

                } else {
                    controls.setVisibility(View.VISIBLE);
                }
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multiPickerWrapper.getPermissionAndPickSingleImageAndCrop(imgOptions(), 1, 1);
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multiPickerWrapper.getPermissionAndTakePictureAndCrop(imgOptions(), 1, 1);
            }
        });
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePic.setImageDrawable(null);
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (profilePic.getDrawable() == null) {
                    Toast.makeText(mContext, "Patient has no profile image!", Toast.LENGTH_SHORT).show();
                } else {
                    profilePicBitmap = ((RoundedDrawable) profilePic.getDrawable()).getSourceBitmap();
                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    intent.putExtra(PICTURE_TYPE, TYPE_PROFILE_PIC);
                    getActivity().startActivity(intent);
                }
            }
        });
        locPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intlPhoneInput.setDefault();
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {

                final Place place = PlacePicker.getPlace(mContext, data);
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                String attributions = (String) place.getAttributions();
                if (attributions == null) {
                    attributions = "";
                }


                location.setText("");
                location.setText(name);
                location.setError(null);
                //  mAddress.setText(address);
                //  mAttributions.setText(Html.fromHtml(attributions));


            } else {
                Toast.makeText(mContext, "No location selected",
                        Toast.LENGTH_LONG).show();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }

    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            // do something here
        }

        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            controls.setVisibility(View.GONE);
            String imagePath = list.get(0).getOriginalPath();
            profilePic.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {

        }

        @Override
        public void onError(String s) {
            Toast.makeText(getContext(), "Error choosing image", Toast.LENGTH_SHORT).show();
            Log.d(TAG, s);
        }
    };

    public boolean validate() {
        boolean validated = true;

        if (profilePic.getDrawable() == null) {
            clickedPatient.setPicture("");
        } else {
            Bitmap bitmap = ((RoundedDrawable) profilePic.getDrawable()).getSourceBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            final String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            clickedPatient.setPicture(encodedImage);
        }
        if (TextUtils.isEmpty(firstName.getText())) {
            firstName.setError(getString(R.string.error_field_required));
            validated = false;
        } else {

            clickedPatient.setFirstname(firstName.getText().toString().trim());

        }
        if (TextUtils.isEmpty(lastName.getText())) {
            lastName.setError(getString(R.string.error_field_required));
            validated = false;
        } else {

            clickedPatient.setLastname(lastName.getText().toString().trim());

        }
        if (title.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) title.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {

            clickedPatient.setTitle(title.getSelectedItem().toString());

        }
        clickedPatient.setOthername(otherNames.getText().toString().trim());
        if (TextUtils.isEmpty(dateofbirth.getText())) {
            dateofbirth.setError(getString(R.string.error_field_required));
            validated = false;
        } else {

            clickedPatient.setDateofbirth(dateofbirth.getText().toString().trim());

        }
        if (!intlPhoneInput.isValid()) {
            Toast.makeText(mContext, "Invalid number!", Toast.LENGTH_SHORT).show();
            validated = false;
        } else {

            clickedPatient.setContact(intlPhoneInput.getNumber());

        }

        if (TextUtils.isEmpty(location.getText())) {
            location.setError(getString(R.string.error_field_required));
            validated = false;
        } else {
            clickedPatient.setLocation(location.getText().toString().trim());
        }
        return validated;
    }

    public void init() {
        if (action.equals("edit")) {
            String encoded_picture = clickedPatient.getPicture();
            if(!TextUtils.isEmpty(encoded_picture)){
                byte[] decodedBytes = Base64.decode(encoded_picture, 0);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                profilePic.setImageBitmap(decodedBitmap);

            } else {
                profilePic.setImageBitmap(null);
            }
            title.setSelection(((ArrayAdapter) title.getAdapter()).getPosition(clickedPatient.getTitle()));
            firstName.setText(clickedPatient.getFirstname());
            lastName.setText(clickedPatient.getLastname());
            otherNames.setText(clickedPatient.getOthername());
            dateofbirth.setText(clickedPatient.getDateofbirth());
            intlPhoneInput.setNumber(clickedPatient.getContact());
            location.setText(clickedPatient.getLocation());
        }
    }

    private UCrop.Options imgOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setCropFrameColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setCropFrameStrokeWidth(PixelUtil.dpToPx(getContext(), 4));
        options.setCropGridColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setCropGridStrokeWidth(PixelUtil.dpToPx(getContext(), 2));
        options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setToolbarTitle("Crop Image");

        // set rounded cropping guide
        options.setCircleDimmedLayer(true);
        return options;
    }

    public static Uri getImageUriFromBitmap(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateofbirth.setText(simpleDateFormat.format(calendar.getTime()));
        dateofbirth.setError(null);
    }

    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public void clear() {
        profilePic.setImageBitmap(null);
        title.setSelection(0);
        firstName.setText(null);
        lastName.setText(null);
        otherNames.setText(null);
        dateofbirth.setText(null);
        ((EditText) ((LinearLayout) intlPhoneInput.getChildAt(0)).getChildAt(1)).setText("");
        location.setText(null);
    }
}