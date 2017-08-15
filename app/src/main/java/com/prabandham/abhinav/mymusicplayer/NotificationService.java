package com.prabandham.abhinav.mymusicplayer;

/**
 * Created by Abhinav on 17-Mar-17.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NotificationService extends Service {

    Notification status;
    private final String LOG_TAG = "NotificationService";
    static NotificationService notificationService;
    RemoteViews views;
    RemoteViews bigViews;
    private final IBinder binder = new LocalBinder();
    LocalBroadcastManager bManager;
    BroadcastReceiver bReceiver;
    Songs songs;//=new SAAGP(getApplicationContext());
    private MainActivity mainActivity;

    public static NotificationService getInstance()
    {

        return notificationService;
    }

    @Override
    public void onCreate() {


        notificationService=this;
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        notificationService=null;
        //bManager.unregisterReceiver(bReceiver);
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                // showNotification((String)intent.getExtras().get("songid"));
                // Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
            } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
                // Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                mainActivity.prevSong();
                Log.i(LOG_TAG, "Clicked Previous");
            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                // Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();


                if (MainActivity.mediaPlayer.isPlaying()) {
                    // MainActivity.mediaPlayer.pause();
                    views.setImageViewResource(R.id.status_bar_play,
                            R.drawable.play);
                    bigViews.setImageViewResource(R.id.status_bar_play,
                            R.drawable.apollo_holo_dark_play);
                } else {
                    // MainActivity.mediaPlayer.start();
                    views.setImageViewResource(R.id.status_bar_play,
                            R.drawable.pause);
                    bigViews.setImageViewResource(R.id.status_bar_play,
                            R.drawable.apollo_holo_dark_pause);
                }
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                mainActivity.changeButton();
                Log.i(LOG_TAG, "Clicked Play");
            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                // Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
                // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                if(mainActivity!=null)
                mainActivity.nextSong();
                Log.i(LOG_TAG, "Clicked Next");
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                // Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                // mainActivity.stopForegroundAction();
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;
    }
    public void showNotification(String title,String artist,String album,String album_art) {
// Using RemoteViews to bind custom layouts into Notification

        views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);
// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=false;
        options.inSampleSize=2;
        Bitmap bitmap=BitmapFactory.decodeFile(album_art+"",options);
        if(bitmap!=null)
        {
            bigViews.setImageViewBitmap(R.id.status_bar_album_art,bitmap
                    );
            views.setImageViewBitmap(R.id.status_bar_album_art,bitmap);
            views.setViewVisibility(R.id.status_bar_icon, View.GONE);
            views.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);
        }
        else{
            bigViews.setImageViewBitmap(R.id.status_bar_album_art,Constants.getDefaultAlbumArt(this));
            bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.guitar3,options);
            views.setImageViewBitmap(R.id.status_bar_album_art,bitmap);
            views.setViewVisibility(R.id.status_bar_icon, View.GONE);
            views.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);
            //views.setImageViewBitmap(R.id.status_bar_album_art,bitmap);
        }

        Intent notificationIntent = new Intent(this,MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("notificationclick","bottomsheet");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_previous, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.pause);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);

        views.setTextViewText(R.id.status_bar_track_name,title);
      //  views.setTextColor(R.id.status_bar_track_name,getResources().getColor(R.color.black));
        bigViews.setTextViewText(R.id.status_bar_track_name, title);

        views.setTextViewText(R.id.status_bar_artist_name, artist);
      //  views.setTextColor(R.id.status_bar_artist_name,getResources().getColor(R.color.black));
        bigViews.setTextViewText(R.id.status_bar_artist_name,artist);

        bigViews.setTextViewText(R.id.status_bar_album_name, album);

        NotificationCompat.Builder mNotificationBuilder=new NotificationCompat.Builder(this);

        mNotificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        mNotificationBuilder.setCustomContentView(views);
        mNotificationBuilder.setCustomBigContentView(bigViews);
        mNotificationBuilder.setSmallIcon(R.mipmap.musicicon);
        status = mNotificationBuilder.build();
        mNotificationBuilder.setPublicVersion(status);
        status=mNotificationBuilder.build();
        if (Build.VERSION.SDK_INT >=21 )
        {
            status.color = ContextCompat.getColor(getApplicationContext(), R.color.black);
        }
        status.contentIntent =resultPendingIntent;
        status.flags |=Notification.FLAG_AUTO_CANCEL;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

    }
    public void receiveCommand()
    {
        if(MainActivity.mediaPlayer.isPlaying())
        {
            views.setImageViewResource(R.id.status_bar_play,
                    R.drawable.pause);
            bigViews.setImageViewResource(R.id.status_bar_play,
                    R.drawable.apollo_holo_dark_pause);
        }
        else{
            // Toast.makeText(NotificationService.this, "broadcast", Toast.LENGTH_SHORT).show();
            views.setImageViewResource(R.id.status_bar_play,
                    R.drawable.play);
            bigViews.setImageViewResource(R.id.status_bar_play,
                    R.drawable.apollo_holo_dark_play);
        }
        ( (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,status);
        //Toast.makeText(NotificationService.this, "received command", Toast.LENGTH_SHORT).show();


    }

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        NotificationService getService() {
            // Return this instance of MyService so clients can call public methods
            return NotificationService.this;
        }
    }
    public void setCallbacks(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }

}