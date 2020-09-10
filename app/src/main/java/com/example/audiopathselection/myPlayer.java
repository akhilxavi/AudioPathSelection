package com.example.audiopathselection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class myPlayer extends AppCompatActivity {

    MediaPlayer myMediaPlayer;
    AudioManager myAudioManager;

    public void playNow(View view){
        myMediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_player);

        Intent myIntent = getIntent();

        Toast myToast = Toast.makeText(this, "Audio o/p via " +myIntent.getStringExtra("output") + "\n" + "Audio i/p via " +myIntent.getStringExtra("input"), Toast.LENGTH_SHORT);
        ViewGroup myGroup = (ViewGroup) myToast.getView();
        TextView messageTextView = (TextView) myGroup.getChildAt(0);
        messageTextView.setTextSize(50);
        messageTextView.setTextColor(Color.YELLOW);
        myToast.show();

        try {
                myMediaPlayer = MediaPlayer.create(this, R.raw.sound);
                myMediaPlayer.setVolume(50, 50);
        }catch(Exception e){
            e.printStackTrace();
        }

        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar volumeControl = (SeekBar) findViewById(R.id.seekBar);
        final SeekBar scrubBar = (SeekBar) findViewById(R.id.seekBar2);

        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curVolume);

        scrubBar.setMax(myMediaPlayer.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubBar.setProgress(myMediaPlayer.getCurrentPosition());
            }
        },0, 100);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.i("SeekBar Value", Integer.toString(progress));

                myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrubBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.i("Scrub bar value" , Integer.toString(progress));
                myMediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //myMediaPlayer.start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.pause();
            }
        });
    }
}