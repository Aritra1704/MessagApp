package com.example.arpaul.messagapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arpaul.messagapp.DataObject.SMSDO;
import com.example.arpaul.messagapp.R;
import com.example.arpaul.messagapp.SMSDetailActivity;
import com.example.arpaul.messagapp.Utilities.CalendarUtils;
import com.example.arpaul.messagapp.Utilities.StringUtils;

import java.util.ArrayList;

/**
 * Created by ARPaul on 13-03-2016.
 */
public class SMSListAdapter extends RecyclerView.Adapter<SMSListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SMSDO> arrCallDetails;

    public SMSListAdapter(Context context, ArrayList<SMSDO> arrCallDetails) {
        this.context=context;
        this.arrCallDetails = arrCallDetails;
    }

    public void refresh(ArrayList<SMSDO> arrCallDetails) {
        this.arrCallDetails = arrCallDetails;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_detail_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SMSDO objSMSDO = arrCallDetails.get(position);
        //holder.tvContactNo.setText(objSMSDO.getAddress());//objSMSDO.getsenderId()
        holder.tvCallTime.setText(objSMSDO.getAddress());
        holder.tvSMSBody.setText(objSMSDO.getMsg());
        holder.tvSMSAddress.setText(CalendarUtils.getCommaFormattedDateTime(CalendarUtils.getDateTime(objSMSDO.getTime())));

        if(StringUtils.getInt(objSMSDO.getReadState()) == 0){
            holder.mView.setBackgroundColor(context.getResources().getColor(R.color.colorPink));
        } else {
            holder.mView.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SMSDetailActivity.class);
                intent.putExtra("SMS_Detail",objSMSDO);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrCallDetails != null)
            return arrCallDetails.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvContactNo;
        public final TextView tvCallTime;
        public final TextView tvSMSBody;
        public final TextView tvSMSAddress;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvContactNo = (TextView) view.findViewById(R.id.tvContactNo);
            tvCallTime = (TextView) view.findViewById(R.id.tvSMSTime);
            tvSMSBody = (TextView) view.findViewById(R.id.tvSMSBody);
            tvSMSAddress = (TextView) view.findViewById(R.id.tvSMSAddress);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
