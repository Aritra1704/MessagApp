package com.example.arpaul.messagapp.DataObject;

/**
 * Created by ARPaul on 13-03-2016.
 */
public class SMSDO {

    private String _senderid;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _folderName;

    public String getId() {
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

}
