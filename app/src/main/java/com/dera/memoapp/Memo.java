package com.dera.memoapp;
import com.dera.memoapp.util.MemoAndReport;

/**
 * Created by Chidera Nwoke on 5/11/2017.
 */



public class Memo extends MemoAndReport {
    private String subject, sender, body;

    public Memo(){
    }
    public Memo(String subject, String sender, String body) {
        this.subject = subject;
        this.sender = sender;
        this.body = body;
    }

    public String getsubject() {
        return subject;
    }

    public void setsubject(String subject) {
        this.subject = subject;
    }

    public String getbody() {
        return body;
    }

    public void setbody(String message) {
        this.body = body;
    }

    public String getsender() {
        return sender;
    }

    public void setsender(String sender) {
        this.sender = sender;
    }
}
