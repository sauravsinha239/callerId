package app.saurav.calleridapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class CallService extends Service {

    private static final String CHANNEL_ID = "CALLER_ID_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();

        // Create Notification Channel for Android 8.0 and above
        createNotificationChannel();

        // Create and show the notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Incoming Call")
                .setContentText("Identifying caller...")
//                .setSmallIcon(R.drawable.ic_call) // Use your own icon
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Start the service in the foreground
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String number = intent.getStringExtra("number");
        if (number == null) {
            number = "Unknown"; // Fallback if no number is available
        }

        // Start the overlay service with caller number
        Intent overlayIntent = new Intent(this, OverlayService.class);
        overlayIntent.putExtra("number", number);
        startService(overlayIntent);

        // Handle the service's start (this method is called when the service is started)
        return START_STICKY;
        // Keeps the service running in case of crash
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Create Notification Channel for Android 8.0 and above
    private void createNotificationChannel() {
        CharSequence name = "Caller ID Notification";
        String description = "Notification for incoming calls";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
