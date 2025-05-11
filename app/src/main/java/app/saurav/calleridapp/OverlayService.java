package app.saurav.calleridapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager windowManager;
    private View floatingView;
    private static final String TAG = "OverlayService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String number = intent.getStringExtra("number");
        Log.d(TAG, "Raw incoming number: " + number);

        // Normalize number (remove +91 or any non-digit characters)
        if (number != null) {
            number = number.replaceAll("[^0-9]", ""); // Keep only digits
            if (number.length() > 10) {
                number = number.substring(number.length() - 10); // Get last 10 digits
            }
        } else {
            number = "Unknown";
        }

        Log.d(TAG, "Normalized number: " + number);

        // Lookup in DummyDatabase
        String name = DummyDatabase.getDummyData().getOrDefault(number, "Unknown Caller");

        Log.d(TAG, "Resolved name: " + name);

        // Show the overlay
        floatingView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);
        TextView callerName = floatingView.findViewById(R.id.callerName);
        TextView callerNumber = floatingView.findViewById(R.id.callerNumber); // New
        View closeBtn = floatingView.findViewById(R.id.closeOverlay);
        callerName.setText("Caller: " + name);
        callerNumber.setText(number);
        closeBtn.setOnClickListener(v -> stopSelf());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER | Gravity.CENTER;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

        new Handler().postDelayed(this::stopSelf, 30000); // Remove overlay after 30 sec

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

