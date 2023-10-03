package com.prepeez.medicalhealthguard.fragment.misagent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.PictureActivity;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.prepeez.medicalhealthguard.util.PixelUtil;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperSupportFragment;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.realm.Realm;

import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.retrieveGSON;
import static com.prepeez.medicalhealthguard.constants.Const.storeGSON;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.PICTURE_TYPE;

import androidx.core.content.ContextCompat;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFragment5 extends MultiPickerWrapperSupportFragment {
    private static final String TAG = "TabFragment5";
    public static final String TYPE_NHIS = "TYPE_NHIS";
    public static Bitmap nhisBitmap;
    FloatingActionButton fab;
    public RoundedImageView profilePic;
    private EditText NHISNo;
    private static final String PREF_NHIS_IMAGE_URI = "PREF_NHIS_IMAGE_URI";
    Context mContext;
    private static final int LOAD_IMAGE_RESULTS = 999 ;
    private FloatingActionButton addimage, gal, cam, rem;
    LinearLayout controls;

    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab5, container, false);


        //

        fab = rootView.findViewById(R.id.done);

        profilePic = rootView.findViewById(R.id.profile_imgview);
        NHISNo = rootView.findViewById(R.id.nhisno);
        progressBar = rootView.findViewById(R.id.progress_bar);
        addimage = rootView.findViewById(R.id.addimage);
        controls = rootView.findViewById(R.id.add);
        gal = rootView.findViewById(R.id.gal);
        cam = rootView.findViewById(R.id.cam);
        rem = rootView.findViewById(R.id.rem);
        addimage = rootView.findViewById(R.id.addimage);

        mContext = getContext();
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

                multiPickerWrapper.getPermissionAndPickSingleImageAndCrop(imgOptions(), 3.370f, 2.125f );
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
                    Toast.makeText(mContext, "Patient has no image of NHIS!", Toast.LENGTH_SHORT).show();
                } else {
                    nhisBitmap = ((RoundedDrawable)profilePic.getDrawable()).getSourceBitmap();

                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    intent.putExtra(PICTURE_TYPE, TYPE_NHIS);
                    getActivity().startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
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

    public boolean validate (){
        boolean validated = true;

        if (profilePic.getDrawable() == null) {
            clickedPatient.setPicture("");
        }
        else {
            Bitmap bitmap = ((RoundedDrawable)profilePic.getDrawable()).getSourceBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            final String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            clickedPatient.setNhispicture(encodedImage);
        }
        clickedPatient.setNhisnumber(NHISNo.getText().toString().trim());
        return validated;
    }

    public void init() {
        if (action.equals("edit")) {
            String encoded_picture = clickedPatient.getNhispicture();
            if(!TextUtils.isEmpty(encoded_picture)){
                byte[] decodedBytes = Base64.decode(encoded_picture, 0);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                profilePic.setImageBitmap(decodedBitmap);

            } else {
                profilePic.setImageBitmap(null);
            }
            NHISNo.setText(clickedPatient.getNhisnumber());
        }
    }

    public RealmPatient getPatient() {
        return clickedPatient;
    }

    private UCrop.Options imgOptions () {
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

    public void clear() {
        NHISNo.setText(null);
        profilePic.setImageBitmap(null);
    }
}