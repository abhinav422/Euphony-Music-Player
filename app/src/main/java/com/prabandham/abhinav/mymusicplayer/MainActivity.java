package com.prabandham.abhinav.mymusicplayer;


import android.content.BroadcastReceiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends FragmentActivity implements SelectedItem,OnClickInFragment,MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnPreparedListener {

    public static MediaPlayer mediaPlayer;
    SAAGP saagp;
    FragmentParent fragmentParent=new FragmentParent();
    ArrayList<String> title_key;
    SeekBar seekBar;
    ImageButton play_pause;
    int x=0;
    ImageButton back;
    boolean bound=false;
    ImageButton next;
    ServiceConnection serviceConnection;
    Equalizer mEqualizer;
    ArrayList<String> item_selected;
    Handler handler=new Handler();
    TextView textView2;
    ImageButton viewflip_more;
    FragmentTransaction ft;
    TextView textView;
    NotificationService notificationService;
    EqualizerFragment equalizerFragment=new EqualizerFragment();
    boolean flag=false;
    ViewFlipper viewFlipper;
    ViewPager viewPager;
    TextView textView3;
    RelativeLayout playScreenrl;
    BottomSheetBehavior mBottomSheetBehavior;
    BottomSheetBehavior.BottomSheetCallback bottomSheetCallback;
    Runnable runnable;
    int n;
    ImageButton viewflip_play_pause;
    LocalBroadcastManager bManager;
    BroadcastReceiver bReceiver;
    private Cursor selectedCursor;

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void bindService()
    {
        if(!bound ) {
            Intent serviceIntent = new Intent(MainActivity.this, NotificationService.class);
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            //serviceIntent.putExtra("songid", customStatePagerAdapter.songs.get(viewPager.getCurrentItem()));
            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName className, IBinder service) {
                    // cast the IBinder and get MyService instance
                    NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
                    notificationService = binder.getService();
                    notificationService.setCallbacks(MainActivity.this);
                    bound = true;
                    //myService.setCallbacks(MyActivity.this); // register
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {

                    // Toast.makeText(MainActivity.this, "bound false", Toast.LENGTH_SHORT).show();
                    bound = false;
                }
            };
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void setBackground(View v,int n)
    {
        if(n==0)
        {
            v.setBackgroundResource(R.drawable.blurred9);

        }
        else if(n==1)
        {
            v.setBackgroundResource(R.drawable.blurred10);
        }
        else if(n==2)
        {
            v.setBackgroundResource(R.drawable.blurred2);
        }
        else if(n==3)
        {
            v.setBackgroundResource(R.drawable.blurred3);
        }
        else if(n==4)
        {
            v.setBackgroundResource(R.drawable.blurred8);

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bindService();
        Random random=new Random();
        n= random.nextInt(5);
        setActivityBackground(n);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.parent_fragment);
        mediaPlayer=new MediaPlayer();
        saagp=new SAAGP(getApplicationContext());
        try {
            saagp.setThDuration(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_edittext", "5000"));
        }
        catch(Exception e)
        {
            saagp.setThDuration("5000");
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_check",false))
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_check",true)){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        title_key=saagp.getTitleKeys();

        //customStatePagerAdapter=new CustomStatePagerAdapter(getSupportFragmentManager());

        final RelativeLayout bottomSheet = (RelativeLayout) findViewById( R.id.bottomsheet );
       // final RelativeLayout mainrl = (RelativeLayout) findViewById( R.id.mainrl );
        viewPager=(ViewPager)findViewById(R.id.viewpager2);
        textView=(TextView)findViewById(R.id.textView);
        textView.setSelected(true);
        textView.setText("Total Songs : "+new Integer(title_key.size()).toString());



        seekBar=(SeekBar)findViewById(R.id.seekBar);
        //seekBar.setOnSeekBarChangeListener(this);
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper);
        viewFlipper.setDisplayedChild(1);
        textView2=(TextView)findViewById(R.id.textView16);
        textView3=(TextView)findViewById(R.id.textView15);
        play_pause=(ImageButton)findViewById(R.id.imageButton);
        back=(ImageButton)findViewById(R.id.imageButton2);
        next=(ImageButton)findViewById(R.id.imageButton3);
        play_pause.setOnClickListener(this);
        playScreenrl=(RelativeLayout)findViewById(R.id.rl1);
        playScreenrl.setOnClickListener(this);

        setBackground(viewPager,n);

        viewflip_play_pause=(ImageButton)findViewById(R.id.imageButton9);
        viewflip_play_pause.setOnClickListener(this);
        viewflip_more=(ImageButton)findViewById(R.id.imageButton4);
        viewflip_more.setOnClickListener(this);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        viewPager.setOnClickListener(this);
        viewPager.setClickable(true);
        mBottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        DisplayMetrics displayMetrics= getResources().getDisplayMetrics();
        final int density=displayMetrics.densityDpi;
        mBottomSheetBehavior.setPeekHeight( getBottomSheetHeight(50));
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                   // String serviceJsonString = intent.getStringExtra("json");
                    //Do something with the string
                    if(viewPager.getCurrentItem()!=viewPager.getAdapter().getCount()-1)
                    {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                }
                else if(intent.getAction().equals(Constants.ACTION.PREV_ACTION))
                {
                    if(viewPager.getCurrentItem()!=0)
                    {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                    }
                }
                else if(intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION))
                {
                    mediaPlayer.release();
                    handler.removeCallbacks(runnable);
                    if (bound) {
                        notificationService.setCallbacks(null); // unregister
                        unbindService(serviceConnection);
                        bound = false;
                    }
                    finish();
                }
                else if(intent.getAction().equals(Constants.ACTION.PLAY_ACTION))
                {
                    changeButton();
                }
                else if(intent.getAction().equals("CALL_STATE_RINGING"))
                {
                    Toast.makeText(getApplicationContext(),"ringing",Toast.LENGTH_SHORT).show();
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        changeButton();
                    }
                }



            }
        };

        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION.NEXT_ACTION);
        intentFilter.addAction(Constants.ACTION.PLAY_ACTION);
        intentFilter.addAction(Constants.ACTION.PREV_ACTION);
        intentFilter.addAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        bManager.registerReceiver(bReceiver, intentFilter);
        bottomSheetCallback=new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

               try {
                   if (mediaPlayer.isPlaying()) {
                       ((ViewFlipper) bottomSheet.findViewById(R.id.viewFlipper)).findViewById(R.id.imageButton9).setBackgroundResource(R.drawable.custom2);
                   } else
                       ((ViewFlipper) bottomSheet.findViewById(R.id.viewFlipper)).findViewById(R.id.imageButton9).setBackgroundResource(R.drawable.custom);
               }
               catch (Exception e)
               {

               }
                if(newState==BottomSheetBehavior.STATE_COLLAPSED)
                {
                    ((ViewFlipper)bottomSheet.findViewById(R.id.viewFlipper)).setDisplayedChild(1);

                }

                else if(newState==BottomSheetBehavior.STATE_EXPANDED){

                    ((ViewFlipper) bottomSheet.findViewById(R.id.viewFlipper)).setDisplayedChild(0);
                        flag=false;

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {

            }
        };
        mBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ViewPagerAdapter viewPagerAdapter=(ViewPagerAdapter)viewPager.getAdapter();
                String title=viewPagerAdapter.getTitle(position);
                String artist=viewPagerAdapter.getArtist(position);
                String album=viewPagerAdapter.getAlbum(position);
                String albumart=viewPagerAdapter.getAlbumArt(position);
                textView.setText(title);
                String path=viewPagerAdapter.getPath(position);
                String duration=viewPagerAdapter.getDuration(position);
                try {
                    seekBar.setMax(Integer.parseInt(duration));
                    textView2.setText(String.format("%02d:%02d",
                            ((Long.parseLong(duration)/1000)/60),
                            (00+((Long.parseLong(duration)/1000)%60))));
                }catch (NumberFormatException e)
                {
                    seekBar.setMax(0);
                    textView2.setText("00:00");
                }
                seekBar.setProgress(0);

                createMediaPlayer(path,title,artist,album,albumart);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mainrl,fragmentParent).commit();

        runnable=new Runnable() {
            @Override
            public void run() {

                try {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    textView3.setText(String.format("%02d:%02d",
                            ((mediaPlayer.getCurrentPosition() / 1000) / 60),
                            (00 + ((mediaPlayer.getCurrentPosition() / 1000) % 60))));
                }catch (IllegalStateException e)
                {
                   // mediaPlayer.reset();
                }
                handler.postDelayed(runnable,1000);
            }

        };

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(this), PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void createMediaPlayer(String path, final String title, final String artist, final String album, final String albumart) {


                try {

                    MainActivity.mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepareAsync();
                }
                catch (Exception e)
                {
                    mediaPlayer.release();
                    mediaPlayer=MediaPlayer.create(getApplicationContext(),Uri.parse(path));

                }


                if(mediaPlayer!=null)
                {
                    mediaPlayer.setOnPreparedListener(MainActivity.this);
                    try {

                        seekBar.setOnSeekBarChangeListener(MainActivity.this);
                        play_pause.setBackgroundResource(R.drawable.custom2);
                        viewflip_play_pause.setBackgroundResource(R.drawable.custom2);
                        seekBar.setProgress(0);
                        handler.postDelayed(runnable, 1000);
                        startService(title,artist,album,albumart);
                        try {
                            if(mEqualizer!=null)
                            {
                                mEqualizer.setEnabled(false);
                            }
                            mEqualizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
                            mEqualizer.setEnabled(true);
                        }
                        catch (Exception e)
                        {

                        }
                        if (equalizerFragment.isVisible()) {

                            getSupportFragmentManager().popBackStack();
                        }
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    catch (Exception e)
                    {

                    }


                }




    }

    private void setActivityBackground(int n) {
        BitmapDrawable bitmapDrawable;
        if( n==0)
        {

            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred9,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);

        }
        else if(n==1)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred10,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
        else if(n==2)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred2,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
        else if(n==3)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred3,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
        else if(n==4)
        {
            bitmapDrawable=new BitmapDrawable(getResources(),MainActivity.decodeSampledBitmapFromResource(getResources(),R.drawable.blurred8,100,100));
            getWindow().setBackgroundDrawable(bitmapDrawable);
        }
    }

    private void initialize()
    {

    }
    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        if(intent!=null)
        {
        if(intent.getExtras()!=null) {
            if(intent.getExtras().get("notificationclick")!=null)
            {
            if (intent.getExtras().get("notificationclick").toString().equals("bottomsheet")) ;
                {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            }
        }
    }

    public void startEqualizer() {

        if(!equalizerFragment.isVisible()) {
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.mainrl, equalizerFragment).addToBackStack(null).commit();
        }
    }

    private int getBottomSheetHeight(int dp) {
        DisplayMetrics displayMetrics =getResources().getDisplayMetrics();
        double a=displayMetrics.densityDpi/160;
        return (int)Math.round(dp*a);

    }

    public void changeButton()
    {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                play_pause.setBackgroundResource(R.drawable.custom);
                viewflip_play_pause.setBackgroundResource(R.drawable.custom);
            } else {
                mediaPlayer.start();
                play_pause.setBackgroundResource(R.drawable.custom2);
                viewflip_play_pause.setBackgroundResource(R.drawable.custom2);
            }
        }catch (Exception e)
        {

        }
    }

    @Override
    public void onBackPressed()
    {
        if(mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
       else if(fragmentParent.isVisible()) {

          moveTaskToBack(true);
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            notificationService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
        bManager.unregisterReceiver(bReceiver);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

       // Toast.makeText(MainActivity.this, "on stop", Toast.LENGTH_SHORT).show();
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics =getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    @Override
    public void initiateSong(Cursor cursor,int position)
    {
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),cursor,this));
        viewPager.setCurrentItem(position);
        ViewPagerAdapter viewPagerAdapter=(ViewPagerAdapter)viewPager.getAdapter();
        String title=viewPagerAdapter.getTitle(position);
        String artist=viewPagerAdapter.getArtist(position);
        String album=viewPagerAdapter.getAlbum(position);
        String albumart=viewPagerAdapter.getAlbumArt(position);
        textView.setText(title);
        String path=viewPagerAdapter.getPath(position);
        String duration=viewPagerAdapter.getDuration(position);
        try {
            seekBar.setMax(Integer.parseInt(duration));
            textView2.setText(String.format("%02d:%02d",
                    ((Long.parseLong(duration)/1000)/60),
                    (00+((Long.parseLong(duration)/1000)%60))));
        }catch (NumberFormatException e)
        {
            seekBar.setMax(0);
            textView2.setText("00:00");
        }
        seekBar.setProgress(0);

        createMediaPlayer(path,title,artist,album,albumart);
    }

    /*public int createMediaPlayer() {

        play_pause.setBackgroundResource(R.drawable.custom2);
        viewflip_play_pause.setBackgroundResource(R.drawable.custom2);
        seekBar.setMax(Integer.parseInt(saagp.getDuration(customStatePagerAdapter.songs.get(viewPager.getCurrentItem()))));
        seekBar.setProgress(0);
        String path=saagp.getPath(customStatePagerAdapter.songs.get(viewPager.getCurrentItem()));

        try {
            if (MainActivity.mediaPlayer.isPlaying()) {
                MainActivity.mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        }
        catch (Exception e)
        {
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(getApplicationContext(),Uri.parse(path));
            if(mediaPlayer!=null) {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextSong();
                    }
                });
            }
        }


        if(mediaPlayer!=null) {
            mediaPlayer.start();

            //startService();
            try {
                mEqualizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
                mEqualizer.setEnabled(true);
            }
            catch (Exception e)
            {

            }
            handler.postDelayed(runnable, 1000);
            if (equalizerFragment.isVisible()) {

                getSupportFragmentManager().popBackStack();
                //Toast.makeText(this, "Equalizer visible", Toast.LENGTH_SHORT).show();
            }

        }
        //createNotification();
            return 1;
    }*/

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            MainActivity.mediaPlayer.seekTo(progress);
            textView3.setText(String.format("%02d:%02d",
                    ((mediaPlayer.getCurrentPosition()/1000)/60),
                    (00+((mediaPlayer.getCurrentPosition()/1000)%60))));
        }
        else
            seekBar.setProgress(progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.postDelayed(runnable,1000);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageButton)
        {
            changeButton();
            sendCommand();
        }
       else if(v.getId()==R.id.imageButton3)
        {
            nextSong();
        }
       else if(v.getId()==R.id.imageButton2)
        {
            prevSong();
        }
        else if(v.getId()==R.id.imageButton9)
        {
            changeButton();
           //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(this,MainActivity.class).setAction("Receive"));
           sendCommand();

        }
        else if(v.getId()==R.id.imageButton4)
        {

            final PopupMenu popupMenu=new PopupMenu(getApplicationContext(),viewflip_more);
            popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
            if(viewPager.getAdapter()!=null) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ViewPagerAdapter viewPagerAdapter=(ViewPagerAdapter)viewPager.getAdapter();
                        if (item.getItemId() == R.id.atp) {
                            //setItemSelected(customStatePagerAdapter.songs.get(viewPager.getCurrentItem()));
                            setCursor(viewPagerAdapter.getCursorAtPosition(viewPager.getCurrentItem()));
                            AddToPlaylist addToPlaylist = new AddToPlaylist(MainActivity.this);
                            addToPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            addToPlaylist.show();

                        } else if (item.getItemId() == R.id.gtal) {
                            try {
                                AlbumSongs albumSongs =AlbumSongs.getInstance(viewPagerAdapter.getAlbumKey(viewPager.getCurrentItem()));
                                 // albumSongs.setAlbum_key(saagp.getAlbumKeyForSong(customStatePagerAdapter.songs.get(viewPager.getCurrentItem())));
                                //albumSongs.setPosition(position);
                                ft = getSupportFragmentManager().beginTransaction();
                                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                                ft.replace(R.id.mainrl, albumSongs).addToBackStack(null).commit();
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Album not Found",Toast.LENGTH_SHORT).show();
                            }
                        } else if (item.getItemId() == R.id.gtar) {
                            try {
                                ArtistSong artistSong = ArtistSong.getInstance(viewPagerAdapter.getArtistId(viewPager.getCurrentItem()),viewPagerAdapter.getArtist(viewPager.getCurrentItem()),new ArtistTab());
                                ft = getSupportFragmentManager().beginTransaction();
                                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                ft.replace(R.id.mainrl, artistSong).addToBackStack(null).commit();
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Artist not Found",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(item.getItemId()==R.id.eql)
                        {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            startEqualizer();
                        }
                        return true;
                    }
                });
            }
            popupMenu.show();
        }
        else if(v.getId()==R.id.rl1)
        {
            if(mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED)
            {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            else
            {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }

    public void sendCommand() {
        if(bound==true && viewPager.getAdapter()!=null)
            notificationService.receiveCommand();
    }

    @Override
    public ArrayList<String> getItemSelected()
    {
        return item_selected;
    }

    @Override
    public void setItemSelected(ArrayList<String> item_selected) {
        this.item_selected=item_selected;
    }

    @Override
    public void setItemSelected(String item_selected) {
        this.item_selected=new ArrayList<>();
        this.item_selected.add(item_selected);
    }


    @Override
    public void createPlaylist()
    {

        CreatePlaylist createPlaylist=new CreatePlaylist(this);

        createPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createPlaylist.show();
    }

    @Override
    public Cursor getCursor() {
        return selectedCursor;
    }

    @Override
    public void setCursor(Cursor cursor) {
        if(selectedCursor!=null)
        {
            selectedCursor.close();
        }
        selectedCursor=cursor;
    }

    public void startService(String title, String artist, String album, String albumart) {


        notificationService.setCallbacks(MainActivity.this);
        notificationService.showNotification(title,artist,album,albumart);
        // startService(serviceIntent);

    }


    public void nextSong() {
        //Do something with the string
        if(viewPager.getAdapter()!=null) {
            if (viewPager.getCurrentItem() != viewPager.getAdapter().getCount()-1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        }
    }

    public void prevSong() {
        if(viewPager.getAdapter()!=null) {
            if (viewPager.getCurrentItem() != 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK)
        {
            if(data.getIntExtra("fullscreen",0)==1)
            {
                Log.v("mainactivity","fullscreen change");
                if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_check",false))
                {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                else if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_check",true)){
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
            }
            if(data.getIntExtra("edittext",0)==1)
            {
                Log.v("mainactivity","threshold change");
                saagp.setThDuration(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_edittext","5000"));
                SongTab songTab = (SongTab) fragmentParent.customPagerAdapter.instantiateItem(fragmentParent.viewpager, 0);
                if(songTab!=null)
                songTab.notifyDataSetChanged();
            }
            if(data.getIntExtra("listpref",0)==1)
            {
                Log.v("mainactivity","background change");
                n=Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_list","1"));
                setActivityBackground(n);
                setBackground(viewPager,n);

            }
        }
    }



    public void startSetting() {
       Intent intent=new Intent(this,SettingsFragment.class);
        intent.putExtra("background",n);
        startActivityForResult(intent,1);
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public void notifyViewPager()
    {
        if(viewPager.getAdapter()!=null)
        {
            ViewPagerAdapter viewPagerAdapter=(ViewPagerAdapter)viewPager.getAdapter();
            viewPagerAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v("media player complete","next");
        nextSong();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.v("media player complete","next");
                nextSong();
            }
        });
    }
}



