package com.myeverydaybaby;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.myeverydaybaby.fragments.SplashScreenFragment;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getFragmentManager().findFragmentById(R.id.main_frame) == null){
            getFragmentManager().beginTransaction().replace(R.id.main_frame, new SplashScreenFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    
}
