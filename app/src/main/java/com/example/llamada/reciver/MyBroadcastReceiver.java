package com.example.llamada.reciver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.llamada.MainActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        final String SMS = MainActivity.intent.getStringExtra("responseText");
        final String Tel = MainActivity.intent.getStringExtra("responseTel");



        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                Service.TELEPHONY_SERVICE
        );

        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    if (phoneNumber.equals("+52" + Tel)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Tel, null, SMS, null, null);
                        Toast.makeText(context, "Mensaje envíado", Toast.LENGTH_LONG).show();
                    } else if (phoneNumber.equals(Tel)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Tel, null, SMS, null, null);
                        Toast.makeText(context, "Mensaje envíado", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "El número NO es igual : " +
                                Tel + "\n a este : " +
                                phoneNumber, Toast.LENGTH_LONG).show();

                    }
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
