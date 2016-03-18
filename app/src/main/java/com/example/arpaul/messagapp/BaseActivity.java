package com.example.arpaul.messagapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.arpaul.messagapp.Utilities.UnCaughtException;

/**
 * Created by ARPaul on 17-03-2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public LayoutInflater inflater;
    public LinearLayout llBody;
    private ProgressDialog ringProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseActivity.this));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.base);

        inflater 				= 		this.getLayoutInflater();
        llBody                  =       (LinearLayout) findViewById(R.id.llBody);

        initialize();
    }

    public abstract void initialize();

    public void showLoader(String message) {
        ringProgressDialog = ProgressDialog.show(BaseActivity.this, "", message, true);
        ringProgressDialog.setCancelable(false);
    }
    public void hideLoader(){
        if(ringProgressDialog != null && ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();
    }
}
