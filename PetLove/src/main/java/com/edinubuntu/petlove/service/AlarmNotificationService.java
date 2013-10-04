package com.edinubuntu.petlove.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.activity.MarketActivity;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class AlarmNotificationService extends Service {
    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link android.os.IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p/>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link android.content.Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);

        Intent marketIntent = new Intent(getApplicationContext(), MarketActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, marketIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(com.edinubuntu.petlove.R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.market_enter_notification_title))
                .setContentText(getString(R.string.market_enter_notification_message))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, notification);
    }
}
