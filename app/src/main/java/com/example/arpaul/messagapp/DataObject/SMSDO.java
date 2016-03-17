package com.example.arpaul.messagapp.DataObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ARPaul on 13-03-2016.
 */
public class SMSDO implements Parcelable{

    private String _senderid;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _folderName;

    public String getsenderId() {
        return _senderid;
    }

    public String getAddress() {
        return _address;
    }

    public String getMsg() {
        return _msg;
    }

    public String getReadState() {
        return _readState;
    }

    public String getTime() {
        return _time;
    }

    public String getFolderName() {
        return _folderName;
    }


    public void setsenderId(String senderid) {
        _senderid = senderid;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setMsg(String msg) {
        _msg = msg;
    }

    public void setReadState(String readState) {
        _readState = readState;
    }

    public void setTime(String time) {
        _time = time;
    }

    public void setFolderName(String folderName) {
        _folderName = folderName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_senderid);
        dest.writeString(_address);
        dest.writeString(_msg);
        dest.writeString(_readState);
        dest.writeString(_time);
        dest.writeString(_folderName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*public SMSDO(Parcel in){
        this._senderid = in.readString();
        this._address = in.readString();
        this._msg = in.readString();
        this._readState = in.readString();
        this._time = in.readString();
        this._folderName = in.readString();
    }*/

    public static final Parcelable.Creator<SMSDO> CREATOR = new Parcelable.Creator<SMSDO>() {

        @Override
        public SMSDO createFromParcel(Parcel source) {
            SMSDO objSms = new SMSDO();
            objSms._senderid = source.readString();
            objSms._address = source.readString();
            objSms._msg = source.readString();
            objSms._readState = source.readString();
            objSms._time = source.readString();
            objSms._folderName = source.readString();
            return objSms;
        }

        @Override
        public SMSDO[] newArray(int size) {
            return new SMSDO[size];
        }
    };
}
