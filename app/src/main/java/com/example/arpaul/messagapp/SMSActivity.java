package com.example.arpaul.messagapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.arpaul.messagapp.Adapter.SMSListAdapter;
import com.example.arpaul.messagapp.DataObject.SMSDO;
import com.example.arpaul.messagapp.Provider.SMSReadConstants;

import java.util.ArrayList;
import java.util.List;

public class SMSActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private RecyclerView rvSMS;
    private SMSListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSActivity.this,SendSMSActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = Uri.parse(SMSReadConstants.CONTENT+"inbox");
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            ArrayList<SMSDO> lstSms = new ArrayList<>();
            SMSDO objSms = null;

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    objSms = new SMSDO();
                    objSms.setsenderId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                    objSms.setMsg(cursor.getString(cursor.getColumnIndexOrThrow("body")));
                    objSms.setReadState(cursor.getString(cursor.getColumnIndex("read")));
                    objSms.setTime(cursor.getString(cursor.getColumnIndexOrThrow("date")));

                    lstSms.add(objSms);
                } while (cursor.moveToNext());
                adapter.refresh(lstSms);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void initialiseControls(){
        rvSMS = (RecyclerView) findViewById(R.id.rvSMS);

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
