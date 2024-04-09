package ro.pub.cs.systems.eim.practicaltest01var03;

import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.EQUATION1;
import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.EQUATION2;
import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.actionTypes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class PracticalTest01Var03Service extends Service {

    ProcessingThread processingThread = null;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) Objects.requireNonNull(getSystemService(Context.NOTIFICATION_SERVICE))).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String equation1 = intent.getStringExtra(EQUATION1);
        final String equation2 = intent.getStringExtra(EQUATION2);

        // Crearea și pornirea unui fir de execuție pentru trimiterea mesajelor la intervale regulate
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Trimiteți mesajul cu suma la început
                    sendBroadcastWithDelay(actionTypes[0], equation1);
                    Thread.sleep(5000); // Așteptați 5 secunde

                    // Trimiteți mesajul cu diferența
                    sendBroadcastWithDelay(actionTypes[1], equation2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return Service.START_REDELIVER_INTENT;
    }
    private void sendBroadcastWithDelay(final String action, final String equation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Așteptați 5 secunde
                    Thread.sleep(5000);
                    // Trimiteți mesajul cu difuzare
                    Intent intent = new Intent();
                    intent.setAction(action);
                    intent.putExtra(ProcessingThread.BROADCAST_RECEIVER_EXTRA, equation);
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }
}
class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("ro.pub.cs.systems.eim.practicaltest01.sum")) {
            String message = intent.getStringExtra("ProcessingThread.BROADCAST_RECEIVER_EXTRA");
            Log.d("MyBroadcastReceiver", "Mesaj primit prin difuzare: " + message);
        }
    }
}