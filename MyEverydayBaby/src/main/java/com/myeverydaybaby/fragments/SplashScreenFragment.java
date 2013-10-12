package com.myeverydaybaby.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myeverydaybaby.DashboardActivity;
import com.myeverydaybaby.R;
import com.myeverydaybaby.WelcomeActivity;
import com.myeverydaybaby.models.Baby;
import com.myeverydaybaby.persistence.BabyDAO;

import java.sql.SQLException;
import java.util.List;

public class SplashScreenFragment extends Fragment {


    private LoadBabyTask loadBabyTask;
    private BabyDAO babyDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        babyDAO = new BabyDAO(getActivity());
        loadBabyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            Baby baby = null;
            try {
                babyDAO.open();
                List<Baby> babies = babyDAO.getBabies();

                if( babies != null && !babies.isEmpty() ){
                    // TODO add preference to determine which baby to return (or maybe logic for the last visible baby)
                    baby = babies.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                babyDAO.close();
            }
            return baby;
        }

        @Override
        protected void onPostExecute(final Baby baby) {
            loadBabyTask = null;

            if (baby != null) {
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        protected void onCancelled() {
            loadBabyTask = null;
        }

    }

}
