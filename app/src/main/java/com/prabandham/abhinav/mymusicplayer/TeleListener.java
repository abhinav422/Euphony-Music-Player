package com.prabandham.abhinav.mymusicplayer;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Abhinav on 20-Apr-17.
 */
class TeleListener extends PhoneStateListener {

    private final MainActivity mainActivity;

    public TeleListener(MainActivity mainActivity)
    {

        this.mainActivity=mainActivity;

    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // CALL_STATE_IDLE;
                try {
                    if (!MainActivity.mediaPlayer.isPlaying() && mainActivity.x==1) {
                        mainActivity.changeButton();
                        mainActivity.sendCommand();
                        mainActivity.x=0;
                    }
                }
                catch (Exception e)
                {

                }
                //Toast.makeText(mainActivity, "CALL_STATE_IDLE",Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // CALL_STATE_OFFHOOK;
                //Toast.makeText(mainActivity, "CALL_STATE_OFFHOOK", Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                // CALL_STATE_RINGING
                /*Intent intent=new Intent(mainActivity,MainActivity.class);
                intent.setAction("CALL_STATE_RINGING");
                LocalBroadcastManager.getInstance(mainActivity).sendBroadcast(intent);*/
                try {
                    if (MainActivity.mediaPlayer.isPlaying()) {
                        //Toast.makeText(mainActivity, "pause", Toast.LENGTH_LONG).show();
                        mainActivity.changeButton();
                        mainActivity.sendCommand();
                        mainActivity.x = 1;
                    }
                }catch (Exception e)
                {

                }
                //Toast.makeText(mainActivity, "CALL_STATE_RINGING",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}