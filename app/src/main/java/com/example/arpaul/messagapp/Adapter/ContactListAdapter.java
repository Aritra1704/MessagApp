package com.example.arpaul.messagapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arpaul.messagapp.R;

import java.util.ArrayList;

/**
 * Created by ARPaul on 13-03-2016.
 */
public class ContactListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mTravelDOs = new ArrayList<>();

    public ContactListAdapter(Context context, ArrayList<String> listTravelDOs){
        this.context = context;
        mTravelDOs = listTravelDOs;
    }

    public void refresh(ArrayList<String> listTravelDOs){
        mTravelDOs = listTravelDOs;
        notifyDataSetChanged();
    }

    public void clear() {
        mTravelDOs.clear();
    }

    public void addItem(String yourObject) {
        mTravelDOs.add(yourObject);
    }

    public void addItems(ArrayList<String> yourObjectList) {
        mTravelDOs.addAll(yourObjectList);
    }

    @Override
    public int getCount() {
        return mTravelDOs.size();
    }

    @Override
    public Object getItem(int position) {
        return mTravelDOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = LayoutInflater.from(context).inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = LayoutInflater.from(context).inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mTravelDOs.size() ? mTravelDOs.get(position) : "";
    }
}
