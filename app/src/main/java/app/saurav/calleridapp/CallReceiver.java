package app.saurav.calleridapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            Log.d("CallReceiver", "Phone State: " + state);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d("CallReceiver", "Incoming Number: " + incomingNumber);

            if (incomingNumber != null) {
                Intent overlayIntent = new Intent(context, OverlayService.class);
                overlayIntent.putExtra("number", incomingNumber);
                context.startForegroundService(overlayIntent); // IMPORTANT

            }
        }
    }
}

