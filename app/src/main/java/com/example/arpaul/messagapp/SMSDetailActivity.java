package com.example.arpaul.messagapp;

import android.widget.LinearLayout;

/**
 * Created by ARPaul on 13-03-2016.
 */
public class SMSDetailActivity extends BaseActivity {

    private LinearLayout llSMSDetailActivity;

    @Override
    public void initialize() {
        llSMSDetailActivity =	(LinearLayout)inflater.inflate(R.layout.new_sms_screen,null);
        llBody.addView(llSMSDetailActivity, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }
}
