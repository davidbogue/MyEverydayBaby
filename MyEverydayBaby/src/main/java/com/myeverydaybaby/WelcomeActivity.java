package com.myeverydaybaby;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.myeverydaybaby.fragments.DatePickerFragment;
import com.myeverydaybaby.models.Baby;
import com.myeverydaybaby.persistence.BabyDAO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WelcomeActivity extends Activity implements DatePickerDialog.OnDateSetListener {

    private static final int ACTION_TAKE_SMALL_PHOTO = 2;
    private static final int ACTION_PICK_PHOTO = 3;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String CAMERA_DIR = "/dcim/";

    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private TextView mBirthdayText;
    private TextView mNameText;
    private String profilePhotoPath;

    private SaveBabyTask saveBabyTask;
    private BabyDAO babyDAO;

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

        babyDAO = new BabyDAO(this);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_SMALL_PHOTO: {
                if (resultCode == this.RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            }
            case ACTION_PICK_PHOTO:{
                if(resultCode == this.RESULT_OK){
                    handleGalleryPhoto(data);
                }
                break;
            }
        } // switch
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


    // GALLERY RELATED CODE
    public void pickPictureFromGallery(View view){
        Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, ACTION_PICK_PHOTO);
    }

    private void handleGalleryPhoto(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        mImageBitmap =  getScaledBitmap(picturePath);
        if(mImageBitmap != null){
            profilePhotoPath = picturePath;
            mImageView.setImageBitmap( mImageBitmap);
        }
    }

    private Bitmap getScaledBitmap(String pathName){
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Matrix matrix = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(pathName);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(pathName, bmOptions);
        if(bitmap != null){
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
        }
        return bitmap;
    }

    // CAMERA RELATED CODE

    private void dispatchTakePictureIntent(int actionCode) {
        File f;
        try {
            f = setUpPhotoFile();
            profilePhotoPath = f.getAbsolutePath();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(takePictureIntent, actionCode);
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
        }
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File (
                    Environment.getExternalStorageDirectory()
                            + CAMERA_DIR
                            + getString(R.string.album_name)
            );

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }


    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        profilePhotoPath = f.getAbsolutePath();

        return f;
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
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
        if(saveBabyTask != null){
            return;
        }
        if(validateSave()){
            Baby b = new Baby();
            b.setName( mNameText.getText().toString() );
            b.setBirthday( getBirthdayAsLong() );
            b.setPicture( profilePhotoPath );
            saveBabyTask = new SaveBabyTask();
            saveBabyTask.execute( (Baby) b );
        }
    }

    private long getBirthdayAsLong(){
        String bdayText = mBirthdayText.getText().toString();
        if( TextUtils.isEmpty(bdayText) ){
            return System.currentTimeMillis();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = simpleDateFormat.parse(bdayText);
            return date.getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }

    private boolean validateSave(){
        mNameText.setError(null);
        mBirthdayText.setError(null);
        boolean valid = true;

        if(TextUtils.isEmpty( mNameText.getText().toString())){
            mNameText.setError( getString(R.string.error_field_required));
            valid = false;
        }
        if(TextUtils.isEmpty( mBirthdayText.getText().toString())){
            mBirthdayText.setError( getString(R.string.error_field_required));
            valid = false;
        }

        return valid;
    }

    /**
     * Represents an asynchronous task used to load user data from the SQLite database
     * the user.
     */
    public class SaveBabyTask extends AsyncTask<Baby, Void, Baby> {
        String errorMessage;

        @Override
        protected Baby doInBackground(Baby... babies) {
            Baby baby = babies[0];
            try {
                babyDAO.open();
                babyDAO.createBaby(baby);
            } catch (SQLException e) {
                errorMessage = "There was an unexpected error. Please try again.";
                return null;
            }finally {
                babyDAO.close();
            }
            return baby;
        }

        @Override
        protected void onPostExecute(final Baby baby) {
            saveBabyTask = null;
            if (baby != null) {
                Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            saveBabyTask = null;
        }

    }


}
