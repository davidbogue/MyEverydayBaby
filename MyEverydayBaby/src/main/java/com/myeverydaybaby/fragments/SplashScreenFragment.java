package com.myeverydaybaby.fragments;

import android.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myeverydaybaby.DashboardActivity;
import com.myeverydaybaby.R;
import com.myeverydaybaby.models.Baby;

public class SplashScreenFragment extends Fragment {

    private LoadBabyTask loadBabyTask = null;
    private Activity parentActivity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // temporary 5 second pause... remove this later
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                loadBabyData();
            }
        }, 5000);
        return inflater.inflate(R.layout.splash_screen, container, false);
    }

    private void loadBabyData(){
        if (loadBabyTask != null) {
            return;
        }
        loadBabyTask = new LoadBabyTask();
        loadBabyTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous task used to load user data from the SQLite database
     * the user.
     */
    public class LoadBabyTask extends AsyncTask<Void, Void, Baby> {
        @Override
        protected Baby doInBackground(Void... params) {
            try {
                // TODO use DB helper to return baby
                Baby baby = new Baby();
                baby.setName("Test Baby");
                baby.setId(1l);
                baby.setBirthday(123l);


                return baby;
            }catch (Exception e) {
                // authentication failed
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Baby baby) {
            loadBabyTask = null;

            if (baby != null) {
                Intent intent = new Intent(parentActivity, DashboardActivity.class);
                startActivity(intent);
                parentActivity.finish();
            } else {
                //show welcome activity
//                finish();
            }
        }

        @Override
        protected void onCancelled() {
            loadBabyTask = null;
        }

    }

}
