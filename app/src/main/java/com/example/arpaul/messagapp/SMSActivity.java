package com.example.arpaul.messagapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.arpaul.messagapp.Adapter.SMSListAdapter;
import com.example.arpaul.messagapp.DataObject.SMSDO;
import com.example.arpaul.messagapp.Provider.SMSReadConstants;
import com.example.arpaul.messagapp.Utilities.CustomLoader;

import java.util.ArrayList;

public class SMSActivity extends BaseActivity implements LoaderManager.LoaderCallbacks {

    private RecyclerView rvSMS;
    private SMSListAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private LinearLayout llSMSActivity;

    @Override
    public void initialize() {
        llSMSActivity =	(LinearLayout)inflater.inflate(R.layout.content_sms,null);
        llBody.addView(llSMSActivity, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int hasREAD_SMSPermission = checkSelfPermission( Manifest.permission.READ_SMS );
            int hasSEND_SMSPermission = checkSelfPermission( Manifest.permission.SEND_SMS );
            int hasReadContactsPermission = checkSelfPermission( Manifest.permission.READ_CONTACTS );
            if( hasREAD_SMSPermission != PackageManager.PERMISSION_GRANTED  &&
                    hasSEND_SMSPermission != PackageManager.PERMISSION_GRANTED &&
                    hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS},1);
            } else if( hasREAD_SMSPermission != PackageManager.PERMISSION_GRANTED  &&
                    hasSEND_SMSPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS},1);
            } else if( hasREAD_SMSPermission != PackageManager.PERMISSION_GRANTED  &&
                    hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.READ_CONTACTS},1);
            } else if(hasSEND_SMSPermission != PackageManager.PERMISSION_GRANTED &&
                    hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS},1);
            } else if(hasREAD_SMSPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},1);
            } else if(hasSEND_SMSPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
            } else if(hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
            } else {
                getSupportLoaderManager().initLoader(1, null, SMSActivity.this);
            }
        } else {
            getSupportLoaderManager().initLoader(1, null, SMSActivity.this);
        }

        initialiseControls();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSActivity.this,SendSMSActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            getSupportLoaderManager().initLoader(1, null, SMSActivity.this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        showLoader("Please wait..");
        Uri CONTENT_URI = Uri.parse(SMSReadConstants.CONTENT+"inbox");
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            final ArrayList<SMSDO> lstSms = new ArrayList<>();
            SMSDO objSms = null;

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    objSms = new SMSDO();
                    objSms.setsenderId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                    objSms.setMsg(cursor.getString(cursor.getColumnIndexOrThrow("body")));
                    objSms.setReadState(cursor.getString(cursor.getColumnIndexOrThrow("read")));
                    objSms.setTime(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    lstSms.add(objSms);
                } while (cursor.moveToNext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refresh(lstSms);
                    }
                });
            }
        }
        hideLoader();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void initialiseControls(){
        toolbar = (Toolbar) llSMSActivity.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvSMS = (RecyclerView) llSMSActivity.findViewById(R.id.rvSMS);

        fab = (FloatingActionButton) llSMSActivity.findViewById(R.id.fab);
        /*************Initialising Adapter*******************/
        adapter = new SMSListAdapter(SMSActivity.this, new ArrayList<SMSDO>());
        rvSMS.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
