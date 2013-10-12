package com.myeverydaybaby;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.app.DialogFragment;
import android.widget.TextView;

import com.myeverydaybaby.fragments.DatePickerFragment;

import java.util.List;

public class WelcomeActivity extends Activity implements DatePickerDialog.OnDateSetListener {

    private static final int ACTION_TAKE_SMALL_PHOTO = 2;

    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private TextView mBirthdayText;
    private TextView mNameText;

    private Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_SMALL_PHOTO);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        // Show the Up button in the action bar.
        setupActionBar();
        mImageView = (ImageView)this.findViewById(R.id.imageView);
        mBirthdayText = (TextView)this.findViewById(R.id.birthdayText);
        mNameText = (TextView)this.findViewById(R.id.babyNameText);
        mImageBitmap = null;

        Button picBtn = (Button) this.findViewById(R.id.camera_button);
        setBtnListenerOrDisable( picBtn, mTakePicOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE );
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }


    // DATE PICKER CODE

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month++;
        mBirthdayText.setText(month+"/"+day+"/"+year);
    }


    // CAMERA RELATED CODE

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_SMALL_PHOTO: {
                if (resultCode == this.RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            }
        } // switch
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(Button btn, Button.OnClickListener onClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.not_available).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

    // SAVE AND CONTINUE CODE
    public void saveAndContinue(View v){

    }


}
