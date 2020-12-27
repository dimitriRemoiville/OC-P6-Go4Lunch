package com.dimitri.remoiville.go4lunch.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    private String mText;
    private User mSendBy;
    private Date mCreatedAt;

    public Message() { }

    public Message(String text, User sendBy) {
        mText = text;
        mSendBy = sendBy;
        mCreatedAt = new Date();
    }

    public Message(String text, User sendBy, Date createdAt) {
        mText = text;
        mSendBy = sendBy;
        mCreatedAt = createdAt;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public User getSendBy() {
        return mSendBy;
    }

    public void setSendBy(User sendBy) {
        mSendBy = sendBy;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mText='" + mText + '\'' +
                ", mSendBy=" + mSendBy.getFirstName() +
                ", createdAt=" + mCreatedAt +
                '}';
    }
}
