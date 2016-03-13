package com.example.arpaul.messagapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.arpaul.messagapp.Adapter.ContactListAdapter;

import java.util.ArrayList;

/**
 * Created by ARPaul on 13-03-2016.
 */
public class SendSMSActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private ArrayList<String> arrContactNo;
    private Spinner toolbar_spinner;
    private EditText edtBody;
    private Button btnSend;
    private String phoneNumber = "";
    private ProgressDialog ringProgressDialog;
    private ContactListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.new_sms_screen);
        inititaliseControls();

        toolbar_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                phoneNumber = arrContactNo.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(edtBody.getText().toString())){

                    String smsBody = edtBody.getText().toString();

                    String SMS_SENT = "SMS_SENT";
                    String SMS_DELIVERED = "SMS_DELIVERED";

                    PendingIntent sentPendingIntent = PendingIntent.getBroadcast(SendSMSActivity.this, 0, new Intent(SMS_SENT), 0);
                    PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(SendSMSActivity.this, 0, new Intent(SMS_DELIVERED), 0);

// For when the SMS has been sent
                    registerReceiver(smsSentReceiver, new IntentFilter(SMS_SENT));

// For when the SMS has been delivered
                    registerReceiver(smsDeliveryReceiver, new IntentFilter(SMS_DELIVERED));

// Get the default instance of SmsManager
                    SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS
                    smsManager.sendTextMessage(phoneNumber, null, smsBody, sentPendingIntent, deliveredPendingIntent);
                }
            }
        });
    }

    BroadcastReceiver smsSentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    /*notifyUser("SMS sent successfully");*/
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    notifyUser("Generic failure cause.");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    notifyUser("Service is currently unavailable.");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    notifyUser("No pdu provided.");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    notifyUser("Radio was explicitly turned off.");
                    break;
            }
        }
    };

    BroadcastReceiver smsDeliveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    //notifyUser("SMS delivered");
                    break;
                case Activity.RESULT_CANCELED:
                    notifyUser("SMS not delivered");
                    break;
            }
        }
    };

    public void notifyUser(String message){

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(SendSMSActivity.this, SendSMSActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //use the flag FLAG_UPDATE_CURRENT to override any notification already there
        PendingIntent contentIntent = PendingIntent.getActivity(SendSMSActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(SendSMSActivity.this);
        builder.setAutoCancel(false);
        builder.setTicker("New SMS received.");
        builder.setContentTitle("Failure");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.sms);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        builder.setLargeIcon(icon);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        //builder.setNumber(100);
        builder.build();

        Notification notification = builder.getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
        notificationManager.notify(11, notification);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        showLoader();
        return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            arrContactNo = new ArrayList<>();

            ContentResolver cr = getContentResolver();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        System.out.println("name : " + name + ", ID : " + id);
                        // get the phone number
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            System.out.println("phone" + phone);
                            arrContactNo.add(phone);
                        }
                        pCur.close();
                    }
                } while (cursor.moveToNext());
                adapter.refresh(arrContactNo);
            }
        }
        hideLoader();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public void showLoader() {
        ringProgressDialog = ProgressDialog.show(SendSMSActivity.this, "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
    }
    private void hideLoader(){
        if(ringProgressDialog != null && ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();
    }

    private void inititaliseControls(){
        toolbar_spinner     = (Spinner) findViewById(R.id.toolbar_spinner);
        edtBody             = (EditText) findViewById(R.id.edtBody);
        btnSend             = (Button) findViewById(R.id.btnSend);

        adapter = new ContactListAdapter(SendSMSActivity.this, new ArrayList<String>());

        toolbar_spinner = (Spinner) findViewById(R.id.toolbar_spinner);
        toolbar_spinner.setAdapter(adapter);

    }
}
