package com.prabandham.abhinav.mymusicplayer;

import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
import it.beppi.knoblibrary.Knob;

/**
 * Created by Abhinav on 27-Mar-17.
 */
public class EqualizerFragment extends Fragment {
    SeekBar seekBar;
    SeekBar seekBar2;
    SeekBar seekBar3;
    SeekBar seekBar4;
    SeekBar seekBar5;
    MaterialSpinner spinner;
    MainActivity mainActivity;
    Equalizer mEqualizer;
    Knob knob;
    int volumeIndex;
    AudioManager audioManager = null;
    RelativeLayout panel;
    Singleton m_Inst = Singleton.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.equalizer_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner=(MaterialSpinner)view.findViewById(R.id.spinner);
        seekBar=(SeekBar)view.findViewById(R.id.seekBar2);
        seekBar2=(SeekBar)view.findViewById(R.id.seekBar3);
        seekBar3=(SeekBar)view.findViewById(R.id.seekBar4);
        seekBar4=(SeekBar)view.findViewById(R.id.seekBar5);
        seekBar5=(SeekBar)view.findViewById(R.id.seekBar6);
        knob=(Knob)view.findViewById(R.id.knob);

        panel=(RelativeLayout)view.findViewById(R.id.rl4);
        mainActivity=(MainActivity)getActivity();
        mEqualizer=mainActivity.mEqualizer;
        mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
       /* RoundKnobButton rv = new RoundKnobButton(mainActivity.getApplicationContext(), R.drawable.stator, R.drawable.rotoron, R.drawable.rotoroff,
                400,400);
       RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        panel.addView(rv, lp);*/
        audioManager = (AudioManager) mainActivity.getSystemService(getContext().AUDIO_SERVICE);
        //rv.setRotorPercentage(100);
        volumeIndex=audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        knob.setNumberOfStates(volumeIndex);
        knob.setState(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
       // rv.setRotorPosAngle((float)(3*((audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)*100)/volumeIndex))-150);
        knob.setOnStateChanged(new Knob.OnStateChanged() {
            @Override
            public void onState(int state) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (state), 0);
            }
        });
        /* rv.SetListener(new RoundKnobButton.RoundKnobButtonListener() {
            public void onStateChange(boolean newstate) {
                //Toast.makeText(mainActivity,  "New state:"+newstate,  Toast.LENGTH_SHORT).show();
            }

            public void onRotate(final int percentage) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        (int)((percentage*volumeIndex)/100), 0);
            }
        });*/
       /* try {
            croller = (Croller) view.findViewById(R.id.croller);
            audioManager = (AudioManager) mainActivity.getSystemService(getContext().AUDIO_SERVICE);
            croller.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            croller.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress) {
                    // use the progress
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {

        }*/
        short numberFrequencyBands=mEqualizer.getNumberOfBands();
        final short  lowerEqualizerBandLevel=mEqualizer.getBandLevelRange()[0];
        final short  upperEqualizerBandLevel=mEqualizer.getBandLevelRange()[1];
        for(short i=0;i<numberFrequencyBands;i++)
        {
            final short equalizerBandIndex=i;
            if(i==0)
            {
             setSeekBar(seekBar,i,upperEqualizerBandLevel,lowerEqualizerBandLevel,equalizerBandIndex);
            }
            else if(i==1)
            {
                setSeekBar(seekBar2,i,upperEqualizerBandLevel,lowerEqualizerBandLevel,equalizerBandIndex);

            }
            else if(i==2)
            {
                setSeekBar(seekBar3,i,upperEqualizerBandLevel,lowerEqualizerBandLevel,equalizerBandIndex);

            }
            else if(i==3)
            {
                setSeekBar(seekBar4,i,upperEqualizerBandLevel,lowerEqualizerBandLevel,equalizerBandIndex);

            }
            else if(i==4)
            {
                setSeekBar(seekBar5,i,upperEqualizerBandLevel,lowerEqualizerBandLevel,equalizerBandIndex);

            }

        }
        equalizeSound();
    }

    private void equalizeSound() {
        ArrayList<String> equalizerPresetNames=new ArrayList<String>();
        ArrayAdapter<String> equalizerPresetSpinnerAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_text,R.id.textV1,equalizerPresetNames);
        equalizerPresetSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        for(short i=0;i<mEqualizer.getNumberOfPresets();i++)
        {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));

        }
        spinner.setAdapter(equalizerPresetSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("spinner item",Integer.toString(position));
                try {
                    mEqualizer.usePreset((short)position);
                }
                catch (UnsupportedOperationException e)
                {
                   // mEqualizer.usePreset((short)0);
                }
                catch(IllegalArgumentException e1)
                {
                    mEqualizer.usePreset((short)0);
                }
                short numberFrequencyBands=mEqualizer.getNumberOfBands();
                final short lowerEqualizerBandLevel=mEqualizer.getBandLevelRange()[0];
                for(short i=0;i<numberFrequencyBands;i++)
                {
                    short equalizerBandIndex=i;
                    if(i==0)
                    {
                        //setSeekBar(seekBar,i,upperEqualizerBandLevel,lowerEqualizerBandLevel,equalizerBandIndex);
                        seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);
                    }
                    else if(i==1)
                    {
                        seekBar2.setProgress(mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);

                    }
                    else if(i==2)
                    {
                        seekBar3.setProgress(mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);

                    }
                    else if(i==3)
                    {
                        seekBar4.setProgress(mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);

                    }
                    else if(i==4)
                    {
                        seekBar5.setProgress(mEqualizer.getBandLevel(equalizerBandIndex)-lowerEqualizerBandLevel);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSeekBar(SeekBar seekBar, int i, short upper, final short lower, final short equalizerBandIndex)
    {
        seekBar.setId(i);
        seekBar.setMax(upper-lower);
        seekBar.setProgress(mEqualizer.getBand(equalizerBandIndex));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mEqualizer.setBandLevel(equalizerBandIndex,(short)(progress+lower));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
